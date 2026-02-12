package com.meta.community_be.chatbot.dto;

import com.meta.community_be.chatbot.domain.ChatRoom;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChatRoomResponseDto {
    private Long id;
    private String title;
    private String authorNickname;

    public ChatRoomResponseDto(ChatRoom chatRoom) {
        this.id = chatRoom.getId();
        this.title = chatRoom.getTitle();
        this.authorNickname = chatRoom.getUser().getNickname();
    }
}