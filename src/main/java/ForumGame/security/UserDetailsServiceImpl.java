package ForumGame.security;

import ForumGame.domain.UserEntity;
import ForumGame.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserServiceImpl userService;

    /**
     * UserDetails текущий авторизированный пользователь
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userService.findByLogin(username);

        return User.builder()
                .username(userEntity.getLogin())
                .password(userEntity.getPassword())
                .roles(userEntity.getRoles().toString())
                .build();

    }
}
