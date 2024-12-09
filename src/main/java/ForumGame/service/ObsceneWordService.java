package ForumGame.service;

import ForumGame.repository.ObsceneWordRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class ObsceneWordService {
    private ObsceneWordRepository obsceneWordRepository;

    /**
     * Проверяет, есть ли такое слово в БД нецензурных слов
     *
     * @return возвращает true если слово найдено;
     */
    public boolean checkWord(String word) {
        return obsceneWordRepository.existsById(word);
    }
}
