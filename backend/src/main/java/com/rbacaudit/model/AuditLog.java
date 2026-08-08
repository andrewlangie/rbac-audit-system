package com.rbacaudit.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.Instant;

/**
 * Append-only by design, on purpose, in two layers:
 *   1. Java layer: no setters, no no-arg constructor, every field final-ish
 *      (set once in the constructor). There is simply no method on this
 *      class that lets you change a row after it's created.
 *   2. Database layer (see schema.sql / migration in Phase 4): the DB user
 *      the app connects with is granted INSERT and SELECT on this table
 *      only - UPDATE and DELETE are revoked at the grant level. So even a
 *      SQL injection or a bug in application code can't rewrite history.
 *
 * Storing "username" as a plain string (not a foreign key to User) is
 * deliberate too: if a user is later deleted, their audit trail should
 * survive intact rather than cascade-deleting or nulling out.
 */
@Entity
@Table(name = "audit_log")
@Getter
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String action; // e.g. "LOGIN", "LOGIN_FAILED", "DOCUMENT_DELETE", "ROLE_CHANGE"

    @Column
    private String details; // free-text context, e.g. "deleted document id=42"

    @Column(name = "ip_address")
    private String ipAddress;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Outcome outcome;

    @Column(nullable = false, updatable = false)
    private Instant timestamp = Instant.now();

    protected AuditLog() {
        // required by JPA, not used directly - keeps the public API to
        // "construct with everything already known", never "build then mutate"
    }

    public AuditLog(String username, String action, String details, String ipAddress, Outcome outcome) {
        this.username = username;
        this.action = action;
        this.details = details;
        this.ipAddress = ipAddress;
        this.outcome = outcome;
        this.timestamp = Instant.now();
    }

    public enum Outcome {
        SUCCESS, FAILURE
    }
}
