package org.spring.metaquery.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.spring.metaquery.dto.ApiResponse;
import org.spring.metaquery.dto.schema.SchemaInitRequest;
import org.spring.metaquery.dto.schema.SchemaInitResponse;
import org.spring.metaquery.services.SchemaIterationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/schema-iterations")
@RequiredArgsConstructor
public class SchemaIterationController {
    private final SchemaIterationService schemaIterationService;

    @GetMapping("/get-current-iteration/{workspaceId}")
    public ResponseEntity<ApiResponse<SchemaInitResponse>> getCurrentSchemaIteration(@PathVariable Long workspaceId) {
        log.info("Received request to get current schema iteration for workspace ID: {}", workspaceId);
        // TODO: Implement current schema iteration retrieval service
        throw new UnsupportedOperationException("Current schema iteration retrieval is not yet implemented.");
    }

    @GetMapping("/get-all-iteration/{workspaceId}")
    public ResponseEntity<ApiResponse<List<SchemaInitResponse>>> getSchemaIteration(@PathVariable Long workspaceId) {
        log.info("Received request to get schemas iteration for workspace ID: {}", workspaceId);
        // TODO: Implement schema iteration retrieval service
        throw new UnsupportedOperationException("Schema iteration retrieval is not yet implemented.");
    }

    @PostMapping("/init-schema")
    public ResponseEntity<ApiResponse<SchemaInitResponse>> initSchema(@RequestBody SchemaInitRequest request) {
        log.info("Received schema initialization request for workspace ID: {}", request.workspaceId());
        // TODO: Implement schema init service
        throw new UnsupportedOperationException("Schema initialization is not yet implemented.");
    }

    @PutMapping("/update-schema")
    public ResponseEntity<ApiResponse<SchemaInitResponse>> updateSchema(@RequestBody SchemaInitRequest request) {
        log.info("Received schema update request for workspace ID: {}", request.workspaceId());
        // TODO: Implement schema update service
        throw new UnsupportedOperationException("Schema update is not yet implemented.");
    }
}
