package ForumGame.web;

import ForumGame.domain.Comment;
import ForumGame.domain.Post;
import ForumGame.domain.UserEntity;
import ForumGame.dto.CommentDto;
import ForumGame.mapper.CommentMapper;
import ForumGame.service.CommentServiceImpl;
import ForumGame.service.PostServiceImpl;
import ForumGame.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentServiceImpl commentService;
    private final UserServiceImpl userService;
    private final PostServiceImpl postService;

    @PostMapping("/v1/comments")
    public void add(@AuthenticationPrincipal UserDetails userDetails, @RequestBody CommentDto dto, @RequestParam int postId) {
        UserEntity userEntity = userService.checkBlocked(userDetails);
        Post post = postService.findById(postId);
        Comment comment = CommentMapper.mapToEntity(dto, userEntity, post);
        commentService.add(comment);
    }

    @PutMapping("/v1/comments")
    public void update(@AuthenticationPrincipal UserDetails userDetails, @RequestBody CommentDto dto, @RequestParam int commentId) {
        UserEntity userEntity = userService.checkBlocked(userDetails);
        commentService.checkComment(userEntity, commentId);
        commentService.update(dto, commentId);
    }

    @DeleteMapping("/v1/comments")
    public void delete(@AuthenticationPrincipal UserDetails userDetails, @RequestParam int commentId) {
        UserEntity userEntity = userService.checkBlocked(userDetails);
        commentService.checkComment(userEntity, commentId);
        commentService.delete(commentId);
    }

    @PutMapping("/v1/comments/like")
    public void like(@AuthenticationPrincipal UserDetails userDetails, @RequestParam int commentId) {
        UserEntity userEntity = userService.checkBlocked(userDetails);
        commentService.like(commentId, userEntity);
    }
}
