package com.polytech.commandes.security;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.time.Instant;

@Component
public class JwtUtil {
    private final Algorithm algo; private final long exp;
    public JwtUtil(@Value("${security.jwt.secret:change-moi}") String secret, @Value("${security.jwt.expiration:3600}") long exp) { this.algo = Algorithm.HMAC256(secret); this.exp = exp; }
    public String create(String user, String role) { Instant now = Instant.now(); return JWT.create().withSubject(user).withClaim("role", role).withIssuedAt(now).withExpiresAt(now.plusSeconds(exp)).sign(algo); }
    public DecodedJWT verify(String token) { return JWT.require(algo).build().verify(token); }
}
