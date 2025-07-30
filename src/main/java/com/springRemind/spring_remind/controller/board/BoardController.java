package com.springRemind.spring_remind.controller.board;

import com.springRemind.spring_remind.entity.board.Board;
import com.springRemind.spring_remind.entity.board.Post;
import com.springRemind.spring_remind.model.board.ParamsPost;
import com.springRemind.spring_remind.model.response.CommonResult;
import com.springRemind.spring_remind.model.response.ListResult;
import com.springRemind.spring_remind.model.response.SingleResult;
import com.springRemind.spring_remind.service.ResponseService;
import com.springRemind.spring_remind.service.board.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Tag(name = "게시판", description = "Board")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1/board")
public class BoardController {

    private final BoardService boardService;
    private final ResponseService responseService;

    @Operation(summary = "게시판 정보 조회", description = "게시판 정보를 조회한다.")
    @GetMapping(value = "/{boardName}")
    public SingleResult<Board> boardInfo(
            @Parameter(name = "boardName", description = "게시판 이름") @PathVariable("boardName") String boardName
    ) {
        return responseService.getSingleResult(boardService.findBoard(boardName));
    }

    @Operation(summary = "게시판 글 리스트", description = "게시판 게시글 리스트를 조회한다.")
    @GetMapping(value = "/{boardName}/posts")
    public ListResult<Post> posts(
            @Parameter(name = "boardName", description = "게시판 이름") @PathVariable("boardName") String boardName
    ) {
        return responseService.getListResult(boardService.findPosts(boardName));
    }

    @Parameter(name = "X-AUTH-TOKEN", description = "로그인 성공 후 access_token", required = true,
            in = ParameterIn.HEADER, schema = @Schema(implementation = String.class))
    @Operation(summary = "게시판 글 작성", description = "게시판에 글을 작성한다.")
    @PostMapping(value = "/{boardName}")
    public SingleResult<Post> post(
            @Parameter(name = "boardName", description = "게시판 이름") @PathVariable("boardName") String boardName,
            @ParameterObject @Valid @ModelAttribute ParamsPost post
            ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String uid = authentication.getName();
        return responseService.getSingleResult(boardService.writePost(uid, boardName, post));
    }

    @Operation(summary = "게시판 글 상세", description = "게시판에 글을 상세정보를 조회한다.")
    @GetMapping(value = "/post/{postId}")
    public SingleResult<Post> post(
            @Parameter(name = "postId", description = "게시글 Id") @PathVariable("postId") long postId
    ) {
        return responseService.getSingleResult(boardService.getPost(postId));
    }

    @Parameter(name = "X-AUTH-TOKEN", description = "로그인 성공 후 access_token", required = true,
            in = ParameterIn.HEADER, schema = @Schema(implementation = String.class))
    @Operation(summary = "게시판 글 수정", description = "게시판에 글을 수정한다.")
    @PutMapping(value = "/post/{postId}")
    public SingleResult<Post> post(
            @Parameter(name = "postId", description = "게시글 Id") @PathVariable("postId") long postId,
            @ParameterObject @Valid @ModelAttribute ParamsPost post
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String uid = authentication.getName();
        return responseService.getSingleResult(boardService.updatePost(postId, uid, post));
    }

    @Parameter(name = "X-AUTH-TOKEN", description = "로그인 성공 후 access_token", required = true,
            in = ParameterIn.HEADER, schema = @Schema(implementation = String.class))
    @Operation(summary = "게시판 글 삭제", description = "게시판의 글을 삭제한다.")
    @DeleteMapping(value = "/post/{postId}")
    public CommonResult deletepost(
            @Parameter(name = "postId", description = "게시글 Id") @PathVariable("postId") long postId
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String uid = authentication.getName();
        boardService.deletePost(postId, uid);
        return responseService.getSuccessResult();
    }
}
