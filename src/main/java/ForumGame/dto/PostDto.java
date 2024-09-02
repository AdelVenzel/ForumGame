package ForumGame.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PostDto {
    private String title;
    private String content;
    private LocalDateTime dateCreation;
}
