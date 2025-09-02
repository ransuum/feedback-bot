package org.task.feedbackbot.service.impl;

import com.julienvey.trello.Trello;
import com.julienvey.trello.domain.Card;
import com.julienvey.trello.impl.TrelloImpl;
import com.julienvey.trello.impl.http.JDKTrelloHttpClient;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.task.feedbackbot.models.entity.Feedback;
import org.task.feedbackbot.service.TrelloService;

@Service
@Slf4j
public class TrelloServiceImpl implements TrelloService {

    @Value("${trello.api.key}")
    private String apiKey;

    @Value("${trello.api.token}")
    private String token;

    @Value("${trello.api.list-id}")
    private String listId;

    private Trello trello;

    @PostConstruct
    public void init() {
        this.trello = new TrelloImpl(apiKey, token, new JDKTrelloHttpClient());
    }

    @Override
    public String createCard(Feedback feedback) {
        try {
            final var cardName = String.format("Критичний відгук - %s (%s)",
                    feedback.getUser().getPosition().getDisplayName(),
                    feedback.getUser().getBranch());

            final var cardDescription = String.format("""
                            **Критичність:** %d/5
                            **Тональність:** %s
                            **Філія:** %s
                            **Посада:** %s
                            **Дата:** %s
                            
                            **Повідомлення:**
                            %s
                            
                            **Рекомендації для вирішення:**
                            %s
                            """,
                    feedback.getCriticalityLevel(),
                    feedback.getSentiment().name(),
                    feedback.getUser().getBranch(),
                    feedback.getUser().getPosition().getDisplayName(),
                    feedback.getCreatedAt(),
                    feedback.getMessage(),
                    feedback.getSolution()
            );

            final var card = new Card();
            card.setName(cardName);
            card.setDesc(cardDescription);

            final var created = trello.createCard(listId, card);
            return created.getId();
        } catch (Exception e) {
            log.error("Error creating Trello card for feedback {}", feedback.getId(), e);
            return null;
        }
    }
}
