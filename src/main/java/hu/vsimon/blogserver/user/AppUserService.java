package hu.vsimon.blogserver.user;

import hu.vsimon.blogserver.role.Role;
import hu.vsimon.blogserver.role.RoleName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AppUserService implements UserDetailsService {

    AppUserRepository userRepository;

    @Autowired
    public AppUserService(AppUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public AppUser loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return user;
    }

    public boolean isAdmin(String email) {
        for (Role role : loadUserByUsername(email).getRoles()) {
            if (role.getName().equals(RoleName.ROLE_ADMIN)) {
                return true;
            }
        }

        return false;
    }
}
