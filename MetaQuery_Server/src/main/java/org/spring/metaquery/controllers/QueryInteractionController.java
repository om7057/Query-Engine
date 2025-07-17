package org.spring.metaquery.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.spring.metaquery.dto.ApiResponse;
import org.spring.metaquery.dto.query.NewQueryRequest;
import org.spring.metaquery.services.QueryInteractionService;
import org.spring.metaquery.services.SchemaIterationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/query-interactions")
@RequiredArgsConstructor
public class QueryInteractionController {
    private final SchemaIterationService schemaIterationService;
    private final QueryInteractionService queryInteractionService;

    @GetMapping("/{workspaceId}")
    public ResponseEntity<ApiResponse<?>> getQueryInteractions(@PathVariable Long workspaceId) {
        log.info("Received request to get query interactions for workspace ID: {}", workspaceId);
        // TODO: Implement query interaction retrieval service
        throw new UnsupportedOperationException("Query interaction retrieval is not yet implemented.");
    }

    @PostMapping("/new-query")
    public ResponseEntity<ApiResponse<?>> newQuery(@RequestBody NewQueryRequest request) {
        log.info("Received new query request for workspace ID: {}, Query: {}", request.workspaceId(),
                request.queryText());
        // TODO: Implement new query service
        throw new UnsupportedOperationException("New query request is not yet implemented.");
    }
}
