package com.Hirav.real_estate.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.Customizer;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig {

    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);

        jdbcUserDetailsManager.setUsersByUsernameQuery(
                "SELECT username, password, true FROM users WHERE username=?"
        	);

        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(
        	    "SELECT username, CONCAT('ROLE_', role) FROM users WHERE username=?"
        	);


        return jdbcUserDetailsManager;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(configurer ->
                configurer
                
                        .requestMatchers(HttpMethod.POST, "/users").permitAll()
                        .requestMatchers(HttpMethod.GET, "/users").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/users/{id}").hasAnyRole("BUYER", "SELLER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/users/{id}").hasAnyRole("BUYER", "SELLER", "ADMIN") 
                        .requestMatchers(HttpMethod.DELETE, "/users/{id}").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/properties").hasRole("SELLER") 
                        .requestMatchers(HttpMethod.GET, "/properties").permitAll() 
                        .requestMatchers(HttpMethod.PUT, "/properties/{id}").hasRole("SELLER")
                        .requestMatchers(HttpMethod.DELETE, "/properties/{id}").hasRole("SELLER") 

                        .requestMatchers(HttpMethod.POST, "/bookings").hasRole("BUYER") 
                        .requestMatchers(HttpMethod.GET, "/bookings/buyer").hasRole("BUYER")
                        .requestMatchers(HttpMethod.GET, "/bookings/owner").hasRole("SELLER")
                        .requestMatchers(HttpMethod.PUT, "/bookings/{id}").hasRole("BUYER") 
                        .requestMatchers(HttpMethod.PUT, "/bookings/{id}/status").hasRole("SELLER") 
                        .requestMatchers(HttpMethod.DELETE, "/bookings/{id}").hasRole("BUYER")
        );

        http.httpBasic(Customizer.withDefaults());

        http.csrf(csrf -> csrf.disable());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
