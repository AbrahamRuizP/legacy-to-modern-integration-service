package com.legacy.integration.xml;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CustomerXpathExtractorTest {

    private static final String CUSTOMER_XML = """
            <?xml version="1.0" encoding="UTF-8"?>
            <customer xmlns="http://legacy.integration/customer">
                <id>8f6a10d4-b574-4162-916e-fc3349bb528a</id>
                <firstName>John</firstName>
                <lastName>Smith</lastName>
                <createdAt>2026-09-01T10:30:00Z</createdAt>
                <status>ACTIVE</status>
            </customer>
            """;

    private CustomerXPathExtractor extractor;

    @BeforeEach
    void setUp() {
        extractor = new CustomerXPathExtractor();
    }

    @Test
    void extractsFirstName() {
        assertEquals("John", extractor.extractFirstName(CUSTOMER_XML));
    }

    @Test
    void extractsLastName() {
        assertEquals("Smith", extractor.extractLastName(CUSTOMER_XML));
    }

    @Test
    void extractsStatus() {
        assertEquals("ACTIVE", extractor.extractStatus(CUSTOMER_XML));
    }

    @Test
    void extractsCreatedAt() {
        assertEquals("2026-09-01T10:30:00Z", extractor.extractCreatedAt(CUSTOMER_XML));
    }

    @Test
    void extractsId() {
        assertEquals("8f6a10d4-b574-4162-916e-fc3349bb528a", extractor.extractId(CUSTOMER_XML));
    }

    @Test
    void rejectsMalformedXml() {
        assertThrows(IllegalArgumentException.class,
                () -> extractor.extractFirstName("<customer>"));
    }
}
