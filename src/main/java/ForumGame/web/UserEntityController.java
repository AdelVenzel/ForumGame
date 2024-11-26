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
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PostMapping("/v1/users")
    public void register(@RequestBody UserEntityDto dto) {
        UserEntity user = UserEntityMapper.mapToEntity(dto);
        userService.register(user);
    }

    @PutMapping("/v1/users/role")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void changeRole(@AuthenticationPrincipal UserDetails userDetails, @RequestParam String login, @RequestParam String role) {
        UserEntity myUser = userService.checkBlocked(userDetails);
        userService.changeRole(Role.valueOf(role), login, myUser);
    }

    @PutMapping("/v1/users")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void blockingUser(@AuthenticationPrincipal UserDetails userDetails, @RequestParam String login, @RequestParam boolean block) {
        UserEntity myUser = userService.checkBlocked(userDetails);
        userService.blockUser(login, block, myUser);
    }

    @GetMapping("/v1/users/token")
    public ResponseEntity<String> generateToken(Principal principal) {
        final String token = jwtTokenUtil.generateToken(principal.getName());
        return ResponseEntity.ok(token);
    }
}
