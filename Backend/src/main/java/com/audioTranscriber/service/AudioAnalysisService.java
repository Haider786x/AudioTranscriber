package com.audioTranscriber.service;

import com.audioTranscriber.dto.AudioAnalysisResponse;
import com.audioTranscriber.exception.AudioProcessingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.InputStream;
import java.time.Duration;
import java.util.List;
import java.util.Map;

@Service
public class AudioAnalysisService {

    private final WebClient webClient;

    public AudioAnalysisService(
            @Value("${assemblyai.api.key}") String apiKey,
            @Value("${assemblyai.api.base-url}") String baseUrl) {

        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("Authorization", apiKey)
                .build();
    }

    // ✅ SCENARIO 1: Classpath audio
    public AudioAnalysisResponse analyzeFromClasspath(String fileName) {
        try {
            ClassPathResource resource = new ClassPathResource("audio/" + fileName);
            if (!resource.exists()) {
                throw new AudioProcessingException("File not found: audio/" + fileName);
            }

            try (InputStream is = resource.getInputStream()) {
                byte[] audioBytes = is.readAllBytes();
                return transcribe(audioBytes);
            }

        } catch (Exception e) {
            throw new AudioProcessingException("Audio processing failed: " + e.getMessage());
        }
    }

    // ✅ SCENARIO 2: Multipart upload
    public AudioAnalysisResponse analyzeFromFiles(List<MultipartFile> files) {
        try {
            StringBuilder result = new StringBuilder();

            for (MultipartFile file : files) {
                result.append(transcribe(file.getBytes()).response())
                        .append("\n");
            }

            return new AudioAnalysisResponse(result.toString());

        } catch (Exception e) {
            throw new AudioProcessingException("Audio upload failed: " + e.getMessage());
        }
    }

    // 🔥 AssemblyAI transcription flow
    private AudioAnalysisResponse transcribe(byte[] audioBytes) {

        // 1️⃣ Upload audio
        String uploadUrl = webClient.post()
                .uri("/upload")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .bodyValue(audioBytes)
                .retrieve()
                .bodyToMono(Map.class)
                .map(res -> res.get("upload_url").toString())
                .block(Duration.ofSeconds(30));

        // 2️⃣ Request transcription
        Map<String, String> transcriptRequest = Map.of(
                "audio_url", uploadUrl
        );

        String transcriptId = webClient.post()
                .uri("/transcript")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(transcriptRequest)
                .retrieve()
                .bodyToMono(Map.class)
                .map(res -> res.get("id").toString())
                .block(Duration.ofSeconds(30));

        // 3️⃣ Poll until completed
        while (true) {
            Map<String, Object> statusResponse = webClient.get()
                    .uri("/transcript/" + transcriptId)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block(Duration.ofSeconds(30));

            String status = statusResponse.get("status").toString();

            if ("completed".equals(status)) {
                return new AudioAnalysisResponse(
                        statusResponse.get("text").toString()
                );
            }

            if ("error".equals(status)) {
                throw new AudioProcessingException(
                        statusResponse.get("error").toString()
                );
            }

            try {
                Thread.sleep(2000);
            } catch (InterruptedException ignored) {}
        }
    }
}
