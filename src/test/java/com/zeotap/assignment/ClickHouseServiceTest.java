package com.zeotap.assignment;

import com.zeotap.assignment.model.ClickHouseConnectionRequest;
import com.zeotap.assignment.model.ColumnSelectionRequest;
import com.zeotap.assignment.service.ClickHouseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ClickHouseServiceTest {

    private ClickHouseService service;
    private ClickHouseService spyService;

    @BeforeEach
    public void setup() {
        service = new ClickHouseService();
        spyService = Mockito.spy(service);
    }

    private ClickHouseConnectionRequest getMockRequest() {
        ClickHouseConnectionRequest req = new ClickHouseConnectionRequest();
        req.setHost("localhost");
        req.setPort(8123);
        req.setDatabase("default");
        req.setUser("default");
        req.setJwtToken("test-token");
        return req;
    }

    @Test
    public void testConnection_success() throws Exception {
        Connection mockConn = mock(Connection.class);
        when(mockConn.isClosed()).thenReturn(false);
        doReturn(mockConn).when(spyService).getConnection(any());

        boolean result = spyService.testConnection(getMockRequest());

        assertTrue(result);
        verify(mockConn).close();
    }

    @Test
    public void testConnection_failure() throws Exception {
        doThrow(new SQLException("Connection error")).when(spyService).getConnection(any());

        boolean result = spyService.testConnection(getMockRequest());

        assertFalse(result);
    }

    @Test
    public void testGetTables_success() throws Exception {
        Connection mockConn = mock(Connection.class);
        DatabaseMetaData mockMeta = mock(DatabaseMetaData.class);
        ResultSet mockRS = mock(ResultSet.class);

        when(mockConn.getMetaData()).thenReturn(mockMeta);
        when(mockMeta.getTables(null, null, "%", new String[]{"TABLE"})).thenReturn(mockRS);
        when(mockRS.next()).thenReturn(true, true, false);
        when(mockRS.getString("TABLE_NAME")).thenReturn("table1", "table2");

        doReturn(mockConn).when(spyService).getConnection(any());

        List<String> tables = spyService.getTables(getMockRequest());

        assertEquals(2, tables.size());
        assertTrue(tables.contains("table1"));
        assertTrue(tables.contains("table2"));
        verify(mockRS).close();
        verify(mockConn).close();
    }

    @Test
    public void testGetTables_failure() throws Exception {
        doThrow(new SQLException("error")).when(spyService).getConnection(any());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            spyService.getTables(getMockRequest());
        });

        assertTrue(ex.getMessage().contains("Failed to fetch tables"));
    }

    @Test
    public void testGetColumns_success() throws Exception {
        Connection mockConn = mock(Connection.class);
        ResultSet mockRS = mock(ResultSet.class);

        when(mockConn.createStatement()).thenReturn(mock(Statement.class));
        when(mockConn.createStatement().executeQuery(any())).thenReturn(mockRS);
        when(mockRS.next()).thenReturn(true, true, false);
        when(mockRS.getString(1)).thenReturn("col1", "col2");

        ColumnSelectionRequest columnRequest = new ColumnSelectionRequest();
        columnRequest.setConnection(getMockRequest());
        columnRequest.setTableName("sample");

        doReturn(mockConn).when(spyService).getConnection(any());

        List<String> columns = spyService.getColumns(columnRequest);

        assertEquals(2, columns.size());
        assertTrue(columns.contains("col1"));
        assertTrue(columns.contains("col2"));
    }

    @Test
    public void testGetColumns_failure() throws Exception {
        doThrow(new SQLException("error")).when(spyService).getConnection(any());

        ColumnSelectionRequest columnRequest = new ColumnSelectionRequest();
        columnRequest.setConnection(getMockRequest());
        columnRequest.setTableName("sample");

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            spyService.getColumns(columnRequest);
        });

        assertTrue(ex.getMessage().contains("Failed to fetch columns"));
    }
}
