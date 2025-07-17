package org.spring.metaquery.dto.schema;

/**
 * SchemaInitRequest is a data transfer object that represents a request to initialize a schema.
 * It contains the workspace ID and a user prompt for schema initialization.
 */
public record SchemaInitRequest(Long workspaceId, String userPrompt) {
}
