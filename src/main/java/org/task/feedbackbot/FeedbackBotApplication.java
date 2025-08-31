package org.task.feedbackbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FeedbackBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(FeedbackBotApplication.class, args);
    }

}
