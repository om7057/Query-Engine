package org.spring.metaquery.repositories;

import org.spring.metaquery.entities.SchemaIteration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SchemaIterationRepository extends JpaRepository<SchemaIteration, Long> {
    Optional<SchemaIteration> findTopByWorkspaceIdOrderByVersionDesc(Long workspaceId);
    List<SchemaIteration> findAllByWorkspaceId(Long workspaceId);
    @Modifying
    @Query("UPDATE SchemaIteration si SET si.active = false WHERE si.workspace.id = :workspaceId")
    void updateActiveByWorkspaceId(Long workspaceId, boolean active);
}
