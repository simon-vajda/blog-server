package hu.vsimon.blogserver.auth;

import hu.vsimon.blogserver.auth.payload.JWTResponse;
import hu.vsimon.blogserver.auth.payload.LoginRequest;
import hu.vsimon.blogserver.auth.payload.SignupRequest;
import hu.vsimon.blogserver.role.Role;
import hu.vsimon.blogserver.role.RoleName;
import hu.vsimon.blogserver.role.RoleRepository;
import hu.vsimon.blogserver.security.jwt.JWTUtils;
import hu.vsimon.blogserver.user.AppUser;
import hu.vsimon.blogserver.user.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final AppUserRepository appUserRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtils jwtUtils;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, AppUserRepository appUserRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, JWTUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.appUserRepository = appUserRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<?> signIn(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        AppUser appUser = (AppUser) authentication.getPrincipal();
        List<String> roles = appUser.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JWTResponse(jwt, appUser.getId(), appUser.getName(), appUser.getEmail(), roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignupRequest request) {
        if(appUserRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyUsedException();
        }

        AppUser appUser = new AppUser(
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getName()
        );

        Collection<Role> roles = new HashSet<>();
        Role role = roleRepository.findByName(RoleName.ROLE_USER).orElseThrow(() -> new RuntimeException("ROLE_USER is not found"));
        roles.add(role);

        appUser.setRoles(roles);
        appUserRepository.save(appUser);

        return ResponseEntity.ok("success");
    }
}
