package com.zeotap.assignment;

import com.zeotap.assignment.service.FileService;
import org.junit.jupiter.api.*;
import java.io.File;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class FileServiceTest {

    private FileService fileService;
    private final String testFilePath = "./data/test_output.csv";

    @BeforeEach
    void setUp() {
        fileService = new FileService();
    }

    @Test
    void testWriteAndReadCSV() {
        List<String> headers = List.of("id", "name");
        List<List<String>> rows = List.of(
                List.of("1", "Alice"),
                List.of("2", "Bob")
        );

        fileService.writeCSV(testFilePath, headers, rows);

        List<Map<String, String>> result = fileService.readCSV(testFilePath, headers);

        assertEquals(2, result.size());
        assertEquals("Alice", result.get(0).get("name"));

        new File(testFilePath).delete(); // cleanup
    }

    @Test
    void testReadCSV_fileNotFound() {
        Exception ex = assertThrows(RuntimeException.class, () -> {
            fileService.readCSV("nonexistent.csv", List.of("id"));
        });
        assertTrue(ex.getMessage().contains("File not found"));
    }
}
