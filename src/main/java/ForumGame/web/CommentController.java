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

import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentServiceImpl commentService;
    private final UserServiceImpl userService;
    private final PostServiceImpl postService;

    @PostMapping("/comment/add")
    public String add(@AuthenticationPrincipal UserDetails userDetails, @RequestBody CommentDto dto, @RequestParam int postId) {
        UserEntity userEntity = userService.checkBlocked(userDetails);
        Optional<Post> postOpt = postService.findById(postId);
        if (postOpt.isEmpty()) {
            log.debug("Post not found");
            return "Post not found";
        }
        Post post = postOpt.get();
        Comment comment = CommentMapper.mapToEntity(dto, userEntity, post);
        commentService.add(comment);
        return "Add comment";
    }

    @PutMapping("/comment/update")
    public String update(@AuthenticationPrincipal UserDetails userDetails, @RequestBody CommentDto dto, @RequestParam int commentId) {
        UserEntity userEntity = userService.checkBlocked(userDetails);
        commentService.checkEditUser(userEntity, commentId);
        commentService.update(dto, commentId);
        return "update comment";
    }

    @DeleteMapping("/comment/delete")
    public String delete(@AuthenticationPrincipal UserDetails userDetails, @RequestParam int commentId) {
        UserEntity userEntity = userService.checkBlocked(userDetails);
        commentService.checkEditUser(userEntity, commentId);
        commentService.delete(commentId);
        return "delete comment";
    }
    @PutMapping("/comment/like")
    public String like(@AuthenticationPrincipal UserDetails userDetails, @RequestParam int commentId) {
        UserEntity userEntity = userService.checkBlocked(userDetails);
        commentService.like(commentId,userEntity);
        return "Like";
    }
}
