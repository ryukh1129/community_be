package com.meta.community_be.ai.service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OpenAiService {
    // SpringAI 제공하는 모델 구현체
    private final OpenAiChatModel openAiChatModel;
    private final OpenAiEmbeddingModel openAiEmbeddingModel;
    private final OpenAiImageModel openAiImageModel;
    private final OpenAiAudioSpeechModel openAiAudioSpeechModel;
    private final OpenAiAudioTranscriptionModel openAiAudioTranscriptionModel;

    // 대화 기록 포함하여 AI 응답을 생성하기 위한 인터페이스 직접 사용
    private final ChatModel chatModel;

    // 모델 선택 및 AI temperature 설정은 application.properties에서 관리
    @Value("${spring.ai.openai.chat.options.model}")
    private String aiModel;
    @Value("${spring.ai.openai.chat.options.temperature")
    private String aiTemperature;

    // [1] 기본 챗 모델 사용 - 응답 반환 테스트
    public String generate(String message) {
        // Message 객체 생성
        SystemMessage systemMessage = new SystemMessage("You are a helpful assistant who speaks Korean.");
        UserMessage userMessage = new UserMessage(message);
        AssistantMessage assistantMessage = new AssistantMessage("");

        // Options 설정
        OpenAiChatOptions.builder()
                .model(aiModel)
                .temperature(Double.valueOf(aiTemperature))
                .build();

        // Prompt 생성
        Prompt prompt = new Prompt(List.of(systemMessage, userMessage, assistantMessage));

        // Request & Response
        ChatResponse chatResponse = openAiChatModel.call(prompt);
        return chatResponse.getResult().getOutput().getText();
    }
}