package com.legacy.integration.customer.repository;

import com.legacy.integration.customer.entity.Customer;
import com.legacy.integration.customer.entity.CustomerStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class CustomerRepository {

    @PersistenceContext(unitName = "legacyPersistenceUnit")
    private EntityManager entityManager;

    public Customer findById(UUID id) {
        return entityManager.find(Customer.class, id);
    }

    public List<Customer> findByStatus(CustomerStatus status) {
        return entityManager.createQuery(
                        """
                                SELECT c FROM Customer c
                                WHERE c.status = :status
                                """,
                        Customer.class
                )
                .setParameter("status", status)
                .getResultList();
    }

    public List<Customer> findByEmailContaining(String text) {
        return entityManager.createQuery(
                """
                        SELECT c FROM Customer c
                        WHERE LOWER(c.email) LIKE CONCAT('%', LOWER(:text), '%')
                        """,
                Customer.class
        )
                .setParameter("text", text.trim())
                .getResultList();
    }

    public List<Customer> findCreateBetween(Instant start, Instant end) {
        return entityManager.createQuery(
                """
                        SELECT c FROM Customer c
                        WHERE c.createdAt BETWEEN :start AND :end
                        """,
                Customer.class
        )
                .setParameter("end", end)
                .setParameter("start", start)
                .getResultList();
    }

    public List<Customer> findAll() {
        return entityManager.createQuery(
                """
                        SELECT c FROM Customer c
                        """,
                Customer.class
        ).getResultList();
    }

    public List<Customer> findByFullName(String fullName) {
        return entityManager.createQuery(
                        """
                                SELECT c FROM Customer c
                                WHERE LOWER(CONCAT(CONCAT(c.firstName, ' '), c.lastName))
                                = LOWER(:fullName)
                                """,
                        Customer.class
                )
                .setParameter("fullName", fullName.trim())
                .getResultList();
    }
}
