package com.meta.community_be.board.controller;

import com.meta.community_be.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/board")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

}
