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

        //TODO: how should it work!
        //in case of working with spring auth we should retrieve the actual password
        //but in common cases our password stored in db in encrypted form....

        String enteredPassword = user.getPassword();
        String enteredPasswordEncoded = passwordEncoder.encode(enteredPassword);

        ApplicationUser applicationUser = new ApplicationUser(
                login,
                enteredPasswordEncoded,
                authorities,
                true,
                true,
                true,
                true);

        return Optional.of(applicationUser);
    }

}
