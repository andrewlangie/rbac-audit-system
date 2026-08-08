package com.rbacaudit.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A single, granular capability - e.g. "DOCUMENT_DELETE" or "USER_MANAGE".
 * Roles are just named bundles of these. Keeping permissions as their own
 * entity (rather than hardcoded strings on Role) means you can add a new
 * permission or reassign it to a role without touching any Java code.
 */
@Entity
@Table(name = "permissions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name; // e.g. "DOCUMENT_DELETE", "USER_MANAGE", "AUDIT_VIEW"

    @Column(nullable = false)
    private String description;

    public Permission(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
