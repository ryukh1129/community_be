package com.meta.community_be.article.domain;

import com.meta.community_be.article.dto.ArticleRequestDto;
import com.meta.community_be.auth.domain.User;
import com.meta.community_be.board.domain.Board;
import com.meta.community_be.common.domain.TimeStamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "article")
public class Article extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Article(ArticleRequestDto articleRequestDto, Board board, User user) {
        this.title = articleRequestDto.getTitle();
        this.contents = articleRequestDto.getContents();
        this.board = board;
        this.user = user;
    }

    public void update(ArticleRequestDto articleRequestDto) {
        this.title = articleRequestDto.getTitle();
        this.contents = articleRequestDto.getContents();
    }
}