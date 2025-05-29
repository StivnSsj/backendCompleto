package co.edu.unicauca.api_rest.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays; // Necesario para Arrays.asList
import java.util.Date;
import java.util.HashMap;
import java.util.List; // Necesario para List
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JwtGenerator {

    private static final Key JWT_SECRET = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    private static final long JWT_EXPIRATION_MS = 3600000; // 1 hora

    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + JWT_EXPIRATION_MS);

        String roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", roles);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(currentDate)
                .setExpiration(expireDate)
                .addClaims(claims)
                .signWith(JWT_SECRET, SignatureAlgorithm.HS512)
                .compact();
    }

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

    /**
     * Extrae la lista de roles (autoridades) de un token JWT.
     * Estos roles se usaron como un claim "roles" en el momento de la generación del token.
     *
     * @param token El token JWT del cual extraer los roles.
     * @return Una lista de Strings, donde cada String es un rol (ej. "ROL_DOCENTE", "ROL_COORDINADOR").
     * Retorna una lista vacía si no se encuentran roles o si el token es inválido.
     */
    public List<String> getRolesFromJwt(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(JWT_SECRET)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            // Obtener el claim "roles"
            Object rolesObject = claims.get("roles");

            if (rolesObject instanceof String) {
                // Si el claim es una cadena separada por comas (como lo generamos)
                String rolesString = (String) rolesObject;
                return Arrays.asList(rolesString.split(","));
            } else if (rolesObject instanceof List) {
                // En caso de que se haya guardado como una lista directamente (aunque no es nuestro caso actual)
                return (List<String>) rolesObject;
            } else {
                return List.of(); // Retorna una lista vacía si el claim no es de un tipo esperado
            }
        } catch (Exception ex) {
            // Manejar excepciones si el token es inválido, expirado, etc.
            // Puedes loguear el error o retornar una lista vacía.
            System.err.println("Error al extraer roles del JWT: " + ex.getMessage());
            return List.of(); // Retorna una lista vacía en caso de error
        }
    }
}