package com.zeotap.assignment;

import com.zeotap.assignment.model.*;
import com.zeotap.assignment.service.ClickHouseService;
import com.zeotap.assignment.service.FileService;
import com.zeotap.assignment.service.IngestionService;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.sql.ResultSet;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class IngestionServiceTest {

    @InjectMocks
    private IngestionService ingestionService;

    @Mock
    private ClickHouseService clickHouseService;

    @Mock
    private FileService fileService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testClickHouseToFile() throws Exception {
        IngestionRequestClickHouseToFile req = new IngestionRequestClickHouseToFile();
        req.setTableName("table");
        req.setColumns(List.of("id", "name"));
        req.setConnection(new ClickHouseConnectionRequest());
        req.setOutputFilePath("file.csv");

        // Mock ResultSet
        ResultSet rs = mock(ResultSet.class);
        when(rs.next()).thenReturn(true, false); // one row
        when(rs.getString("id")).thenReturn("1");
        when(rs.getString("name")).thenReturn("Alice");

        when(clickHouseService.queryData(anyString(), any())).thenReturn(rs);

        IngestionResponse res = ingestionService.clickHouseToFile(req);

        assertEquals(1, res.getRecordCount());
        verify(fileService).writeCSV(eq("file.csv"), eq(List.of("id", "name")), any());
    }


    @Test
    void testFileToClickHouse() throws Exception {
        IngestionRequestFileToClickHouse req = new IngestionRequestFileToClickHouse();
        req.setTargetTable("table");
        req.setColumns(List.of("id", "name"));
        req.setConnection(new ClickHouseConnectionRequest());
        req.setInputFilePath("file.csv");

        List<Map<String, String>> records = List.of(Map.of("id", "1", "name", "Alice"));
        when(fileService.readCSV(anyString(), any())).thenReturn(records);

        IngestionResponse res = ingestionService.fileToClickHouse(req);
        assertEquals(1, res.getRecordCount());
    }
}
