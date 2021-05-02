package by.library.mylib.security;

import by.library.mylib.entity.User;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Component
public class JWTTokenProvider {
    public static final Logger LOG = LoggerFactory.getLogger(JWTTokenProvider.class);

    /**
     * Задаем параметры генерации вебтокена
     *
     * @param authentication переданная авторизация
     * @return возвращаем json с токеном
     */
    public String generateToken(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Date now = new Date(System.currentTimeMillis());
        Date expiryDate = new Date((now.getTime() + SecurityConstants.EXPIRATION_TIME));
        String userId = Long.toString(user.getId());
        Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put("id", userId);
        claimsMap.put("username", user.getUsername());
        return Jwts.builder().setSubject(userId).addClaims(claimsMap)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET).compact();
    }

    /**
     * Валидация токена
     * @param token полученный от пользователя токен
     * @return действующий ли токен
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SecurityConstants.SECRET)
                    .parseClaimsJws(token);
            return true;

        } catch (SignatureException | MalformedJwtException |
                ExpiredJwtException | UnsupportedJwtException |
                IllegalArgumentException ex) {
            LOG.error(ex.getMessage());
            return false;
        }
    }
    public Long getUserIdFromToken(String token){
        Claims claims=Jwts.parser().setSigningKey(SecurityConstants.SECRET)
                .parseClaimsJws(token).getBody();
        String id=(String)claims.get("id");
        return Long.parseLong(id);
    }
}
