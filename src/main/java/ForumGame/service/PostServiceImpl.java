package ForumGame.service;

import ForumGame.domain.Comment;
import ForumGame.domain.Post;
import ForumGame.domain.Role;
import ForumGame.domain.UserEntity;
import ForumGame.dto.PostDto;
import ForumGame.exception.CustomException;
import ForumGame.repository.PostRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class PostServiceImpl implements DatabaseWorker<Post, PostDto> {
    private final PostRepository postRepository;
    private final CommentServiceImpl commentService;

    @Override
    public void add(Post entity) {
        postRepository.save(entity);
        log.debug("post added({}), date of addition({})", entity.getTitle(), entity.getDateCreation());
    }

    @Override
    public void update(PostDto dto, int id) {
        Post post = postRepository.findById(id).orElseThrow(() -> {
            throw new CustomException("post not found");
        });
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        postRepository.save(post);
        log.debug("post update({})", post.getTitle());
    }

    @Override
    public void delete(int id) {
        Post post = postRepository.findById(id).orElseThrow(() -> {
            throw new CustomException("post not found");
        });
        List<Comment> comments = post.getComments();
        commentService.deleteAll(comments);
        postRepository.deleteById(id);
        log.debug("post delete({})", post.getTitle());
    }

    /**
     * Проверяет принадлежит ли пост этому пользователю,
     * либо является ли пользователь ADMIN/MODERATOR
     *
     * @param userEntity - авторизированный пользователь
     * @param postId     - id поста
     */
    public void checkPost(UserEntity userEntity, int postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> {
            throw new CustomException("Post not found");
        });
        if (post.getUserEntity().getId() != userEntity.getId()
                && (userEntity.getRoles() != Role.ADMIN && userEntity.getRoles() != Role.MODERATOR)) {
            throw new CustomException("A user with disabilities ( " + userEntity.getLogin() + " )");
        }
    }

    public Post findById(int postId) {
        return postRepository.findById(postId).orElseThrow(() -> {
            throw new CustomException("Post not found");
        });
    }

    public List<Post> getAllByDate(LocalDateTime startDate) {
        log.debug("Search for posts for a specific date({})", startDate);
        LocalDateTime endDate = startDate.plusDays(1);
        return postRepository.findByDate(startDate, endDate);
    }
}
