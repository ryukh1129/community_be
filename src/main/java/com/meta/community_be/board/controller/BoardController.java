package com.meta.community_be.board.controller;

import com.meta.community_be.board.dto.BoardRequestDto;
import com.meta.community_be.board.dto.BoardResponseDto;
import com.meta.community_be.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/boards")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @PostMapping()
    public ResponseEntity<BoardResponseDto> createBoard(@RequestBody BoardRequestDto boardRequestDto) {
        BoardResponseDto boardResponseDto = boardService.createBoard(boardRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(boardResponseDto);
    }

    @GetMapping()
    public ResponseEntity<List<BoardResponseDto>> getBoards() {
        List<BoardResponseDto> boardResponseDtoList = boardService.getBoards();
        return ResponseEntity.ok(boardResponseDtoList);
    }

    @GetMapping("{id}")
    public ResponseEntity<BoardResponseDto> getBoardById(@PathVariable Long id) {
        BoardResponseDto boardResponseDto = boardService.getBoardById(id);
        return ResponseEntity.ok(boardResponseDto);
    }

    @PutMapping("{id}")
    public ResponseEntity<BoardResponseDto> updateBoard(@PathVariable Long id, @RequestBody BoardRequestDto boardRequestDto) {
        BoardResponseDto boardResponseDto = boardService.updateBoard(id, boardRequestDto);
        return ResponseEntity.ok(boardResponseDto);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteBoard(@PathVariable Long id) {
        boardService.deleteBoard(id);
        return ResponseEntity.noContent().build();
    }
}