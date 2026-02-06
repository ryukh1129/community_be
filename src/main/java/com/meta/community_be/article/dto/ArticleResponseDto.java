package com.meta.community_be.article.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meta.community_be.article.domain.Article;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ArticleResponseDto {
    private Long id;
    private String title;
    private String contents;
    private String authorUsername;
    private String authorNickname;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifiedAt;

    public ArticleResponseDto(Article article) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.contents = article.getContents();
        this.createdAt = article.getCreatedAt();
        this.modifiedAt = article.getModifiedAt();

        if (article.getUser() != null) {
            this.authorUsername = article.getUser().getUsername();
            this.authorNickname = article.getUser().getNickname();
        }
    }
}