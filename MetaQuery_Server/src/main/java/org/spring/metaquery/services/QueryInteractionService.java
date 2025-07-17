package org.spring.metaquery.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.spring.metaquery.entities.QueryInteraction;
import org.spring.metaquery.entities.Workspace;
import org.spring.metaquery.enums.SqlDialect;
import org.spring.metaquery.repositories.QueryInteractionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class QueryInteractionService {
    private final QueryInteractionRepository queryInteractionRepository;
    private final WorkspaceService workspaceService;

    public List<QueryInteraction> getAllQueryInteractionsByWorkspaceId(Long workspaceId) {
        log.info("Fetching all query interactions for workspace ID: {}", workspaceId);
        List<QueryInteraction> queryInteractions = queryInteractionRepository.getAllByWorkspaceId(workspaceId);

        if (queryInteractions.isEmpty()) {
            log.warn("No query interactions found for workspace ID: {}", workspaceId);
            throw new RuntimeException("No query interactions found for workspace ID: " + workspaceId);
        }

        log.info("Found {} query interactions for workspace ID: {}", queryInteractions.size(), workspaceId);
        return queryInteractions;
    }

    public QueryInteraction createNewQueryInteraction(Long workspaceId, String userInput, String generatedSql,
                                                      SqlDialect sqlDialect) {
        log.info("Creating new query interaction for workspace ID: {} with User Input: {}", workspaceId, userInput);

        Workspace workspace = workspaceService.getWorkspaceById(workspaceId);

        QueryInteraction newQueryInteraction = QueryInteraction.builder()
                .workspace(workspace)
                .userInput(userInput)
                .generatedSql(generatedSql)
                .sqlDialect(sqlDialect)
                .feedbackPositive(null)
                .feedbackComment(null)
                .build();

        return queryInteractionRepository.save(newQueryInteraction);
    }

    public QueryInteraction updateQueryInteractionFeedback(Long interactionId, Boolean feedbackPositive,
                                                           String feedbackComment) {
        log.info("Updating query interaction feedback for interaction ID: {}", interactionId);

        QueryInteraction queryInteraction = queryInteractionRepository.findById(interactionId)
                .orElseThrow(() -> new RuntimeException("Query interaction not found with ID: " + interactionId));

        queryInteraction.setFeedbackPositive(feedbackPositive);
        queryInteraction.setFeedbackComment(feedbackComment);
        return queryInteractionRepository.save(queryInteraction);
    }
}
