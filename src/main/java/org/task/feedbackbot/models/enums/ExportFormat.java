package org.task.feedbackbot.models.enums;

import lombok.Getter;

@Getter
public enum ExportFormat {
    CSV(".csv"),
    EXCEL(".xlsx");

    private final String format;

    ExportFormat(String format) {
        this.format = format;
    }
}

