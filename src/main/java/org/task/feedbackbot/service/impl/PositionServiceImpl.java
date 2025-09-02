package org.task.feedbackbot.service.impl;

import jakarta.annotation.Nullable;
import org.springframework.stereotype.Service;
import org.task.feedbackbot.models.enums.Position;
import org.task.feedbackbot.service.PositionService;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.Arrays;
import java.util.List;

@Service
public class PositionServiceImpl implements PositionService {

    @Nullable
    @Override
    public Position findPositionByText(String text) {
        return Arrays.stream(Position.values())
                .filter(pos ->
                        pos.name().equalsIgnoreCase(text) || pos.getDisplayName().equalsIgnoreCase(text))
                .findFirst()
                .orElse(null);
    }

    @Override
    public ReplyKeyboardMarkup createPositionKeyboard() {
        final List<KeyboardRow> keyboard = Arrays.stream(Position.values())
                .map(pos -> {
                    KeyboardRow row = new KeyboardRow();
                    row.add(new KeyboardButton(pos.getDisplayName()));
                    return row;
                })
                .toList();

        return ReplyKeyboardMarkup.builder()
                .keyboard(keyboard)
                .resizeKeyboard(true)
                .oneTimeKeyboard(true)
                .build();
    }
}
