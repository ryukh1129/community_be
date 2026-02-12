package com.meta.community_be.chatbot.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SenderType {
    USER,
    SYSTEM
}