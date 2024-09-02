package ForumGame.mapper;

import ForumGame.domain.Comment;
import ForumGame.domain.Post;
import ForumGame.domain.UserEntity;
import ForumGame.dto.CommentDto;

import java.time.LocalDateTime;

public class CommentMapper {
    public static Comment mapToEntity(CommentDto dto, UserEntity user, Post post){
        Comment comment = new Comment();
        comment.setContent(dto.getContent());
        comment.setNumberLikes(0);
        comment.setDateCreation(LocalDateTime.now());
        comment.setUserEntity(user);
        comment.setPost(post);
        return comment;
    }
}
