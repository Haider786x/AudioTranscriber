package com.audioTranscriber.dto;

public record AudioAnalysisRequest(
        String fileName,
        String prompt
) {}
