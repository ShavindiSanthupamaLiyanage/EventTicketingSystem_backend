package com.shavi.RealTimeEventTicketingSystem.configurations;

import com.shavi.RealTimeEventTicketingSystem.enums.UserRole;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for testing; enable it in production
                .authorizeHttpRequests(auth -> auth
                                .anyRequest().permitAll()
                )
                .httpBasic(httpBasicCustomizer -> {}); // Enable basic HTTP authentication for secured endpoints

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // Use your own UserDetailsService in production
    @Bean
    public UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager(
                User.withUsername("admin")
                        .password(passwordEncoder().encode("admin"))
                        .roles("ADMIN")
                        .build(),
                User.withUsername("vendor")
                        .password(passwordEncoder().encode("vendorpass"))
                        .roles(UserRole.VENDOR.name())
                        .build(),
                User.withUsername("customer")
                        .password(passwordEncoder().encode("customerpass"))
                        .roles(UserRole.CUSTOMER.name())
                        .build()
        );
    }

}
