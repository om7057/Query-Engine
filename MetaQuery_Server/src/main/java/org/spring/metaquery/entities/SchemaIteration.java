package org.spring.metaquery.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Table(
  name = "schema_iterations",
  uniqueConstraints = @UniqueConstraint(
    columnNames = { "workspace_id", "version" }
  )
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SchemaIteration {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "workspace_id", nullable = false)
  private Workspace workspace;

  @Column(name = "version", nullable = false)
  private Integer version;

  @Column(name = "ddl", columnDefinition = "TEXT", nullable = false)
  private String ddl;

  @Column(name = "active", nullable = false)
  @Builder.Default
  private Boolean active = false;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @PrePersist
  protected void onCreate() {
    createdAt = LocalDateTime.now();
  }
}
