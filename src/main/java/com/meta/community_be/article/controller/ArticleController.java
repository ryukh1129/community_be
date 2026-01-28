package com.meta.community_be.article.controller;

import com.meta.community_be.article.service.ArticleService;
import com.meta.community_be.article.dto.ArticleRequestDto;
import com.meta.community_be.article.dto.ArticleResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/boards/{boardId}/articles")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;

    @PostMapping()
    public ResponseEntity<ArticleResponseDto> createArticle(
            @RequestBody ArticleRequestDto articleRequestDto,
            @PathVariable Long boardId) {
        ArticleResponseDto articleResponseDto = articleService.createArticle(articleRequestDto, boardId);
        return ResponseEntity.status(HttpStatus.CREATED).body(articleResponseDto);
    }

    @GetMapping()
    public ResponseEntity<List<ArticleResponseDto>> getArticles() {
        List<ArticleResponseDto> articleResponseDtoList = articleService.getArticles();
        return ResponseEntity.ok(articleResponseDtoList);
    }

    @GetMapping("{id}")
    public ResponseEntity<ArticleResponseDto> getArticleById(
            @PathVariable Long id,
            @PathVariable Long boardId) {
        ArticleResponseDto articleResponseDto = articleService.getArticleById(id, boardId);
        return ResponseEntity.ok(articleResponseDto);
    }

    @PutMapping("{id}")
    public ResponseEntity<ArticleResponseDto> updateArticle(
            @PathVariable Long id,
            @RequestBody ArticleRequestDto articleRequestDto,
            @PathVariable Long boardId) {
        ArticleResponseDto articleResponseDto = articleService.updateArticle(id, articleRequestDto, boardId);
        return ResponseEntity.ok(articleResponseDto);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteArticle(
            @PathVariable Long id,
            @PathVariable Long boardId) {
        articleService.deleteArticle(id, boardId);
        return ResponseEntity.noContent().build();
    }
}