package com.howwow.schedulebot.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table
@Entity(name = "chat_settings")
public class ChatSettings extends AbstractEntity{

    @Column(nullable = false, name = "chat_id", unique = true)
    private Long chatId;

    @Column(name = "message_thread_id")
    private Integer messageThreadId;

    @Column(name = "group_name")
    private String groupName;

    @DateTimeFormat(pattern = "HH:mm")
    @Column(name = "delivery_time")
    private LocalTime deliveryTime;

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = false;

}
