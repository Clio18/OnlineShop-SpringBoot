package com.obolonyk.shopboot.dao.jdbc;

import com.obolonyk.shopboot.auth.ApplicationUser;
import com.obolonyk.shopboot.auth.ApplicationUserDao;
import com.obolonyk.shopboot.dao.UserDao;
import com.obolonyk.shopboot.dao.jdbc.rowmapper.UserRowMapper;
import com.obolonyk.shopboot.entity.User;
import com.obolonyk.shopboot.security.ApplicationUserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository("prod")
public class JdbcUserDao implements UserDao, ApplicationUserDao {
    private static final String SELECT_BY_LOGIN = "SELECT id, name, last_name, login, email, password, salt, role FROM users WHERE login = ?;";
    private static final String SAVE = "INSERT INTO users (name, last_name, login, email, password, salt, role) VALUES (?, ?, ?, ?, ?, ?, 'USER');";

    private final RowMapper<User> rowMapper = new UserRowMapper();

    private final JdbcTemplate jdbcTemplate;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public JdbcUserDao(JdbcTemplate jdbcTemplate, PasswordEncoder passwordEncoder) {
        this.jdbcTemplate = jdbcTemplate;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User getByLogin(String login) {
        return jdbcTemplate.queryForObject(SELECT_BY_LOGIN, rowMapper, login);
    }

    @Override
    public void save(User user) {
        jdbcTemplate.update(SAVE,
                user.getName(),
                user.getLastName(),
                user.getLogin(),
                user.getEmail(),
                user.getPassword(),
                user.getSalt());
    }

    @Override
    public Optional<ApplicationUser> selectApplicationUserByUsername(String login) {
        User user = getByLogin(login);
        ApplicationUserRole applicationUserRole = ApplicationUserRole.valueOf(user.getRole().name());
        Set<SimpleGrantedAuthority> authorities = applicationUserRole.getGrantedAuthorities();
        //TODO: it is strange?
        String encode = passwordEncoder.encode(user.getLogin());
        ApplicationUser applicationUser = new ApplicationUser(user.getLogin(),
                encode,
                authorities,
                true,
                true,
                true,
                true);

        return Optional.of(applicationUser);
    }
}
