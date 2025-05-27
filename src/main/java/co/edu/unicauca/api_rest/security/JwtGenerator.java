package co.edu.unicauca.api_rest.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority; // Importar esta clase
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap; // Importar esta clase
import java.util.Map;     // Importar esta clase
import java.util.stream.Collectors; // Importar esta clase

@Component
public class JwtGenerator {

    // Clave secreta (cambiar a una más segura y almacenarla externamente en producción)
    // Para desarrollo, puedes generar una con Keys.secretKeyFor(SignatureAlgorithm.HS512)
    private static final Key JWT_SECRET = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    // Tiempo de expiración del token (ej. 1 hora = 60 * 60 * 1000 ms)
    private static final long JWT_EXPIRATION_MS = 3600000; // 1 hora

    public String generateToken(Authentication authentication) {
        String username = authentication.getName(); // En este caso, el correo del usuario
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + JWT_EXPIRATION_MS);

        // --- INICIO DE LOS CAMBIOS ---
        // 1. Obtener los roles/autoridades del objeto Authentication
        // authentication.getAuthorities() retorna una colección de GrantedAuthority
        String roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority) // Mapea cada GrantedAuthority a su String de autoridad (ej. "ROL_DOCENTE")
                .collect(Collectors.joining(",")); // Une todos los roles en una sola cadena separada por comas

        // 2. Crear un mapa para los claims personalizados
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", roles); // <-- ¡Aquí se añade el claim de roles!
        // También podrías añadir otros claims si los necesitas, como el ID del usuario, etc.
        // claims.put("userId", someUserId); // Ejemplo

        // --- FIN DE LOS CAMBIOS ---

        return Jwts.builder()
                .setSubject(username)      // 'sub' claim (el nombre de usuario/correo)
                .setIssuedAt(currentDate)  // 'iat' claim (fecha de emisión)
                .setExpiration(expireDate) // 'exp' claim (fecha de expiración)
                .addClaims(claims)         // <-- ¡Añadir los claims personalizados aquí!
                .signWith(JWT_SECRET, SignatureAlgorithm.HS512) // Firma el token con tu clave secreta y algoritmo
                .compact();
    }

    // Los métodos getUsernameFromJwt y validateToken no necesitan cambios para este problema,
    // pero si en el futuro necesitas leer los roles del token, lo harías desde 'claims.get("roles")'.
    public String getUsernameFromJwt(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(JWT_SECRET)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(JWT_SECRET).build().parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            throw new AuthenticationCredentialsNotFoundException("JWT fue expirado o incorrecto", ex.fillInStackTrace());
        }
    }
}