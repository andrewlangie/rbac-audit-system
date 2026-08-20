package com.rbacaudit.repository;

import com.rbacaudit.model.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface AuditLogRepository extends Repository<AuditLog, Long> {
    AuditLog save(AuditLog auditLog);

    List<AuditLog> findByUsername(String username);

    List<AuditLog> findByAction(String Action);

    List<AuditLog> findAll(Pageable pageable);
}