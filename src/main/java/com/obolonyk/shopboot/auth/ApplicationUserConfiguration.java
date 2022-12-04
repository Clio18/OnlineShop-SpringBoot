package com.obolonyk.shopboot.auth;
import com.obolonyk.shopboot.entity.User;
import com.obolonyk.shopboot.repository.UserRepository;
import com.obolonyk.shopboot.security.ApplicationUserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Component
public class ApplicationUserConfiguration implements ApplicationUserDao {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Optional<ApplicationUser> selectApplicationUserByUsername(String login) {
        Optional<User> optionalUser = userRepository.findByLogin(login);
        User user = optionalUser.orElseThrow(() -> new RuntimeException(String.format("User with %s login not found", login)));
        ApplicationUserRole applicationUserRole = ApplicationUserRole.valueOf(user.getRole().name());
        Set<SimpleGrantedAuthority> authorities = applicationUserRole.getGrantedAuthorities();

        //TODO: it is strange!
        //it works only if login is the same as password, because in this case the result of encryption is the same
        //the main issue here is that we retrieve encrypted password from db...
        String password = passwordEncoder.encode(user.getLogin());

        ApplicationUser applicationUser = new ApplicationUser(user.getLogin(),
                password,
                authorities,
                true,
                true,
                true,
                true);

        return Optional.of(applicationUser);
    }

}
