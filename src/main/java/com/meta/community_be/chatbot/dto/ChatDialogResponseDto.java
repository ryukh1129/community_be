package com.meta.community_be.chatbot.dto;

import com.meta.community_be.chatbot.domain.ChatDialog;
import com.meta.community_be.chatbot.domain.SenderType;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChatDialogResponseDto {
    private Long id;
    private SenderType senderType;
    private String contents;
    private LocalDateTime createdAt;

    public ChatDialogResponseDto(ChatDialog chatDialog) {
        this.id = chatDialog.getId();
        this.senderType = chatDialog.getSenderType();
        this.contents = chatDialog.getContents();
        this.createdAt = chatDialog.getCreatedAt();
    }
}