package com.textcaptcha.taskmanager.pojo;

public class SolutionCheckerResult {

    private final double sensitivity;
    private final double specificity;

    public SolutionCheckerResult(double sensitivity, double specificity) {
        this.sensitivity = sensitivity;
        this.specificity = specificity;
    }

    public double getSensitivity() {
        return sensitivity;
    }

    public double getSpecificity() {
        return specificity;
    }

    public boolean isSuccessful(double sensitivityThreshold, double specificityThreshold) {
        return sensitivity >= sensitivityThreshold && specificity >= specificityThreshold;
    }

    @Override
    public String toString() {
        return "SolutionCheckerResult{" +
                "sensitivity=" + sensitivity +
                ", specificity=" + specificity +
                '}';
    }
}
