package com.meta.community_be.chatbot.domain;

import com.meta.community_be.auth.domain.User;
import com.meta.community_be.chatbot.dto.ChatRoomRequestDto;
import com.meta.community_be.common.domain.TimeStamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "chat_room")
public class ChatRoom extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    List<ChatDialog> chatDialogs = new ArrayList<>();

    public ChatRoom(User user, ChatRoomRequestDto chatRoomRequestDto) {
        this.user = user;
        this.title = chatRoomRequestDto.getTitle();
    }
}