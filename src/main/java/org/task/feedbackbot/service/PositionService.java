package org.task.feedbackbot.service;

import jakarta.annotation.Nullable;
import org.task.feedbackbot.models.enums.Position;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

public interface PositionService {
    @Nullable
    Position findPositionByText(String text);

    ReplyKeyboardMarkup createPositionKeyboard();
}
