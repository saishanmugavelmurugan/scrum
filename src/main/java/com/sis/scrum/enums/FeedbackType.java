package com.sis.scrum.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum FeedbackType {
    POSITIVE("Positive"), NEGATIVE("Negative"),IDEA("Idea"),PRAISE("Praise");
    private String feedbackType;

    FeedbackType(String feedbackType) {
        this.feedbackType = feedbackType;
    }

    public String toString () {
        return feedbackType;
    }

    @JsonValue
    public String getFeedbackType() {
        return feedbackType;
    }

    @JsonCreator
    public static FeedbackType fromValue(String value) {
        for (FeedbackType type : values()) {
            String currentType = type.getFeedbackType();
            if (currentType.equals(value)) {
                return type;
            }
        }

        // Return a response entity with a 400 Bad Request status
        throw new IllegalArgumentException("Invalid value for feedback type Enum: " + value);
    }
}
