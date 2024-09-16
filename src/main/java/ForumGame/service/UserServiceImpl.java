package ForumGame.service;

import ForumGame.domain.Role;
import ForumGame.domain.UserEntity;
import ForumGame.domain.UserStatus;
import ForumGame.exception.CustomException;
import ForumGame.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class UserServiceImpl  {
private final UserRepository userRepository;

    public void register(UserEntity user) {
        userRepository.findByLogin(user.getLogin()).ifPresent(t ->{
            throw new CustomException("User is already exist");
        });
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        log.debug("User registration({})", user.getLogin());
    }


    public UserEntity findByLogin(String login) {
        return userRepository.findByLogin(login)
                .orElseThrow(()->new CustomException("User not found"));
    }


    /**
     * Проверка пользователя на блокировку
     * @param userDetails - авторизованный пользователь
     * @return - текущий пользователь из БД
     */
    public UserEntity checkBlocked(UserDetails userDetails){
        String login = userDetails.getUsername();
        UserEntity userEntity = findByLogin(login);
        if (userEntity.getStatus() == UserStatus.BLOCKED){
            throw new CustomException("User blocked");
        }
        return userEntity;
    }

    public void changeRole(Role role, String login){
        UserEntity user = findByLogin(login);
        Role oldRole = user.getRoles();
        user.setRoles(role);
        userRepository.save(user);
        log.debug("The user's({}) role has been changed from({}) to({})", user.getLogin(), oldRole, user.getRoles());
    }

    public void blockingUser(String login){
        UserEntity user = findByLogin(login);
        user.setStatus(UserStatus.BLOCKED);
        userRepository.save(user);
        log.debug("User({}) is blocked", user.getLogin());
    }

    public void unblockingUser(String login){
        UserEntity user = findByLogin(login);
        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);
        log.debug("User({}) is unblocked", user.getLogin());
    }
}
