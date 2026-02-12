package com.meta.community_be.chatbot.repository;

import com.meta.community_be.chatbot.domain.ChatDialog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatDialogRepository extends JpaRepository<ChatDialog, Long> {
}