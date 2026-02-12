package com.meta.community_be.chatbot.controller;

import com.meta.community_be.auth.domain.PrincipalDetails;
import com.meta.community_be.chatbot.dto.ChatRequestDto;
import com.meta.community_be.chatbot.dto.ChatResponseDto;
import com.meta.community_be.chatbot.dto.ChatRoomRequestDto;
import com.meta.community_be.chatbot.dto.ChatRoomResponseDto;
import com.meta.community_be.chatbot.service.ChatbotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/chatbot")
@RequiredArgsConstructor
public class ChatbotController {
    private final ChatbotService chatbotService;

    @PostMapping("/rooms")
    public ResponseEntity<ChatRoomResponseDto> createRoom(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody ChatRoomRequestDto chatRoomRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(chatbotService.createChatRoom(principalDetails, chatRoomRequestDto));
    }

    @PostMapping("/rooms/{roomId}/messages")
    public ResponseEntity<ChatResponseDto> postMessage(
            @PathVariable Long roomId,
            @RequestBody ChatRequestDto chatRequestDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        ChatResponseDto chatResponseDto = chatbotService.processMessage(principalDetails, roomId, chatRequestDto);
        return ResponseEntity.ok(chatResponseDto);
    }
}