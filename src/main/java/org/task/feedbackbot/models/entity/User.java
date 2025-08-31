package org.task.feedbackbot.models.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.task.feedbackbot.models.enums.Position;
import org.task.feedbackbot.models.enums.UserState;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    private Long telegramId;

    @Enumerated(EnumType.STRING)
    private Position position;

    private String branch;

    @Enumerated(EnumType.STRING)
    private UserState state = UserState.NEW;

    @Builder.Default
    private LocalDateTime registeredAt = LocalDateTime.now();

    @Builder.Default
    private LocalDateTime lastActiveAt = LocalDateTime.now();
}
