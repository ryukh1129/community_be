package com.meta.community_be.ai.service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.audio.transcription.AudioTranscriptionResponse;
import org.springframework.ai.audio.tts.TextToSpeechPrompt;
import org.springframework.ai.audio.tts.TextToSpeechResponse;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.embedding.Embedding;
import org.springframework.ai.embedding.EmbeddingOptions;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.*;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

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

    // [2] 스트리밍 챗 모델 사용 - 응답 스트림 반환 테스트 // 챗봇 적용 예정
    public Flux<String> generateStream(String message) {
        // Message 객체 생성
        SystemMessage systemMessage = new SystemMessage("You are a helpful assistant who speaks Korean.");
        UserMessage userMessage = new UserMessage(message);
        AssistantMessage  assistantMessage = new AssistantMessage("");

        // Options 설정
        OpenAiChatOptions openAiChatOptions = OpenAiChatOptions.builder()
                .model(aiModel)
                .temperature(Double.valueOf(aiTemperature))
                .build();

        // Prompt 생성
        Prompt prompt = new Prompt(List.of(systemMessage, userMessage, assistantMessage));

        // Request & Response (Streaming)
        return openAiChatModel.stream(prompt)
                .mapNotNull(chatResponse -> chatResponse.getResult().getOutput().getText());
    }

    // [3] Embedding 모델 사용 예시
    public List<float[]> generateEmbeddings(List<String> texts, String model) {
        // Options 설정
        EmbeddingOptions embeddingOptions = EmbeddingOptions.builder()
                .model(model)
                .build();

        // Prompt 생성 및 호출
        EmbeddingRequest prompt = new EmbeddingRequest(texts, embeddingOptions);

        EmbeddingResponse embeddingResponse = openAiEmbeddingModel.call(prompt);
        return embeddingResponse.getResults().stream()
                .map(Embedding::getOutput)
                .toList();
    }

    // [4] Image 모델 사용 예시
    public List<String> generateImages(String text, int count, int height, int width) {
        // Options 설정
        OpenAiImageOptions imageOptions = OpenAiImageOptions.builder()
                .quality("hd")
                .N(count)
                .height(height)
                .width(width)
                .build();

        // Prompt 생성
        ImagePrompt imagePrompt = new ImagePrompt(text, imageOptions);

        // Request & Response
        ImageResponse imageResponse = openAiImageModel.call(imagePrompt);
        return imageResponse.getResults().stream()
                .map(image -> image.getOutput().getUrl())
                .toList();
    }

    // [5] TTS(Text To Speech) 모델 사용 예시
    public byte[] tts(String text) {
        // Options 설정
        OpenAiAudioSpeechOptions speechOptions = OpenAiAudioSpeechOptions.builder()
                .voice(OpenAiAudioApi.SpeechRequest.Voice.CORAL)
                .responseFormat(OpenAiAudioApi.SpeechRequest.AudioResponseFormat.MP3)
                .speed(1.0)
                .model(OpenAiAudioApi.TtsModel.TTS_1.value)
                .build();

        // Prompt 생성
        TextToSpeechPrompt prompt = new TextToSpeechPrompt(text, speechOptions);

        // Request & Response
        TextToSpeechResponse response = openAiAudioSpeechModel.call(prompt);
        return response.getResult().getOutput();
    }

    // [6] STT(Speech To Text) 모델 사용 예시
    public String stt(Resource audioFile) {
        // Options 설정
        OpenAiAudioApi.TranscriptResponseFormat responseFormat = OpenAiAudioApi.TranscriptResponseFormat.VTT;
        OpenAiAudioTranscriptionOptions transcriptionOptions = OpenAiAudioTranscriptionOptions.builder()
                .language("ko") // 오디오의 언어
                .prompt("This is a sample transcription.") // 음성 인식전 참고할 텍스트 프롬프트
                .temperature(0f)
                .model(OpenAiAudioApi.TtsModel.TTS_1.value)
                .responseFormat(responseFormat)
                .build();

        // Prompt 생성
        AudioTranscriptionPrompt prompt = new AudioTranscriptionPrompt(audioFile, transcriptionOptions);

        // Request & Response
        AudioTranscriptionResponse response = openAiAudioTranscriptionModel.call(prompt);
        return response.getResult().getOutput();
    }
}