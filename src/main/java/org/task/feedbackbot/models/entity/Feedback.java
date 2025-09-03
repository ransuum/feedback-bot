package org.task.feedbackbot.models.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.task.feedbackbot.models.enums.SentimentType;

import java.time.LocalDateTime;

@Entity
@Table(name = "feedback")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Enumerated(EnumType.STRING)
    private SentimentType sentiment;

    private Integer criticalityLevel;

    @Column(columnDefinition = "TEXT")
    private String solution;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    private String trelloCardId;

    @Builder.Default
    private Boolean syncedToGoogleDocs = false;

    public boolean isCritical() {
        return criticalityLevel >= 4;
    }
}
