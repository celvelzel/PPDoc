package com.majy.ppdocapi.service;

public interface ModelService
{
    String extractInfo(String modelType, String modelName, String ocrResult, String keyInfo);
    String generateSummary(String modelType, String modelName, String ocrResult, String summaryType);
    String classification(String modelType, String modelName, String ocrResult);
}
