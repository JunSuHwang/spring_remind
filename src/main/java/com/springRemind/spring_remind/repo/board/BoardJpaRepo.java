package com.springRemind.spring_remind.repo.board;

import com.springRemind.spring_remind.entity.board.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardJpaRepo extends JpaRepository<Board, Long> {
    Board findByName(String name);
}
