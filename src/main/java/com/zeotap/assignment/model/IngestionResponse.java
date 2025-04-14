package com.zeotap.assignment.model;

public class IngestionResponse {
    private int recordCount;
    private String message;

    public IngestionResponse(int recordCount, String message) {
        this.recordCount = recordCount;
        this.message = message;
    }

    public int getRecordCount() {
        return recordCount;
    }
    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
