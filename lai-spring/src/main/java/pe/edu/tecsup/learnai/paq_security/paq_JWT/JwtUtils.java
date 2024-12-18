package pe.edu.tecsup.learnai.paq_security.paq_JWT;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expirationTime;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String generateToken(String username, String v_code) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("v_code", v_code);

        return Jwts.builder()
                .setClaims(claims) // Aquí se ponen los claims que incluyen el username y v_code
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractVerificationCode(String token) {
        String v_code = (String) Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("v_code");  // Acceder correctamente al claim "v_code"
        System.out.println("Extracted verification code: " + v_code);
        return v_code;
    }


    public String extractUsername(String token) {
        String username = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        System.out.println("Extracted username: " + username);  // Log para depurar el nombre extraído
        return username;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // Logger opcional
        }
        return false;
    }
}

