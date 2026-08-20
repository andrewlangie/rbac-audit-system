package com.rbacaudit.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * A role is just a named collection of permissions (for ex. ADMIN will contain {DOCUMENT_DELETE,
 * USER_MANAGE, AUDIT_VIEW}). Users are assigned their respective roles, not permissions directly -
 * this indirection is the whole point of RBAC: change what a role can do in one
 * place and every user with that role updates automatically.
 */
@Entity
@Table(name = "Roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "role_permissions",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )

    private Set<Permission> permissions  = new HashSet<>();

    public Role(String name) {
        this.name = name;
    }
}