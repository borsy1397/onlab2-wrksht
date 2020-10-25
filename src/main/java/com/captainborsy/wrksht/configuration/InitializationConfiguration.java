package com.captainborsy.wrksht.configuration;

import com.captainborsy.wrksht.model.Role;
import com.captainborsy.wrksht.model.User;
import com.captainborsy.wrksht.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;
import java.util.List;

@Configuration
public class InitializationConfiguration {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final String defaultAdminUsername;
    private final String defaultAdminPassword;

    @Autowired
    public InitializationConfiguration(UserRepository userRepository,
                                       PasswordEncoder passwordEncoder,
                                       @Value("${wrksht.admin.username}") String defaultAdminUsername,
                                       @Value("${wrksht.admin.password}") String defaultAdminPassword) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.defaultAdminUsername = defaultAdminUsername;
        this.defaultAdminPassword = defaultAdminPassword;
    }

    @PostConstruct
    private void initAdmin() {
        List<User> users = userRepository.findAllByRole(Role.ADMIN);

        if (users.isEmpty()) {
            User user = User.builder()
                    .username(defaultAdminUsername)
                    .password(passwordEncoder.encode(defaultAdminPassword))
                    .firstName("Admin")
                    .lastName("Admin")
                    .role(Role.ADMIN)
                    .build();
            userRepository.save(user);
        }

    }

}
