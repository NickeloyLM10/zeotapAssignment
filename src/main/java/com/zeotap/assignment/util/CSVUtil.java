package com.zeotap.assignment.util;

import org.apache.commons.csv.*;

import java.io.*;
import java.util.*;

public class CSVUtil {

    public static List<Map<String, String>> parseCSV(String filePath, List<String> selectedColumns) throws IOException {
        List<Map<String, String>> records = new ArrayList<>();

        try (Reader reader = new FileReader(filePath);
             CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

            for (CSVRecord record : parser) {
                Map<String, String> row = new HashMap<>();
                for (String column : selectedColumns) {
                    row.put(column, record.get(column));
                }
                records.add(row);
            }
        }

        return records;
    }

    public static void writeCSV(String filePath, List<String> headers, List<List<String>> rows) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
             CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(headers.toArray(new String[0])))) {

            for (List<String> row : rows) {
                printer.printRecord(row);
            }
        }
    }
}
