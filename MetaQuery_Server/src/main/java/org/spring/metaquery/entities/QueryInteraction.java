package org.spring.metaquery.entities;

import jakarta.persistence.*;
import lombok.*;
import org.spring.metaquery.enums.SqlDialect;

import java.time.LocalDateTime;

@Entity
@Table(name = "query_interactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QueryInteraction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "workspace_id", nullable = false)
    private Workspace workspace;

    @Column(name = "user_input", columnDefinition = "TEXT", nullable = false)
    private String userInput;

    @Column(name = "generated_sql", columnDefinition = "TEXT", nullable = false)
    private String generatedSql;

    @Column(name = "sql_dialect", nullable = false)
    private SqlDialect sqlDialect;

    @Column(name = "feedback_positive")
    private Boolean feedbackPositive;

    @Column(name = "feedback_comment", columnDefinition = "TEXT")
    private String feedbackComment;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
