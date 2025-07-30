package com.springRemind.spring_remind.repo.board;

import com.springRemind.spring_remind.entity.board.Board;
import com.springRemind.spring_remind.entity.board.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PostJpaRepo extends JpaRepository<Post, Long> {
    List<Post> findByBoard(Board board);
}
