package ForumGame.repository;

import ForumGame.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    @Query("select p from Post p where p.dateCreation >= :startDate and p.dateCreation < :endDate")
    List<Post> findByDate(@Param("startDate")LocalDateTime startDate, @Param("endDate")LocalDateTime endDate);
}
