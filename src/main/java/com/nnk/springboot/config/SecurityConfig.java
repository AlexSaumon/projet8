
package com.nnk.springboot.config;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import java.io.IOException;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(
            UserRepository userRepository
    ) {
        return username -> userRepository.findByUsername(username)
                .map(user ->
                        org.springframework.security.core.userdetails.User
                                .withUsername(user.getUsername())
                                .password(user.getPassword())
                                .roles(user.getRole())
                                .build()
                )
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "Utilisateur introuvable : " + username
                        )
                );
    }


    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http.authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/webjars/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                );
        http.addFilterBefore(
                new OncePerRequestFilter() {
                    @Override
                    protected void doFilterInternal(
                            HttpServletRequest request,
                            HttpServletResponse response,
                            FilterChain filterChain
                    ) throws ServletException, IOException {

                        if ("/login".equals(request.getServletPath())
                                && request.getUserPrincipal() != null) {
                            response.sendRedirect("/bidList/list");
                            return;
                        }

                        filterChain.doFilter(request, response);
                    }
                },
                UsernamePasswordAuthenticationFilter.class
        )
                .formLogin(form -> form
                        // Utilise la page de connexion générée par Spring Security
                        .defaultSuccessUrl("/bidList/list", true)
                        .permitAll()
                )

                .httpBasic(httpBasic -> httpBasic.disable())

                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout=true")
                        .permitAll()
                );

        return http.build();
    }


}
