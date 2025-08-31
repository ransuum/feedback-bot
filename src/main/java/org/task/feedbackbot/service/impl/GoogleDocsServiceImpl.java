package org.task.feedbackbot.service.impl;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.docs.v1.Docs;
import com.google.api.services.docs.v1.DocsScopes;
import com.google.api.services.docs.v1.model.BatchUpdateDocumentRequest;
import com.google.api.services.docs.v1.model.InsertTextRequest;
import com.google.api.services.docs.v1.model.Location;
import com.google.api.services.docs.v1.model.Request;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.task.feedbackbot.exception.GoogleDocsException;
import org.task.feedbackbot.models.entity.Feedback;
import org.task.feedbackbot.service.GoogleDocsService;

import java.time.format.DateTimeFormatter;
import java.util.Collections;

@Service
@Slf4j
public class GoogleDocsServiceImpl implements GoogleDocsService {

    @Value("${google.docs.document-id}")
    private String documentId;

    @Value("${google.docs.credentials-path}")
    private String credentialsPath;

    @Override
    public void addFeedbackToDoc(Feedback feedback) {
        try {
            final var service = createDocsService();

            final String feedbackText = String.format("""
                            
                            === НОВИЙ ВІДГУК ===
                            User Id: %d
                            Дата: %s
                            Посада: %s
                            Філія: %s
                            Тональність: %s
                            Критичність: %d/5
                            Повідомлення: %s
                            Рекомендації: %s
                            ================
                            
                            """,
                    feedback.getUser().getTelegramId(),
                    feedback.getCreatedAt().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")),
                    feedback.getUser().getPosition().getDisplayName(),
                    feedback.getUser().getBranch(),
                    feedback.getSentiment().name(),
                    feedback.getCriticalityLevel(),
                    feedback.getMessage(),
                    feedback.getSolution()
            );

            final var batchUpdateRequest = new BatchUpdateDocumentRequest()
                    .setRequests(Collections.singletonList(new Request()
                            .setInsertText(
                                    new InsertTextRequest()
                                            .setText(feedbackText)
                                            .setLocation(new Location().setIndex(1)))));

            service.documents().batchUpdate(documentId, batchUpdateRequest).execute();

            feedback.setSyncedToGoogleDocs(true);
            log.info("Feedback {} successfully synced to Google Docs", feedback.getId());
        } catch (Exception e) {
            log.error("Error syncing feedback to Google Docs", e);
        }
    }

    private Docs createDocsService() {
        final var resource = new ClassPathResource(credentialsPath);

        try {
            final var credential = GoogleCredential
                    .fromStream(resource.getInputStream())
                    .createScoped(Collections.singletonList(DocsScopes.DOCUMENTS));

            return new Docs.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    GsonFactory.getDefaultInstance(),
                    credential
            ).setApplicationName("Feedback Bot").build();
        } catch (Exception e) {
            throw new GoogleDocsException("Exception due creating docs service: " + e.getMessage());
        }
    }
}
