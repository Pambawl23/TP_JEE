package com.polytech.commandes.security;
import com.polytech.commandes.entity.Client;
import com.polytech.commandes.repository.ClientRepo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.Map;
import java.util.Optional;

@Tag(name = "Authentification", description = "Obtenir un token JWT")
@RestController
@RequestMapping("/securite")
public class AuthController {
    private final JwtUtil jwt; private final ClientRepo repo;
    public AuthController(JwtUtil jwt, ClientRepo repo) { this.jwt = jwt; this.repo = repo; }
    @Operation(summary = "Obtenir un token JWT", description = "Envoie email et nom, retourne un token pour Authorization: Bearer <token>")
    @PostMapping("/token")
    public Map<String, String> token(@RequestBody Map<String, String> b) {
        var email = Optional.ofNullable(b.get("email")).orElse("").trim().toLowerCase();
        var nom = Optional.ofNullable(b.get("nom")).orElse("").trim();
        if (email.isBlank() || nom.isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "nom et email sont obligatoires");
        var c = repo.findByEmail(email).orElseGet(() -> {
            var n = new Client(); n.setNom(nom); n.setEmail(email);
            n.setRole(Role.ROLE_CLIENT); return repo.save(n); });
        if (!c.getNom().equalsIgnoreCase(nom))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Client invalide");
        if (c.getRole() == null) {
            c.setRole(Role.ROLE_CLIENT);
            c = repo.save(c); }
        return Map.of("token", jwt.create(c.getEmail(), c.getRole().name())); }
}
