package com.meta.community_be.board.service;

import com.meta.community_be.board.domain.Board;
import com.meta.community_be.board.dto.BoardRequestDto;
import com.meta.community_be.board.dto.BoardResponseDto;
import com.meta.community_be.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    @Transactional
    public BoardResponseDto createBoard(@RequestBody BoardRequestDto boardRequestDto) {
        // RequestDto -> Entity 변환
        Board newBoard = new Board(boardRequestDto);
        Board savedBoard = boardRepository.save(newBoard);
        // Entity -> ResponseDto 변환
        BoardResponseDto boardResponseDto = new BoardResponseDto(savedBoard);
        return boardResponseDto;
    }

    @Transactional(readOnly = true)
    public List<BoardResponseDto> getBoards() {
        List<BoardResponseDto> boardResponseDtoList = boardRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(BoardResponseDto::new).toList();
        return boardResponseDtoList;
    }

    @Transactional(readOnly = true)
    public BoardResponseDto getBoardById(Long id) {
        Board foundboard = findBoardById(id);
        BoardResponseDto boardResponseDto = new BoardResponseDto(foundboard);
        return boardResponseDto;
    }

    @Transactional
    public BoardResponseDto updateBoard(@PathVariable Long id, @RequestBody BoardRequestDto boardRequestDto) {
        // 해당 id의 게시판이 존재하는지 확인
        Board foundBoard = findBoardById(id);
        // 게시판 내용 수정
        foundBoard.update(boardRequestDto);
        return new BoardResponseDto(foundBoard);
    }

    @Transactional
    public void deleteBoard(@PathVariable Long id) {
        // 해당 id의 게시판이 존재하는지 확인
        Board foundBoard = findBoardById(id);
        // 게시판 내용 삭제
        boardRepository.delete(foundBoard);
    }

    // id로 게시판 찾는 헬퍼 메서드
    public Board findBoardById(Long id) {
        return boardRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 id의 게시판은 존재하지 않습니다."));
    }
}