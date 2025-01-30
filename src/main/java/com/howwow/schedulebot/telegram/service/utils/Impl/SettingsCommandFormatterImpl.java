package com.howwow.schedulebot.telegram.service.utils.Impl;

import com.howwow.schedulebot.chat.dto.response.FoundedChatResponse;
import com.howwow.schedulebot.telegram.service.utils.SettingsCommandFormatter;
import org.springframework.stereotype.Component;

@Component
public class SettingsCommandFormatterImpl implements SettingsCommandFormatter {

    @Override
    public String format(FoundedChatResponse foundedChatResponse) {
        return """
            🛠 *Настройки бота*
            
            📌 **Группа:** %s
            ⏰ **Время напоминания:** %s
            
            _Для изменения настроек используйте соответствующие команды_
            """
                .formatted(
                        (foundedChatResponse.groupName() != null && !foundedChatResponse.groupName().isBlank()) ? foundedChatResponse.groupName() : "Пусто",
                        foundedChatResponse.deliveryTime() != null? foundedChatResponse.deliveryTime() : "Пусто"
                );
    }
}
