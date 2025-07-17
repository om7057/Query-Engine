package org.spring.metaquery.dto.query;

/**
 * NewQueryRequest is a data transfer object that represents a request to create a new query.
 * It contains the workspace ID and the query text.
 */
public record NewQueryRequest(Long workspaceId, String queryText) {
}
