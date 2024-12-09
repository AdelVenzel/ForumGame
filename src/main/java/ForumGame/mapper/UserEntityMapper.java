package ForumGame.mapper;

import ForumGame.domain.Role;
import ForumGame.domain.UserEntity;
import ForumGame.domain.UserStatus;
import ForumGame.dto.UserEntityDto;

public class UserEntityMapper {
    public static UserEntity mapToEntity(UserEntityDto dto){
        UserEntity user = new UserEntity();
        user.setLogin(dto.getLogin());
        user.setPassword(dto.getPassword());
        user.setRoles(Role.USER);
        user.setStatus(UserStatus.ACTIVE);
        return user;
    }
}
