package org.spring.metaquery.repositories;

import org.spring.metaquery.entities.QueryInteraction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QueryInteractionRepository extends JpaRepository<QueryInteraction, Long> {
    List<QueryInteraction> getAllByWorkspaceId(Long workspaceId);
}
