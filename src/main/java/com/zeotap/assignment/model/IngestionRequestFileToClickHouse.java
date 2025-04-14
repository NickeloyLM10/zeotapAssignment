package com.zeotap.assignment.model;

import java.util.List;

public class IngestionRequestFileToClickHouse {
    private ClickHouseConnectionRequest connection;
    private String targetTable;
    private List<String> columns;
    private String inputFilePath;

    // Getters and Setters
    public ClickHouseConnectionRequest getConnection() {
        return connection;
    }
    public void setConnection(ClickHouseConnectionRequest connection) {
        this.connection = connection;
    }

    public String getTargetTable() {
        return targetTable;
    }
    public void setTargetTable(String targetTable) {
        this.targetTable = targetTable;
    }

    public List<String> getColumns() {
        return columns;
    }
    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public String getInputFilePath() {
        return inputFilePath;
    }
    public void setInputFilePath(String inputFilePath) {
        this.inputFilePath = inputFilePath;
    }
}
