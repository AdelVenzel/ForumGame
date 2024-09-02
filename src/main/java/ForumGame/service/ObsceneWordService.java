package ForumGame.service;

import ForumGame.domain.ObsceneWord;
import ForumGame.repository.ObsceneWordRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
        Optional<ObsceneWord> entityOpt = obsceneWordRepository.findById(word);
        if (entityOpt.isPresent()) {
            log.debug("Obscene language({})", word);
            return true;
        } else {
            log.debug("the word is correct({})", word);
        }
        return false;
    }
}
