package org.task.feedbackbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.task.feedbackbot.models.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
