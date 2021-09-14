package com.textcaptcha.textingest.dto;

import java.util.List;
import java.util.Map;

public class CorefApiResponse {

    private Map<String, List<Integer>> predictions;
    private Map<Integer, Integer> clusters;
    private Map<Integer, Double> scores;

    public Map<String, List<Integer>> getPredictions() {
        return predictions;
    }

    public void setPredictions(Map<String, List<Integer>> predictions) {
        this.predictions = predictions;
    }

    public Map<Integer, Integer> getClusters() {
        return clusters;
    }

    public void setClusters(Map<Integer, Integer> clusters) {
        this.clusters = clusters;
    }

    public Map<Integer, Double> getScores() {
        return scores;
    }

    public void setScores(Map<Integer, Double> scores) {
        this.scores = scores;
    }

}
