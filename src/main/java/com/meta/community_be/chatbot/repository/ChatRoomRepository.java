package com.meta.community_be.chatbot.repository;

import com.meta.community_be.auth.domain.User;
import com.meta.community_be.chatbot.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    List<ChatRoom> findAllByUserOrderByCreatedAtDesc(User user);
}