package com.zeotap.assignment.service;

import org.apache.commons.csv.*;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

@Service
public class FileService {

    // Read selected columns from CSV file
    public List<Map<String, String>> readCSV(String filePath, List<String> selectedColumns) {
        List<Map<String, String>> recordsList = new ArrayList<>();

        File file = new File(filePath);

        if (!file.exists()) {
            throw new RuntimeException("File not found at path: " + filePath);
        }

        try (Reader reader = new FileReader(filePath);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

            for (CSVRecord csvRecord : csvParser) {
                Map<String, String> row = new HashMap<>();
                for (String column : selectedColumns) {
                    row.put(column, csvRecord.get(column));
                }
                recordsList.add(row);
            }

        } catch (IOException e) {
            throw new RuntimeException("Error reading CSV file: " + filePath, e);
        }

        return recordsList;
    }

    // Write selected columns to CSV file
    public void writeCSV(String filePath, List<String> headers, List<List<String>> rows) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(headers.toArray(new String[0])))) {

            for (List<String> row : rows) {
                csvPrinter.printRecord(row);
            }

        } catch (IOException e) {
            throw new RuntimeException("Error writing CSV file: " + filePath, e);
        }
    }
}
