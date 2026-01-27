package com.meta.community_be.board.repository;

import com.meta.community_be.board.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
