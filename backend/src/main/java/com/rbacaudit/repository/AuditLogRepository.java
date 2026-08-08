package com.rbacaudit.repository;

import com.rbacaudit.model.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

import java.util.List;

/**
 * Deliberately extends the bare-bones `Repository` interface instead of
 * `JpaRepository` or `CrudRepository`. Those parents come with deleteById(),
 * delete(), deleteAll() baked in - even if you never call them, they're
 * sitting there as an attractive nuisance for a future contributor (or a
 * bug). By hand-picking only save() and read methods, deleting an audit
 * row is not just discouraged, it's not compilable.
 */
public interface AuditLogRepository extends Repository<AuditLog, Long> {

    AuditLog save(AuditLog auditLog);

    List<AuditLog> findByUsername(String username);

    List<AuditLog> findByAction(String action);

    Page<AuditLog> findAll(Pageable pageable);
}
