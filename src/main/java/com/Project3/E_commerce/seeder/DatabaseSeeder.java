package com.Project3.E_commerce.seeder;


import com.Project3.E_commerce.models.Role;
import com.Project3.E_commerce.models.User;
import com.Project3.E_commerce.repositorys.RoleRepository;
import com.Project3.E_commerce.repositorys.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class DatabaseSeeder implements CommandLineRunner {


    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;



    @Override
    @Transactional
    public void run(String... args) {
        log.info("Starting database seeding...");
        seedRoles();
        seedAdminUser();
        log.info("Database seeding completed!");
    }

    private void seedAdminUser() {
        log.info("Seeding admin user...");

        String adminEmail = "admin@gmail.com";
        String adminPassword = "123";

        if (userRepository.existsByEmail(adminEmail)) {
            log.info("Admin user already exists. Skipping admin user seeding.");
            return;
        }


        Role adminRole = roleRepository.findByName("ADMIN").orElse(null);

        User admin = new User();
        admin.setUsername("admin");
        admin.setEmail("admin@gmail.com");
        admin.setPassword(passwordEncoder.encode("123"));
        admin.setRole(adminRole);
        admin.setIsVerified(true);
        userRepository.save(admin);

        log.info("Created default admin user: {} (password: {})", adminEmail, adminPassword);
    }

    private void seedRoles() {
        log.info("Seeding roles...");


        if (roleRepository.count() > 0) {
            log.info("Roles already exist. Skipping role seeding.");
            return;
        }
        roleRepository.findByName("ADMIN")
                .orElseGet(() -> roleRepository.save(new Role(null, "ADMIN",null)));
        roleRepository.findByName("CUSTOMER")
                .orElseGet(() -> roleRepository.save(new Role(null, "CUSTOMER",null)));
    }

}