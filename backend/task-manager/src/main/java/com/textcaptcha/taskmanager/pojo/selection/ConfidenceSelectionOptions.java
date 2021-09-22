package com.textcaptcha.taskmanager.pojo.selection;

import java.util.List;

public class ConfidenceSelectionOptions implements SelectionOptions {

    final float confidenceThreshold;
    final boolean shouldBeAboveThreshold;
    final List<Long> ignoredTaskIds;

    public ConfidenceSelectionOptions(float confidenceThreshold, boolean shouldBeAboveThreshold, List<Long> ignoredTaskIds) {
        this.confidenceThreshold = confidenceThreshold;
        this.shouldBeAboveThreshold = shouldBeAboveThreshold;
        this.ignoredTaskIds = ignoredTaskIds;
    }

    public float getConfidenceThreshold() {
        return confidenceThreshold;
    }

    public boolean shouldBeAboveThreshold() {
        return shouldBeAboveThreshold;
    }

    public List<Long> getIgnoredTaskIds() {
        return ignoredTaskIds;
    }

}
