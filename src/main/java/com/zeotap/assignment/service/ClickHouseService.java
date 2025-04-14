package com.zeotap.assignment.service;

import com.zeotap.assignment.model.ClickHouseConnectionRequest;
import com.zeotap.assignment.model.ColumnSelectionRequest;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service
public class ClickHouseService {

    public boolean testConnection(ClickHouseConnectionRequest request) {
        try (Connection conn = getConnection(request)) {
            return conn != null && !conn.isClosed();
        } catch (Exception e) {
            return false;
        }
    }

    public List<String> getTables(ClickHouseConnectionRequest request) {
        List<String> tables = new ArrayList<>();
        try (Connection conn = getConnection(request);
             ResultSet rs = conn.getMetaData().getTables(null, null, "%", new String[]{"TABLE"})) {
            while (rs.next()) {
                tables.add(rs.getString("TABLE_NAME"));
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch tables", e);
        }
        return tables;
    }

    public List<String> getColumns(ColumnSelectionRequest request) {
        List<String> columns = new ArrayList<>();
        try (Connection conn = getConnection(request.getConnection());
             ResultSet rs = conn.createStatement().executeQuery("DESCRIBE TABLE " + request.getTableName())) {
            while (rs.next()) {
                columns.add(rs.getString(1)); // First column is column name
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch columns", e);
        }
        return columns;
    }

    public ResultSet queryData(String query, ClickHouseConnectionRequest request) throws Exception {
        Connection conn = getConnection(request);
        return conn.createStatement().executeQuery(query);
    }

    public void insertData(String insertQuery, ClickHouseConnectionRequest request) throws Exception {
        try (Connection conn = getConnection(request)) {
            conn.createStatement().execute(insertQuery);
        }
    }

    private Connection getConnection(ClickHouseConnectionRequest request) throws Exception {
        String url = "jdbc:clickhouse://" + request.getHost() + ":" + request.getPort() + "/" + request.getDatabase();
        Properties props = new Properties();
        props.setProperty("user", request.getUser());
        props.setProperty("password", request.getJwtToken()); // if ClickHouse uses JWT as password
        return DriverManager.getConnection(url, props);
    }
}
