package com.howwow.schedulebot.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table
@Entity(name = "cites")
public class Cite extends AbstractEntity {

    @Column(nullable = false)
    private String cite;

    @Column(nullable = false, name = "user_id")
    private Integer userId;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private ChatSettings chatId;
}
