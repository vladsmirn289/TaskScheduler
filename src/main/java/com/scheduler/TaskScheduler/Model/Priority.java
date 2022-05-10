package com.scheduler.TaskScheduler.Model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Priority {
    NO, LOW, MEDIUM, HIGH;

    @JsonValue
    public String getPriority() {
        return name();
    }
}
