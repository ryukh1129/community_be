package com.meta.community_be.chatbot.service;

import com.meta.community_be.ai.service.OpenAiService;
import com.meta.community_be.auth.domain.PrincipalDetails;
import com.meta.community_be.auth.domain.User;
import com.meta.community_be.chatbot.domain.ChatRoom;
import com.meta.community_be.chatbot.dto.ChatRoomRequestDto;
import com.meta.community_be.chatbot.dto.ChatRoomResponseDto;
import com.meta.community_be.chatbot.repository.ChatDialogRepository;
import com.meta.community_be.chatbot.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatbotService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatDialogRepository chatDialogRepository;
    private final OpenAiService openAiService;

    @Transactional
    public ChatRoomResponseDto createChatRoom(PrincipalDetails principalDetails, ChatRoomRequestDto chatRoomRequestDto) {
        User logginedUser = principalDetails.getUser();
        ChatRoom newRoom = new ChatRoom(logginedUser, chatRoomRequestDto);
        ChatRoom savedRoom = chatRoomRepository.save(newRoom);
        ChatRoomResponseDto chatRoomResponseDto = new ChatRoomResponseDto(savedRoom);
        return chatRoomResponseDto;
    }
}