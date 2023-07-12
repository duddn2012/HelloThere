package com.example.hello_there.comment;

import com.example.hello_there.comment.dto.*;
import com.example.hello_there.exception.BaseException;
import com.example.hello_there.exception.BaseResponse;
import com.example.hello_there.login.jwt.JwtService;
import com.example.hello_there.user.UserRepository;
import com.example.hello_there.utils.UtilService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/boards/{boardId}/comments")
public class CommentController {

    private final CommentService commentService;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final UtilService utilService;
    private final JwtService jwtService;

    /** 댓글 생성 **/
    @PostMapping
    public BaseResponse<PostCommentRes> addComment(
            @RequestBody @Valid PostCommentReq postCommentReq,
            @PathVariable Long boardId) {
        try{
            Long userId = jwtService.getUserIdx();
            return new BaseResponse<>(commentService.addComment(boardId, userId, postCommentReq));
        }
        catch(BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /** 댓글 전체 조회 **/
    @GetMapping()
    public BaseResponse<Page<GetCommentRes>> commentList(
            @PathVariable(name = "boardId") Long boardId,
            Pageable pageable) {
        try{
            return new BaseResponse<>(commentService.findComments(boardId,pageable));
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}