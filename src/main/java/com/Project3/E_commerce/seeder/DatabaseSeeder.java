package com.Project3.E_commerce.seeder;

import com.Project3.E_commerce.exceptions.InformationNotFoundException;
import com.Project3.E_commerce.models.Category;
import com.Project3.E_commerce.models.Product;
import com.Project3.E_commerce.models.Role;
import com.Project3.E_commerce.models.User;
import com.Project3.E_commerce.repositorys.CategoryRepository;
import com.Project3.E_commerce.repositorys.ProductRepository;
import com.Project3.E_commerce.repositorys.RoleRepository;
import com.Project3.E_commerce.repositorys.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class DatabaseSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        log.info("Starting database seeding...");
        seedRoles();
        seedCategories();
        seedProducts();
        seedAdminUser();
        log.info("Database seeding completed!");
    }


    private void seedRoles() {
        log.info("Seeding roles...");
        if (roleRepository.count() > 0) {
            log.info("Roles already exist. Skipping role seeding.");
            return;
        }
        if (!roleRepository.existsByName("ADMIN")) {
            roleRepository.save(new Role(null, "ADMIN", null));
        }
        if (!roleRepository.existsByName("CUSTOMER")) {
            roleRepository.save(new Role(null, "CUSTOMER", null));
        }
        log.info("Created roles: ADMIN, CUSTOMER");
    }


    private void seedCategories() {
        log.info("Seeding categories...");

        String[] categoryNames = {"Electronics", "Clothing", "Home & Kitchen"};

        for (String name : categoryNames) {
            if (!categoryRepository.existsByName(name)) {
                Category category = new Category();
                category.setName(name);
                categoryRepository.save(category);
                log.info("Created category: {}", name);
            }
        }
    }


    private void seedProducts() {
        log.info("Seeding products...");
        if (productRepository.count() >= 6) {
            log.info("Products already exist. Skipping product seeding.");
            return;
        }

        Category electronics = categoryRepository.findByName("Electronics")
                .orElseThrow(() -> new InformationNotFoundException("Electronics category not found"));
        Category clothing = categoryRepository.findByName("Clothing")
                .orElseThrow(() -> new InformationNotFoundException("Clothing category not found"));
        Category home = categoryRepository.findByName("Home & Kitchen")
                .orElseThrow(() -> new InformationNotFoundException("Home & Kitchen category not found"));

        if (productRepository.count() < 2) {
            Product laptop = new Product();
            laptop.setName("Gaming Laptop Pro");
            laptop.setDescription("High-performance laptop for gaming and work");
            laptop.setPrice(1299.99);
            laptop.setStockQuantity(15);
            laptop.setCategory(electronics);
            productRepository.save(laptop);

            Product earbuds = new Product();
            earbuds.setName("Wireless Earbuds");
            earbuds.setDescription("Noise-cancelling Bluetooth earbuds with 24h battery");
            earbuds.setPrice(89.99);
            earbuds.setStockQuantity(50);
            earbuds.setCategory(electronics);
            productRepository.save(earbuds);
            log.info("Created 2 Electronics products");
        }


        if (productRepository.count() < 4) {
            Product hoodie = new Product();
            hoodie.setName("Premium Cotton Hoodie");
            hoodie.setDescription("Comfortable unisex hoodie, available in multiple colors");
            hoodie.setPrice(49.99);
            hoodie.setStockQuantity(30);
            hoodie.setCategory(clothing);
            productRepository.save(hoodie);

            Product shoes = new Product();
            shoes.setName("Running Shoes Ultra");
            shoes.setDescription("Lightweight running shoes with superior cushioning");
            shoes.setPrice(119.99);
            shoes.setStockQuantity(25);
            shoes.setCategory(clothing);
            productRepository.save(shoes);
            log.info("Created 2 Clothing products");
        }


        if (productRepository.count() < 6) {
            Product coffeeMaker = new Product();
            coffeeMaker.setName("Smart Coffee Maker");
            coffeeMaker.setDescription("Programmable coffee maker with app control");
            coffeeMaker.setPrice(79.99);
            coffeeMaker.setStockQuantity(20);
            coffeeMaker.setCategory(home);
            productRepository.save(coffeeMaker);

            Product blender = new Product();
            blender.setName("High-Speed Blender");
            blender.setDescription("1000W blender for smoothies, soups, and more");
            blender.setPrice(59.99);
            blender.setStockQuantity(35);
            blender.setCategory(home);
            productRepository.save(blender);
            log.info("Created 2 Home & Kitchen products");
        }
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
        if (adminRole == null) {
            log.error("ADMIN role not found! Cannot create admin user.");
            return;
        }

        User admin = new User();
        admin.setUsername("admin");
        admin.setEmail("admin@gmail.com");
        admin.setPassword(passwordEncoder.encode(adminPassword));
        admin.setRole(adminRole);
        admin.setIsVerified(true);
        admin.setIsDeleted(false);
        admin.setImage("http://res.cloudinary.com/dqqmgoftf/image/upload/v1775576374/f4c86146-3fbb-49a2-83aa-be503a5721ce.png");
        userRepository.save(admin);

        log.info("Created default admin user: {} (password: {})", adminEmail, adminPassword);
    }
}