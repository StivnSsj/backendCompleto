package co.edu.unicauca.api_rest.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode("password");
        System.out.println("Hashed password for 'password': " + hashedPassword);
    }
}
