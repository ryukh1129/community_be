package com.meta.community_be.chatbot.service;

import com.meta.community_be.ai.service.OpenAiService;
import com.meta.community_be.auth.domain.PrincipalDetails;
import com.meta.community_be.auth.domain.User;
import com.meta.community_be.chatbot.domain.ChatDialog;
import com.meta.community_be.chatbot.domain.ChatRoom;
import com.meta.community_be.chatbot.domain.SenderType;
import com.meta.community_be.chatbot.dto.ChatRequestDto;
import com.meta.community_be.chatbot.dto.ChatResponseDto;
import com.meta.community_be.chatbot.dto.ChatRoomRequestDto;
import com.meta.community_be.chatbot.dto.ChatRoomResponseDto;
import com.meta.community_be.chatbot.repository.ChatDialogRepository;
import com.meta.community_be.chatbot.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

    @Transactional
    public ChatResponseDto processMessage(PrincipalDetails principalDetails, Long roomId, ChatRequestDto chatRequestDto) {
        ChatRoom foundRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다. ID: " + roomId));
        if (!foundRoom.getUser().getId().equals(principalDetails.getUser().getId())) {
            throw new IllegalArgumentException("해당 채팅방에 접근할 권한이 없습니다.");
        }

        // 1. (DB 저장) 사용자 메시지 저장
        saveDialog(foundRoom, SenderType.USER, chatRequestDto.getMessage());

        // 2. (대화 맥락 구조 생성) 해당 채팅방의 대화 기록 조회
        List<Message> history = foundRoom.getChatDialogs().stream()
                .map(dialog -> dialog.getSenderType() == SenderType.USER
                        ? (Message) new UserMessage(dialog.getContents())
                        : (Message) new AssistantMessage(dialog.getContents()))
                .collect(Collectors.toList());

        // 3. (AI 응답) OpenAI API 호출
        String apiResponseMessage = openAiService.generateWithHistory(history);

        // 4. (DB 저장) AI 응답 저장
        saveDialog(foundRoom, SenderType.SYSTEM, apiResponseMessage);

        return new ChatResponseDto(apiResponseMessage);
    }

    private void saveDialog(ChatRoom chatRoom, SenderType senderType, String contents) {
        chatDialogRepository.save(new ChatDialog(chatRoom, senderType, contents));
    }
}