package com.rbacaudit.config;

import com.rbacaudit.model.Permission;
import com.rbacaudit.model.Role;
import com.rbacaudit.model.User;
import com.rbacaudit.repository.PermissionRepository;
import com.rbacaudit.repository.RoleRepository;
import com.rbacaudit.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Runs once on startup. Guards every insert with an existence check so
 * it's safe to restart the app repeatedly without duplicating data.
 *
 * This exists purely so the app is demoable the second it boots - an
 * interviewer (or you, six months from now) shouldn't have to manually
 * INSERT rows before they can log in and see RBAC actually differentiate
 * behavior.
 */
@Component
public class DataSeeder implements CommandLineRunner {

    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public DataSeeder(PermissionRepository permissionRepository,
                       RoleRepository roleRepository,
                       UserRepository userRepository) {
        this.permissionRepository = permissionRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        Permission viewDocs = getOrCreatePermission("DOCUMENT_VIEW", "View documents");
        Permission editDocs = getOrCreatePermission("DOCUMENT_EDIT", "Edit documents");
        Permission deleteDocs = getOrCreatePermission("DOCUMENT_DELETE", "Delete documents");
        Permission manageUsers = getOrCreatePermission("USER_MANAGE", "Create/modify/deactivate users");
        Permission viewAudit = getOrCreatePermission("AUDIT_VIEW", "View the audit log");

        Role viewer = getOrCreateRole("VIEWER", Set.of(viewDocs));
        Role editor = getOrCreateRole("EDITOR", Set.of(viewDocs, editDocs));
        Role admin = getOrCreateRole("ADMIN", Set.of(viewDocs, editDocs, deleteDocs, manageUsers, viewAudit));

        seedUser("admin", "admin@example.com", "AdminPass123!", Set.of(admin));
        seedUser("editor", "editor@example.com", "EditorPass123!", Set.of(editor));
        seedUser("viewer", "viewer@example.com", "ViewerPass123!", Set.of(viewer));
    }

    private Permission getOrCreatePermission(String name, String description) {
        return permissionRepository.findByName(name)
                .orElseGet(() -> permissionRepository.save(new Permission(name, description)));
    }

    private Role getOrCreateRole(String name, Set<Permission> permissions) {
        return roleRepository.findByName(name)
                .orElseGet(() -> {
                    Role role = new Role(name);
                    role.setPermissions(permissions);
                    return roleRepository.save(role);
                });
    }

    private void seedUser(String username, String email, String rawPassword, Set<Role> roles) {
        if (userRepository.existsByUsername(username)) {
            return;
        }
        User user = new User(username, email, passwordEncoder.encode(rawPassword));
        user.setRoles(roles);
        userRepository.save(user);
    }
}
