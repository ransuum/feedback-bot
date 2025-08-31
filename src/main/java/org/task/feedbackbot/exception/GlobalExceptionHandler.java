package org.task.feedbackbot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FeedbackJsonProcessingException.class)
    public ResponseEntity<String> handleFeedbackJsonProcessing(FeedbackJsonProcessingException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("JSON processing error: " + ex.getMessage());
    }

    @ExceptionHandler(FeedbackAnalyzerException.class)
    public ResponseEntity<String> handleFeedbackAnalyzer(FeedbackAnalyzerException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Feedback analyzer error: " + ex.getMessage());
    }

    @ExceptionHandler(GoogleDocsException.class)
    public ResponseEntity<String> handleGoogleDocs(GoogleDocsException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Google Docs error: " + ex.getMessage());
    }

    @ExceptionHandler(OpenApiException.class)
    public ResponseEntity<String> handleOpenApi(OpenApiException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("OpenAI API error: " + ex.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleOtherRuntime(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Unexpected error: " + ex.getMessage());
    }
}

