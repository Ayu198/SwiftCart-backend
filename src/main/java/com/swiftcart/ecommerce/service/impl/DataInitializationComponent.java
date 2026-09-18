package com.swiftcart.ecommerce.service.impl;

import com.swiftcart.ecommerce.domain.USER_ROLE;
import com.swiftcart.ecommerce.modal.User;
import com.swiftcart.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializationComponent implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        initializeAdminUser();
    }

    private void initializeAdminUser() {
        String adminUsername = "ayush.admin@swift.com";

        if(userRepository.findByEmail(adminUsername) == null) {
            User adminUser = new User();
            adminUser.setPassword((passwordEncoder.encode("admin123")));
            adminUser.setEmail(adminUsername);
            adminUser.setFullName("Admin");
            adminUser.setRole(USER_ROLE.ROLE_ADMIN);

            userRepository.save(adminUser);
        }
    }
}
