package ForumGame.service;

import ForumGame.domain.Comment;
import ForumGame.domain.Role;
import ForumGame.domain.UserEntity;
import ForumGame.dto.CommentDto;
import ForumGame.exception.CustomException;
import ForumGame.repository.CommentRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class CommentServiceImpl implements DatabaseWorker<Comment, CommentDto> {
    private final CommentRepository commentRepository;
    private final ObsceneWordService obsceneWordService;

    @Override
    public void add(Comment entity) {
        checkContents(entity.getContent());
        commentRepository.save(entity);
        log.debug("comment added({}), date of addition({})", entity.getContent(), entity.getDateCreation());
    }

    @Override
    public void update(CommentDto dto, int id) {
        Optional<Comment> entityOpt = commentRepository.findById(id);
        if (entityOpt.isPresent()) {
            Comment comment = entityOpt.get();
            comment.setContent(dto.getContent());
            commentRepository.save(comment);
            log.debug("comment update({})", comment.getContent());
        } else {
            log.debug("comment not found");
        }
    }

    @Override
    public void delete(int id) {
        Optional<Comment> entityOpt = commentRepository.findById(id);
        if (entityOpt.isPresent()) {
            Comment comment = entityOpt.get();
            commentRepository.delete(comment);
            log.debug("comment delete({})", comment.getContent());
        } else {
            log.debug("comment not found");
        }
    }

    /**
     * Проверяет принадлежит ли комментарий этому пользователю,
     * либо является ли пользователь ADMIN/MODERATOR
     *
     * @param userEntity - авторизированный пользователь
     * @param commentId  - id поста
     * @return принадлежит ли этот комментарий пользователю
     */
    public boolean checkComment(UserEntity userEntity, int commentId) {
        Optional<Comment> entityOpt = commentRepository.findById(commentId);
        if (entityOpt.isPresent()) {
            Comment comment = entityOpt.get();
            if (comment.getUserEntity().getId() == userEntity.getId()) {
                log.debug("The comment belongs to the user({})", userEntity.getLogin());
                return true;
            } else if (userEntity.getRoles() == Role.ADMIN || userEntity.getRoles() == Role.MODERATOR) {
                log.debug("A user with enhanced capabilities({})", userEntity.getLogin());
            }
        }
        log.debug("This comment does not belong to the user({}), or there are insufficient rights", userEntity.getLogin());
        return false;
    }

    /**
     * Проверяет возможность редактирования поста
     *
     * @param userEntity - текущий пользователь из БД
     * @param commentId  - id поста
     */
    public void checkEditUser(UserEntity userEntity, int commentId) {
        if (!checkComment(userEntity, commentId)) {
            throw new CustomException("Not enough rights to edit");
        }
    }

    /**
     * Находим комментарий по Id в типе Optional
     * Проверка на то, существует такой объект или нет
     * Если существует, то меняем кол-во лайков и сохраняем в БД
     * Если не существует, то выбрасываем исключение
     *
     * @param commentId - id комментария
     */
    public void like(int commentId, UserEntity user) {
        Optional<Comment> entityOpt = commentRepository.findById(commentId);
        if (entityOpt.isPresent()) {
            Comment comment = entityOpt.get();
            comment.setNumberLikes(comment.getNumberLikes() + 1);
            commentRepository.save(comment);
            log.debug("the user({}) has put a like, the current number of likes({})", user.getLogin(), comment.getNumberLikes());
        } else {
            throw new CustomException("Comment not found");
        }
    }

    public void checkContents(String comment) {
        log.debug("The beginning of the review of the comment for obscene language");
        String[] words = comment.split("\\s+");
        for (String word : words) {
            if (obsceneWordService.checkWord(word)) {
                throw new CustomException("Obscene language");
            }
        }
        log.debug("End of verification");
    }
}
