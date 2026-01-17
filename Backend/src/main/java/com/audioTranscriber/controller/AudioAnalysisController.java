package com.audioTranscriber.controller;

import com.audioTranscriber.dto.AudioAnalysisRequest;
import com.audioTranscriber.dto.AudioAnalysisResponse;
import com.audioTranscriber.exception.AudioProcessingException;
import com.audioTranscriber.service.AudioAnalysisService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/audio/analysis")
public class AudioAnalysisController {

    private final AudioAnalysisService service;

    public AudioAnalysisController(AudioAnalysisService service) {
        this.service = service;
    }

    // 🔹 From classpath audio
    @PostMapping("/from-classpath")
    public ResponseEntity<AudioAnalysisResponse> fromClasspath(
            @RequestBody AudioAnalysisRequest request) {
        return ResponseEntity.ok(
                service.analyzeFromClasspath(request.fileName())
        );
    }

    // 🔹 From uploaded files
    @PostMapping(
            value = "/from-files",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<AudioAnalysisResponse> fromFiles(
            @RequestParam("audioFiles") List<MultipartFile> files) {
        return ResponseEntity.ok(service.analyzeFromFiles(files));
    }

    @ExceptionHandler(AudioProcessingException.class)
    public ResponseEntity<AudioAnalysisResponse> handle(AudioProcessingException ex) {
        return ResponseEntity.badRequest()
                .body(new AudioAnalysisResponse(ex.getMessage()));
    }
}
