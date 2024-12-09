package ForumGame.web;
import ForumGame.domain.Post;
import ForumGame.domain.UserEntity;
import ForumGame.dto.PostDto;
import ForumGame.mapper.PostMapper;
import ForumGame.service.PostServiceImpl;
import ForumGame.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostServiceImpl postService;
    private final UserServiceImpl userService;

    @PostMapping("/v1/posts")
    public void add(@AuthenticationPrincipal UserDetails userDetails, @RequestBody PostDto dto) {
        UserEntity userEntity = userService.checkBlocked(userDetails);
        Post post = PostMapper.mapToEntity(dto, userEntity);
        postService.add(post);
    }

    @PutMapping("/v1/posts")
    public void update(@AuthenticationPrincipal UserDetails userDetails, @RequestBody PostDto dto, @RequestParam int postId) {
        UserEntity userEntity = userService.checkBlocked(userDetails);
        postService.checkPost(userEntity, postId);
        postService.update(dto, postId);
    }

    @DeleteMapping("/v1/posts")
    public void delete(@AuthenticationPrincipal UserDetails userDetails, @RequestParam int postId) {
        UserEntity userEntity = userService.checkBlocked(userDetails);
        postService.checkPost(userEntity, postId);
        postService.delete(postId);
    }

    /**
     * Идет проверка пользователя на блокировку
     * Находим объект нужного пользователя по логину
     * Получение списка всех постов найденного пользователя
     * Список постов конвертируем в список DTO постов
     *
     * @param userDetails текущий авторизированный пользователь
     * @param login       логин искомого пользователя
     * @return список DTO постов
     */
    @PostMapping("/v1/posts/posts-by-login")
    public List<PostDto> getAllByLogin(@AuthenticationPrincipal UserDetails userDetails, @RequestParam String login) {
        userService.checkBlocked(userDetails);
        UserEntity user = userService.findByLogin(login);
        List<Post> posts = user.getPosts();
        return posts.stream()
                .map(PostMapper::mapToDto)
                .collect(Collectors.toList());
    }

    /**
     * @param userDetails - текущий авторизированный пользователь
     * @param date        - дата, по которой ищем погсты
     * @return - список постов DTO
     * @DateTimeFormat(2024-07-17T00:00 - необходимый формат для постмэна) - аннотиация, которая преобразовывает параметр из запроса в дата время
     * так как программа не понимает как обрабатывать тип данных LocalDateTime date
     */
    @PostMapping("/v1/posts/posts-by-date")
    public List<PostDto> getAllByDate(@AuthenticationPrincipal UserDetails userDetails, @RequestParam
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        userService.checkBlocked(userDetails);
        List<Post> posts = postService.getAllByDate(date);
        return posts.stream()
                .map(PostMapper::mapToDto)
                .collect(Collectors.toList());
    }
}
