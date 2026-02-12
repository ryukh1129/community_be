package com.meta.community_be.ai.controller;

import com.meta.community_be.ai.service.OpenAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("api/chat")
@RequiredArgsConstructor
public class OpenAiController {
    private final OpenAiService openAiService;

    @PostMapping("/chat")
    public String chat(@RequestBody Map<String, String>body) {
        return openAiService.generate(body.get("text"));
    }
}
