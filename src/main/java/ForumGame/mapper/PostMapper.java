package ForumGame.mapper;

import ForumGame.domain.Post;
import ForumGame.domain.UserEntity;
import ForumGame.dto.PostDto;

import java.time.LocalDateTime;

public class PostMapper {
    /**
     * Конвертация из DTO в сущность БД
     * @param dto - объект с фронта
     * @param user - текуший авторизированный пользователь
     * @return сущность поста
     */
    public static Post mapToEntity(PostDto dto, UserEntity user) {
        Post post = new Post();
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setDateCreation(LocalDateTime.now());
        post.setUserEntity(user);
        return post;
    }

    /**
     * Конвертация из сущности БД в DTO
     * @param post сущность
     * @return объект DTO
     */
    public static PostDto mapToDto(Post post){
        return new PostDto(post.getTitle(), post.getContent(), post.getDateCreation());
    }
}
