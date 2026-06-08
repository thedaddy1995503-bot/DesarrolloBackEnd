package com.sistema.security;

import com.sistema.model.User;
import com.sistema.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Esta clase se encarga de crear el usuario por defecto
 * automáticamente cuando se enciende el servidor.
 */
@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Verificar si el usuario admin ya existe en la base de datos
        if (userRepository.findByUsername("admin").isEmpty()) {
            
            // Si no existe, lo creamos
            User admin = new User(
                    null,
                    "admin",
                    passwordEncoder.encode("123"),
                    "ADMIN"
            );
            
            userRepository.save(admin);
            System.out.println("==================================================");
            System.out.println("Usuario por defecto creado automáticamente: admin / 123");
            System.out.println("==================================================");
        }
    }
}
