package com.meta.community_be.article.controller;

import com.meta.community_be.article.dto.ArticleRequestDto;
import com.meta.community_be.article.dto.ArticleResponseDto;
import com.meta.community_be.article.service.ArticleService;
import com.meta.community_be.auth.domain.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/boards/{boardId}/articles")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;

    @PostMapping()
    public ResponseEntity<ArticleResponseDto> createArticle(
            @RequestBody ArticleRequestDto articleRequestDto,
            @PathVariable Long boardId,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        ArticleResponseDto articleResponseDto = articleService.createArticle(articleRequestDto, boardId, principalDetails);
        return ResponseEntity.status(HttpStatus.CREATED).body(articleResponseDto);
    }

    @GetMapping()
    public ResponseEntity<Page<ArticleResponseDto>> getArticles(
            @PathVariable Long boardId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) { // 기본값 0페이지 , 기본값 10개씩
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<ArticleResponseDto> articleResponseDtoPaginationList = articleService.getArticles(boardId, pageable);
        return ResponseEntity.ok(articleResponseDtoPaginationList);
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