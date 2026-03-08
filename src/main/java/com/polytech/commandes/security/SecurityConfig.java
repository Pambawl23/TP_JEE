package com.polytech.commandes.security;
import org.springframework.context.annotation.*; import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.*; import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private static final String MSG_403 = "{\"message\":\"Vous n'êtes pas autorisé.\"}";
    private final JwtFilter filter;
    public SecurityConfig(JwtFilter filter) { this.filter = filter; }
    private static void send403(HttpServletResponse res) throws IOException { res.setStatus(HttpServletResponse.SC_FORBIDDEN); res.setContentType("application/json"); res.getWriter().write(MSG_403); }
    @Bean
    SecurityFilterChain chain(HttpSecurity h) throws Exception {
        return h.csrf(AbstractHttpConfigurer::disable).httpBasic(AbstractHttpConfigurer::disable).formLogin(AbstractHttpConfigurer::disable).sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).exceptionHandling(e -> e.authenticationEntryPoint((req, res, ex) -> send403(res)).accessDeniedHandler((req, res, ex) -> { try { send403(res); } catch (IOException ioe) { throw new RuntimeException(ioe); } })).authorizeHttpRequests(a -> a.requestMatchers("/securite/token", "/swagger-ui/**", "/v3/api-docs/**").permitAll().requestMatchers("/api/**").authenticated().anyRequest().authenticated()).addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class).build();
    }
}
