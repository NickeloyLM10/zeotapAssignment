package com.zeotap.assignment.model;

public class ColumnSelectionRequest {
    private ClickHouseConnectionRequest connection;
    private String tableName;

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
}
