package org.spring.metaquery.repositories;

import org.spring.metaquery.entities.User;
import org.spring.metaquery.entities.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {
    Optional<Workspace> findByWorkspaceName(String name);
    List<Workspace> findAllByUser(User user);
}
