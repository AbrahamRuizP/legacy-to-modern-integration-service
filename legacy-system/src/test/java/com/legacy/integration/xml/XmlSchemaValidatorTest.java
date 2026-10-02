package com.legacy.integration.xml;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class XmlSchemaValidatorTest {

    private XmlSchemaValidator validator;

    @BeforeEach
    void setUp() {
        validator = new XmlSchemaValidator();
    }

    @Test
    void acceptsCustomerMatchingSchema() {
        String xml = """
                <?xml version="1.0" encoding="UTF-8"?>
                <customer xmlns="http://legacy.integration/customer">
                    <id>123e4567-e89b-12d3-a456-426614174000</id>
                    <firstName>Ada</firstName>
                    <lastName>Lovelace</lastName>
                    <createdAt>2024-01-15T10:30:00Z</createdAt>
                    <status>ACTIVE</status>
                </customer>
                """;

        assertDoesNotThrow(() -> validator.validate(inputStream(xml)));
    }

    @Test
    void rejectsCustomerWithInvalidId() {
        String xml = customerXml("not-a-uuid", "ACTIVE");

        assertThrows(IllegalArgumentException.class,
                () -> validator.validate(inputStream(xml)));
    }

    @Test
    void rejectsCustomerWithUnknownStatus() {
        String xml = customerXml("123e4567-e89b-12d3-a456-426614174000", "PENDING");

        assertThrows(IllegalArgumentException.class,
                () -> validator.validate(inputStream(xml)));
    }

    @Test
    void rejectsMalformedXml() {
        String xml = "<customer>";

        assertThrows(IllegalArgumentException.class,
                () -> validator.validate(inputStream(xml)));
    }

    private static String customerXml(String id, String status) {
        return """
                <customer xmlns="http://legacy.integration/customer">
                    <id>%s</id>
                    <firstName>Ada</firstName>
                    <lastName>Lovelace</lastName>
                    <createdAt>2024-01-15T10:30:00Z</createdAt>
                    <status>%s</status>
                </customer>
                """.formatted(id, status);
    }

    private static ByteArrayInputStream inputStream(String xml) {
        return new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
    }
}
