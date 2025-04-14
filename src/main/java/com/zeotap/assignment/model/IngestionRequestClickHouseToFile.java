package com.zeotap.assignment.model;

import java.util.List;

public class IngestionRequestClickHouseToFile {
    private ClickHouseConnectionRequest connection;
    private String tableName;
    private List<String> columns;
    private String outputFilePath;

    // Getters and Setters
    public ClickHouseConnectionRequest getConnection() {
        return connection;
    }
    public void setConnection(ClickHouseConnectionRequest connection) {
        this.connection = connection;
    }

    public String getTableName() {
        return tableName;
    }
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<String> getColumns() {
        return columns;
    }
    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public String getOutputFilePath() {
        return outputFilePath;
    }
    public void setOutputFilePath(String outputFilePath) {
        this.outputFilePath = outputFilePath;
    }
}
