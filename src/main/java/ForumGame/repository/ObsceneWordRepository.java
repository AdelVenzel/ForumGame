package ForumGame.repository;

import ForumGame.domain.ObsceneWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ObsceneWordRepository extends JpaRepository<ObsceneWord,String> {
}
