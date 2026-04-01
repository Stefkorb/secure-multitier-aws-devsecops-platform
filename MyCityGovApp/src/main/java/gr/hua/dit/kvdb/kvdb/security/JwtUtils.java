package gr.hua.dit.kvdb.kvdb.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {

    private final String jwtSecret = "mySecretKey12345678910fsohdsohfsoufsdiojfdjcfosiduf4s88ssflsdnojsfd89js8989u8vu98sud89uf98us88u9fu8su8rufusuf0usu900000000000dufisdoufour8sjvsdjoi90esjpkfpsdpvlor0sifjsoijviojs89jr8sijv8essvjosehsoefjoesu8fosufe8soufoseufoseufosdjvose98jvso8evsjoshvo8sh8osehjofs"; // αλλάξ’ το σε πιο ασφαλές
    private final long jwtExpirationMs = 86400000; // 1 μέρα

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
