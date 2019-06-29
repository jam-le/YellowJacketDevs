package com.example.readyourresults.Camera;

import java.util.HashMap;

public interface ResultsInterpreted {
    public void resultsInterpreted(HashMap<String, Float> labelConfidences);
    public void resultsInterpreted(HashMap<String, Float> labelConfidences, String maxLabel);
    public void resultsInterpreted(HashMap<String, Float> labelConfidences, String maxLabel, float maxConfidence);
}