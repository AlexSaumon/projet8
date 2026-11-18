package com.nnk.springboot;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PasswordEncodeTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testPassword() {

        Optional<User> user = userRepository.findByUsername("admin");

        if (user.isPresent()) {

            String password = user.get().getPassword();

            System.out.println("Hash en base : " + password);

            System.out.println(
                    "123456 valide : " +
                            passwordEncoder.matches("123456", password)
            );

            System.out.println(
                    "password valide : " +
                            passwordEncoder.matches("password", password)
            );

        }

        System.out.println("Utilisateur trouvé : " + user.isPresent());

        if (user.isPresent()) {
            System.out.println("Username : " + user.get().getUsername());
            System.out.println("Hash : " + user.get().getPassword());

            System.out.println("123456 valide : " +
                    passwordEncoder.matches("123456", user.get().getPassword()));
        }
    }
}