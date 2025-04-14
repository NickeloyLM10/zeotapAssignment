package com.zeotap.assignment.controller;


import com.zeotap.assignment.model.*;
import com.zeotap.assignment.service.ClickHouseService;
import com.zeotap.assignment.service.FileService;
import com.zeotap.assignment.service.IngestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class IngestionController {

    @Autowired
    private ClickHouseService clickHouseService;

    @Autowired
    private FileService fileService;

    @Autowired
    private IngestionService ingestionService;

    // --- ClickHouse connection test ---
    @PostMapping("/connect-clickhouse")
    public ResponseEntity<String> connectToClickHouse(@RequestBody ClickHouseConnectionRequest request) {
        boolean success = clickHouseService.testConnection(request);
        return success ? ResponseEntity.ok("Connection successful")
                : ResponseEntity.badRequest().body("Connection failed");
    }

    // --- Get ClickHouse tables ---
    @PostMapping("/clickhouse/tables")
    public ResponseEntity<List<String>> getTables(@RequestBody ClickHouseConnectionRequest request) {
        return ResponseEntity.ok(clickHouseService.getTables(request));
    }

    // --- Get columns for a table ---
    @PostMapping("/clickhouse/columns")
    public ResponseEntity<List<String>> getColumns(@RequestBody ColumnSelectionRequest request) {
        return ResponseEntity.ok(clickHouseService.getColumns(request));
    }

    // --- ClickHouse to Flat File ---
    @PostMapping("/ingest/clickhouse-to-file")
    public ResponseEntity<IngestionResponse> ingestClickHouseToFile(@RequestBody IngestionRequestClickHouseToFile request) {
        IngestionResponse result = ingestionService.clickHouseToFile(request);
        return ResponseEntity.ok(result);
    }

    // --- Flat File to ClickHouse ---
    @PostMapping("/ingest/file-to-clickhouse")
    public ResponseEntity<IngestionResponse> ingestFileToClickHouse(@RequestBody IngestionRequestFileToClickHouse request) {
        IngestionResponse result = ingestionService.fileToClickHouse(request);
        return ResponseEntity.ok(result);
    }
}
