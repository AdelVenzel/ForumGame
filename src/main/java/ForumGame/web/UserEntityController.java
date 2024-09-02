package ForumGame.web;

import ForumGame.domain.Role;
import ForumGame.domain.UserEntity;
import ForumGame.dto.UserEntityDto;
import ForumGame.mapper.UserEntityMapper;
import ForumGame.security.JwtTokenUtil;
import ForumGame.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserEntityController {

    private final JwtTokenUtil jwtTokenUtil;

    private final UserServiceImpl userService;

    @PostMapping("/users/registration")
    public String register(@RequestBody UserEntityDto dto) {
        UserEntity user = UserEntityMapper.mapToEntity(dto);
        userService.register(user);
        return "User registration";
    }


    @PutMapping("/users/changeRole")
    public String changeRole(@AuthenticationPrincipal UserDetails userDetails, @RequestParam String login, @RequestParam String role) {
        UserEntity userEntity = userService.checkBlocked(userDetails);
        userService.changeRole(userEntity, Role.valueOf(role), login);
        return "Role change was successful";
    }

    @PutMapping("/users/blocking")
    public String blockingUser(@AuthenticationPrincipal UserDetails userDetails, @RequestParam String login) {
        UserEntity userEntity = userService.checkBlocked(userDetails);
        userService.blockingUser(userEntity, login);
        return "User is blocked";
    }

    @PutMapping("/users/unblocking")
    public String unblockingUser(@AuthenticationPrincipal UserDetails userDetails, @RequestParam String login) {
        UserEntity userEntity = userService.checkBlocked(userDetails);
        userService.unblockingUser(userEntity, login);
        return "User is unblocked";
    }


    @GetMapping("/users/generateToken")
    public ResponseEntity<String> generateToken(Principal principal) {
        final String token = jwtTokenUtil.generateToken(principal.getName());
        return ResponseEntity.ok(token);
    }
}
