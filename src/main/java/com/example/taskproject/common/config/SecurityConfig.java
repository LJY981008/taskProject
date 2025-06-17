package com.example.taskproject.common.config;

import com.example.taskproject.common.enums.UserRole;
import com.example.taskproject.common.exception.JwtAccessDeniedHandler;
import com.example.taskproject.common.exception.JwtAuthenticationEntryPoint;
import com.example.taskproject.common.filter.JwtFilter;
import com.example.taskproject.common.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of(
                "http://localhost:3100"
        ));

        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // 이 부분이 중요!
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/swagger-ui/", "/swagger-ui.html", "/v3/api-docs/").permitAll()
                        .requestMatchers("/api/auth/register","api/auth/login").permitAll()
                        .requestMatchers("/api/tasks/my").hasRole(UserRole.USER.name())
                        .requestMatchers("/api/tasks").hasRole(UserRole.USER.name())
                        .requestMatchers("/api/dashboard/stats","/api/dashboard/my-tasks", "/api/statistics/weekly-trend", "/api/statistics/over-due").hasRole(UserRole.USER.name())
                        .requestMatchers("/api/users/**").hasRole(UserRole.USER.name())
                        .requestMatchers(HttpMethod.POST, "/api/tasks").hasRole(UserRole.USER.name())
                        .requestMatchers("/api/tasks/my").hasRole(UserRole.USER.name())
                        .requestMatchers("/api/tasks/**").hasRole(UserRole.USER.name())
                )
                .addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(configure -> configure
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                )
                .build();
    }

}
