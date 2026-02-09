package com.meta.community_be.article.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meta.community_be.article.domain.Article;
import com.meta.community_be.comment.dto.CommentResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class ArticleResponseDto {
    private Long id;
    private String title;
    private String contents;
    private String authorUsername;
    private String authorNickname;
    private boolean liked;
    private int likesCount;
    private String boardTitle;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifiedAt;

    private List<CommentResponseDto> comments;

    public ArticleResponseDto(Article article, int likesCount, boolean liked) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.contents = article.getContents();
        this.createdAt = article.getCreatedAt();
        this.modifiedAt = article.getModifiedAt();

        if (article.getUser() != null) {
            this.authorUsername = article.getUser().getUsername();
            this.authorNickname = article.getUser().getNickname();
        }

        if (article.getComments() != null) {
            this.comments = article.getComments().stream().map(CommentResponseDto::new).toList();
        } else {
            this.comments = List.of();
        }

        this.likesCount = likesCount;
        this.liked = liked;
    }

    public ArticleResponseDto(Article article) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.contents = article.getContents();
        this.createdAt = article.getCreatedAt();
        this.modifiedAt = article.getModifiedAt();

        if (article.getBoard() != null) {
            this.boardTitle = article.getBoard().getTitle();
        }

        if (article.getUser() != null) {
            this.authorUsername = article.getUser().getUsername();
            this.authorNickname = article.getUser().getNickname();
        }

        this.likesCount = 0;
        this.liked = false;

        this.comments = List.of(); // 목록 조회 시에는 comments는 제외 (N+1 방지)
    }
}