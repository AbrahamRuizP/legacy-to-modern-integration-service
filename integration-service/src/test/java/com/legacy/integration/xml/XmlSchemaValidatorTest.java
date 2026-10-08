package com.legacy.integration.xml;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class XmlSchemaValidatorTest {

    private XmlSchemaValidator validator;

    @BeforeEach
    void setUp() {
        validator = new XmlSchemaValidator();
    }

    @Test
    void acceptsCustomerThatMatchesSchema() {
        String xml = customerXml(
                "123e4567-e89b-12d3-a456-426614174000",
                "2026-10-07T12:30:00Z",
                "ACTIVE"
        );

        assertDoesNotThrow(() -> validator.validate(xml));
    }

    @Test
    void rejectsCustomerWithInvalidId() {
        String xml = customerXml("not-a-uuid", "2026-10-07T12:30:00Z", "ACTIVE");

        assertThrows(IllegalStateException.class,
                () -> validator.validate(xml));
    }

    @Test
    void rejectsCustomerWithUnsupportedStatus() {
        String xml = customerXml(
                "123e4567-e89b-12d3-a456-426614174000",
                "2026-10-07T12:30:00Z",
                "PENDING"
        );

        assertThrows(IllegalStateException.class,
                () -> validator.validate(xml));
    }

    @Test
    void rejectsCustomerWithInvalidCreatedAt() {
        String xml = customerXml(
                "123e4567-e89b-12d3-a456-426614174000",
                "yesterday",
                "ACTIVE"
        );

        assertThrows(IllegalStateException.class,
                () -> validator.validate(xml));
    }

    @Test
    void rejectsMalformedXml() {
        assertThrows(IllegalStateException.class,
                () -> validator.validate("<customer>"));
    }

    private static String customerXml(String id, String createdAt, String status) {
        return """
                <?xml version="1.0" encoding="UTF-8"?>
                <customer xmlns="http://legacy.integration/customer">
                    <id>%s</id>
                    <firstName>Ada</firstName>
                    <lastName>Lovelace</lastName>
                    <createdAt>%s</createdAt>
                    <status>%s</status>
                </customer>
                """.formatted(id, createdAt, status);
    }
}
