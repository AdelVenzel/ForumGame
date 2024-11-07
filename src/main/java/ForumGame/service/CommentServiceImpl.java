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

import java.util.Arrays;
import java.util.List;

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
        Comment comment = commentRepository.findById(id).orElseThrow(() -> {
            throw new CustomException("comment not found");
        });
        comment.setContent(dto.getContent());
        commentRepository.save(comment);
        log.debug("comment update({})", comment.getContent());
    }

    @Override
    public void delete(int id) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> {
            throw new CustomException("comment not found");
        });
        commentRepository.delete(comment);
        log.debug("comment delete({})", comment.getContent());
    }

    public void deleteAll(List<Comment> comments) {
        commentRepository.deleteAll(comments);
    }

    /**
     * Проверяет принадлежит ли комментарий этому пользователю,
     * либо является ли пользователь ADMIN/MODERATOR
     *
     * @param userEntity - авторизированный пользователь
     * @param commentId  - id коммента
     */
    public void checkComment(UserEntity userEntity, int commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> {
            throw new CustomException("comment not found");
        });
        if (comment.getUserEntity().getId() != userEntity.getId()
                && (userEntity.getRoles() != Role.ADMIN && userEntity.getRoles() != Role.MODERATOR)) {
            throw new CustomException("This comment does not belong to the user ( " + userEntity.getLogin() + " ) or there are insufficient rights");
        }
    }

    /**
     * Проверка на то, существует такой объект или нет
     * Если существует, то меняем кол-во лайков и сохраняем в БД
     * Если не существует, то выбрасываем исключение
     *
     * @param commentId - id комментария
     * @param user      - авторизированный юзер
     */
    public void like(int commentId, UserEntity user) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> {
            throw new CustomException("Comment not found");
        });
        comment.setNumberLikes(comment.getNumberLikes() + 1);
        commentRepository.save(comment);
        log.debug("the user({}) has put a like, the current number of likes({})", user.getLogin(), comment.getNumberLikes());
    }

    public void checkContents(String comment) {
        log.debug("The beginning of the review of the comment for obscene language");
        String[] words = comment.split("\\s+");
        Arrays.stream(words)
                .filter(obsceneWordService::checkWord)
                .findAny()
                .ifPresent(t -> {
                            throw new CustomException("Obscene language");
                        }
                );
        log.debug("End of verification");
    }
}
