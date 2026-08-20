package com.rbacaudit.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/documents")
public class DocumentController {

    @GetMapping
    @PreAuthorize("hasAuthority('DOCUMENT_VIEW')")
    public ResponseEntity<List<Map<String, Object>>> listDocuments() {
        List<Map<String, Object>> documents = List.of(
                Map.of("id", 1, "title", "Q1 Budget Report"),
                Map.of("id", 2, "title", "Employee Handbook")
        );
        return ResponseEntity.ok(documents);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('DOCUMENT_EDIT')")
    public ResponseEntity<Map<String, String>> createDocument(@RequestBody Map<String, String> request) {
        return ResponseEntity.ok(Map.of(
                "message", "Document created (mock - not actually persisted)",
                "title", request.getOrDefault("title", "Untitled")
        ));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DOCUMENT_DELETE')")
    public ResponseEntity<Map<String, String>> deleteDocument(@PathVariable Long id) {
        return ResponseEntity.ok(Map.of(
                "message", "Document deleted (mock - not actually persisted)",
                "id", id.toString()
        ));
    }
}