package com.zeotap.assignment.service;

import com.zeotap.assignment.model.IngestionRequestClickHouseToFile;
import com.zeotap.assignment.model.IngestionRequestFileToClickHouse;
import com.zeotap.assignment.model.IngestionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.util.*;

@Service
public class IngestionService {

    @Autowired
    private ClickHouseService clickHouseService;

    @Autowired
    private FileService fileService;

    public IngestionResponse clickHouseToFile(IngestionRequestClickHouseToFile request) {
        List<List<String>> rows = new ArrayList<>();
        String query = "SELECT " + String.join(", ", request.getColumns())
                + " FROM " + request.getTableName();

        try (ResultSet rs = clickHouseService.queryData(query, request.getConnection())) {
            while (rs.next()) {
                List<String> row = new ArrayList<>();
                for (String col : request.getColumns()) {
                    row.add(rs.getString(col));
                }
                rows.add(row);
            }
            fileService.writeCSV(request.getOutputFilePath(), request.getColumns(), rows);
            return new IngestionResponse(rows.size(), "Exported ClickHouse to File successfully");

        } catch (Exception e) {
            throw new RuntimeException("ClickHouse to File failed", e);
        }
    }

    public IngestionResponse fileToClickHouse(IngestionRequestFileToClickHouse request) {
        List<Map<String, String>> records = fileService.readCSV(request.getInputFilePath(), request.getColumns());
        int inserted = 0;

        try {
            for (Map<String, String> row : records) {
                String values = String.join(", ", row.values().stream()
                        .map(val -> "'" + val.replace("'", "") + "'").toList());
                String insertQuery = "INSERT INTO " + request.getTargetTable()
                        + " (" + String.join(", ", row.keySet()) + ") VALUES (" + values + ")";
                clickHouseService.insertData(insertQuery, request.getConnection());
                inserted++;
            }
            return new IngestionResponse(inserted, "Imported File to ClickHouse successfully");
        } catch (Exception e) {
            throw new RuntimeException("File to ClickHouse failed", e);
        }
    }
}
