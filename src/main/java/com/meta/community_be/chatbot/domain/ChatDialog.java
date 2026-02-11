package com.meta.community_be.chatbot.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "chat_dialog")
public class ChatDialog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String contents;

    @Column(name = "sender_type")
    @Enumerated(EnumType.STRING)
    private SenderType senderType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatroom_id", nullable = false)
    private ChatRoom chatRoom;

    public ChatDialog(ChatRoom chatRoom, SenderType senderType,  String contents) {
        this.chatRoom = chatRoom;
        this.senderType = senderType;
        this.contents = contents;
    }
}
