package com.meta.community_be.article.service;

import com.meta.community_be.article.domain.Article;
import com.meta.community_be.article.dto.ArticleRequestDto;
import com.meta.community_be.article.dto.ArticleResponseDto;
import com.meta.community_be.article.repository.ArticleRepository;
import com.meta.community_be.board.domain.Board;
import com.meta.community_be.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public ArticleResponseDto createArticle(ArticleRequestDto articleRequestDto, Long boardId) {
        // 해당 id의 게시판이 존재하는지 확인
        Board foundBoard = getValidBoardById(boardId);
        // RequestDto -> Entity 변환
        Article newArticle = new Article(articleRequestDto, foundBoard);
        Article savedArticle = articleRepository.save(newArticle);
        // Entity -> ResponseDto 변환
        ArticleResponseDto articleResponseDto = new ArticleResponseDto(savedArticle);
        return articleResponseDto;
    }

    @Transactional(readOnly = true)
    public List<ArticleResponseDto> getArticles(Long boardId) {
        List<ArticleResponseDto> articleResponseDtoList = articleRepository.findAllByBoardIdOrderByCreatedAtDesc(boardId).stream()
                .map(ArticleResponseDto::new).toList();
        return articleResponseDtoList;
    }

    @Transactional(readOnly = true)
    public ArticleResponseDto getArticleById(Long id, Long boardId) {
        Article foundarticle = getValidBoardAndArticleById(id, boardId);
        ArticleResponseDto articleResponseDto = new ArticleResponseDto(foundarticle);
        return articleResponseDto;
    }

    @Transactional
    public ArticleResponseDto updateArticle(Long id, ArticleRequestDto articleRequestDto, Long boardId) {
        // 해당 id의 게시글이 존재하는지 확인
        Article foundArticle = getValidBoardAndArticleById(id, boardId);
        // 게시글 내용 수정
        foundArticle.update(articleRequestDto);
        return new ArticleResponseDto(foundArticle);
    }

    @Transactional
    public void deleteArticle(Long id, Long boardId) {
        // 해당 id의 게시판과 게시글이 존재하는지 확인
        Article foundArticle = getValidBoardAndArticleById(id, boardId);
        // 게시글 내용 삭제
        articleRepository.delete(foundArticle);
    }

    // id로 게시판과 게시글 찾는 헬퍼 메서드
    public Article getValidBoardAndArticleById(Long id, Long boardId) {
        return articleRepository.findByIdAndBoardId(id, boardId).orElseThrow(() ->
                new IllegalArgumentException("선택한 id의 게시글과 boardId의 게시판은 존재하지 않습니다."));
    }

    public Board getValidBoardById(Long id) {
        return boardRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 id의 게시판은 존재하지 않습니다."));
    }
}