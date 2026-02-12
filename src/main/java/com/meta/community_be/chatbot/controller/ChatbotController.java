package com.meta.community_be.chatbot.controller;

import com.meta.community_be.auth.domain.PrincipalDetails;
import com.meta.community_be.chatbot.dto.*;
import com.meta.community_be.chatbot.service.ChatbotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/chatbot/rooms")
@RequiredArgsConstructor
public class ChatbotController {
    private final ChatbotService chatbotService;

    @PostMapping()
    public ResponseEntity<ChatRoomResponseDto> createRoom(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody ChatRoomRequestDto chatRoomRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(chatbotService.createChatRoom(principalDetails, chatRoomRequestDto));
    }

    @PostMapping("/{roomId}/messages")
    public ResponseEntity<ChatResponseDto> postMessage(
            @PathVariable Long roomId,
            @RequestBody ChatRequestDto chatRequestDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        ChatResponseDto chatResponseDto = chatbotService.processMessage(principalDetails, roomId, chatRequestDto);
        return ResponseEntity.ok(chatResponseDto);
    }

    @GetMapping()
    public ResponseEntity<List<ChatRoomResponseDto>> getRoomByUser(
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        List<ChatRoomResponseDto> chatRoomResponseDtoList = chatbotService.getRoomsByUser(principalDetails);
        return ResponseEntity.ok(chatRoomResponseDtoList);
    }

    @GetMapping("{roomId}/messages")
    public ResponseEntity<List<ChatDialogResponseDto>> getChatDialogs(
            @PathVariable Long roomId,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        List<ChatDialogResponseDto> chatDialogResponseDtoList = chatbotService.getChatDialogById(roomId, principalDetails);
        return ResponseEntity.ok(chatDialogResponseDtoList);
    }
}