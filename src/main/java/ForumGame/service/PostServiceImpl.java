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
import java.util.Optional;

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
        Optional<Post> entityOpt = postRepository.findById(id);
        if (entityOpt.isPresent()) {
            Post post = entityOpt.get();
            post.setTitle(dto.getTitle());
            post.setContent(dto.getContent());
            postRepository.save(post);
            log.debug("post update({})", post.getTitle());
        }else{
            log.debug("post not found");
        }
    }

    @Override
    public void delete(int id) {
        Optional<Post> entityOpt = postRepository.findById(id);
        if (entityOpt.isPresent()){
            Post post = entityOpt.get();
            List<Comment> comments = post.getComments();
            commentService.deleteAll(comments);
            postRepository.deleteById(id);
            log.debug("post delete({})", post.getTitle());
        }else{
            log.debug("post not found");
        }
    }

    /**
     * Проверяет принадлежит ли пост этому пользователю,
     * либо является ли пользователь ADMIN/MODERATOR
     *
     * @param userEntity - авторизированный пользователь
     * @param postId     - id поста
     * @return принадлежит ли этот пост пользователю
     */
    public boolean checkPost(UserEntity userEntity, int postId) {
        Optional<Post> entityOpt = postRepository.findById(postId);
        if (entityOpt.isPresent()) {
            Post post = entityOpt.get();
            log.debug("The post belongs to the user({})", userEntity.getLogin());
            if (post.getUserEntity().getId() == userEntity.getId()) {
                return true;
            } else if(userEntity.getRoles() == Role.ADMIN || userEntity.getRoles() == Role.MODERATOR){
                log.debug("A user with enhanced capabilities({})", userEntity.getLogin());
            }
        }
        log.debug("This post does not belong to the user({}), or there are insufficient rights", userEntity.getLogin());
        return false;
    }

    public Optional<Post> findById(int postId) {
        return postRepository.findById(postId);
    }


    public List<Post> getAllByDate(LocalDateTime startDate) {
        log.debug("Search for posts for a specific date({})", startDate);
        LocalDateTime endDate = startDate.plusDays(1);
        return postRepository.findByDate(startDate, endDate);
    }

    /**
     * Проверяет возможность редактирования поста
     * @param userEntity - текущий пользователь из БД
     * @param postId     - id поста
     */
    public void checkEditUser(UserEntity userEntity, int postId) {
        if (!checkPost(userEntity, postId)) {
            throw new CustomException("Not enough rights to edit");
        }
    }
}
