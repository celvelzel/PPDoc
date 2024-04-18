package com.majy.ppdocapi.service;

public interface ModelService
{
    String extractInfo(String modelName, String ocrResult, String keyInfo);
    String generateSummary(String modelName, String ocrResult, String summaryType);
    String classification(String modelName, String ocrResult);
}
