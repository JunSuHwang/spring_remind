package com.springRemind.spring_remind.service.board;

import com.springRemind.spring_remind.advice.exception.CNotOwnerException;
import com.springRemind.spring_remind.advice.exception.CResourceNotExistException;
import com.springRemind.spring_remind.advice.exception.CUserNotFoundException;
import com.springRemind.spring_remind.entity.User;
import com.springRemind.spring_remind.entity.board.Board;
import com.springRemind.spring_remind.entity.board.Post;
import com.springRemind.spring_remind.model.board.ParamsPost;
import com.springRemind.spring_remind.repo.UserJpaRepo;
import com.springRemind.spring_remind.repo.board.BoardJpaRepo;
import com.springRemind.spring_remind.repo.board.PostJpaRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

    private final BoardJpaRepo boardJpaRepo;
    private final PostJpaRepo postJpaRepo;
    private final UserJpaRepo userJpaRepo;

    public Board findBoard(String boardName) {
return Optional.ofNullable(boardJpaRepo.findByName(boardName)).orElseThrow(CResourceNotExistException::new);
    }
    public List<Post> findPosts(String boardName) {
        return postJpaRepo.findByBoard(findBoard(boardName));
    }
    public Post getPost(long postId) {
        return postJpaRepo.findById(postId).orElseThrow(CResourceNotExistException::new);
    }
    public Post writePost(String uid, String boardName, ParamsPost paramsPost) {
        Board board = findBoard(boardName);
        Post post = new Post(userJpaRepo.findByUid(uid).orElseThrow(CUserNotFoundException::new),
                board, paramsPost.getAuthor(), paramsPost.getTitle(), paramsPost.getContent());
        return postJpaRepo.save(post);
    }
    public Post updatePost(long postId, String uid, ParamsPost paramsPost) {
        Post post = getPost(postId);
        User user = post.getUser();
        if (!uid.equals(user.getUid()))
            throw new CNotOwnerException();
        post.setUpdate(paramsPost.getAuthor(), paramsPost.getTitle(), paramsPost.getContent());
        return post;
    }
    public boolean deletePost(long postId, String uid) {
        Post post = getPost(postId);
        User user = post.getUser();
        if (!uid.equals(user.getUid()))
            throw new CNotOwnerException();
        postJpaRepo.delete(post);
        return true;
    }
}
