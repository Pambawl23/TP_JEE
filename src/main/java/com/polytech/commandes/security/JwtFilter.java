package com.polytech.commandes.security;
import jakarta.servlet.*; import jakarta.servlet.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException; import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwt;
    public JwtFilter(JwtUtil jwt) { this.jwt = jwt; }

    private static String extractToken(String header) {
        if (header == null) {
            return null;
        }
        var value = header.trim();
        if (value.isEmpty()) {
            return null;
        }
        if (value.regionMatches(true, 0, "Bearer", 0, 6)) {
            if (value.length() == 6) {
                return null;
            }
            var token = value.substring(6).trim();
            return token.isEmpty() ? null : token;
        }
        return value;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest r, HttpServletResponse s, FilterChain c) throws ServletException, IOException {
        var token = extractToken(r.getHeader("Authorization"));
        if (token != null) {
            try {
                var d = jwt.verify(token);
                var role = d.getClaim("role").asString();
                if (role == null || role.isBlank()) {
                    role = Role.ROLE_CLIENT.name();
                }
                var a = new UsernamePasswordAuthenticationToken(d.getSubject(), null, List.of(new SimpleGrantedAuthority(role)));
                SecurityContextHolder.getContext().setAuthentication(a);
            } catch (Exception ignored) {
                SecurityContextHolder.clearContext();
            }
        }
        c.doFilter(r, s);
    }
}
