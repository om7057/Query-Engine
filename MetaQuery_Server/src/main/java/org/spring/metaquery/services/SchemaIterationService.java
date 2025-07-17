package org.spring.metaquery.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.spring.metaquery.entities.SchemaIteration;
import org.spring.metaquery.entities.Workspace;
import org.spring.metaquery.repositories.SchemaIterationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SchemaIterationService {
    private final SchemaIterationRepository schemaIterationRepository;

    public SchemaIteration getCurrentSchemaIterationByWorkspaceId(Long workspaceId) {
        log.info("Fetching current schema iteration for workspace ID: {}", workspaceId);
        return schemaIterationRepository.findTopByWorkspaceIdOrderByVersionDesc(workspaceId)
                .orElseThrow(() -> new RuntimeException("No schema iterations found for workspace ID: " + workspaceId));
    }

    public List<SchemaIteration> getAllSchemaIterationsByWorkspaceId(Long workspaceId) {
        log.info("Fetching all schema iterations for workspace ID: {}", workspaceId);
        List<SchemaIteration> schemaIterations = schemaIterationRepository.findAllByWorkspaceId(workspaceId);

        if (schemaIterations.isEmpty()) {
            log.warn("No schema iterations found for workspace ID: {}", workspaceId);
            throw new RuntimeException("No schema iterations found for workspace ID: " + workspaceId);
        }

        log.info("Found {} schema iterations for workspace ID: {}", schemaIterations.size(), workspaceId);
        return schemaIterations;
    }

    @Transactional
    public SchemaIteration createNewSchemaIteration(Long workspaceId, String ddl) {
        log.info("Creating new schema iteration for workspace ID: {}", workspaceId);

        Optional<SchemaIteration> latestIterationOpt =
                schemaIterationRepository.findTopByWorkspaceIdOrderByVersionDesc(workspaceId);

        int newVersion = latestIterationOpt
                .map(SchemaIteration::getVersion)
                .map(version -> version + 1)
                .orElse(1);

        Workspace workspace = latestIterationOpt.map(SchemaIteration::getWorkspace)
                .orElseThrow(() -> new RuntimeException("Workspace not found for ID: " + workspaceId));

        schemaIterationRepository.updateActiveByWorkspaceId(workspaceId, false);

        SchemaIteration newSchemaIteration = SchemaIteration.builder()
                .workspace(workspace)
                .version(newVersion)
                .ddl(ddl)
                .active(true)
                .build();

        return schemaIterationRepository.save(newSchemaIteration);
    }
}
