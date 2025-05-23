package com.backend.backend.config.security;

import com.backend.backend.user.UserRepository;
import com.backend.backend.user.auth.AuthService;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity  // to use @Preauthorize, @PostAuthorize
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserRepository userRepository;

    /**
     * When spring starter security dependency is added in pom.xml, SecurityFilterChain MUST be defined
     * Authorises access to API, once security dependency is added
     * @param http HTTPSecurity object
     * @param jwtFilter OncePerRequestFilter object; injected by spring from jwt.JWTFilter
     * @param authenticationProvider AuthenticationProvider object, to authenticate users
     * @return SecurityFilterChain
     * @throws Exception Exception
     * @see com.backend.backend.config.security.CORSConfiguration
     * @see com.backend.backend.config.security.jwt.JWTFilter
     */
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http, OncePerRequestFilter jwtFilter, AuthenticationProvider authenticationProvider) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req ->
                        req
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()  // allow CORS preflight requests
                        .requestMatchers("/api/v1/auth/**",
                                        "/api/v1/persons",
                                        "/api/v1/persons/{id}",
                                        "/api/v1/persons/email/{email}",
                                        "/api/v1/persons/phone/{phone}",
                                        "/api/v1/persons/five-recent",
                                        "/api/v1/persons/gender-stats",
                                        "/api/v1/persons/county/{name}",
                                        "/api/v1/counties",
                                        "/api/v1/counties/{id}",
                                        "/api/v1/counties/code/{code}",
                                        "/api/v1/counties/name/{name}",
                                        "/api/v1/counties/top-counties")
                                .permitAll()
                                .anyRequest()
                                .authenticated()  // any route not declared above requires a token
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    /**
     * BCrypt used for password hashing
     * @see AuthService
     * @return PasswordEncoder object
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * UserDetailsService interface contains single method loadUserByUsername()
     * Method used to query database using userRepository, for user details
     * Implemented by DaoAuthenticationProvider() below
     * @return UserDetailsService object
     */
    @Bean
    public UserDetailsService userDetailsService() {
        // lambda expression; username is the parameter
        return username -> userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    /**
     * AuthenticationManager interface used to authenticate users
     * AuthenticationManager relies on AuthenticationProviders (e.g. DaoAuthenticationProvider())
     * @see AuthService
     * @return AuthenticationProvider object
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
