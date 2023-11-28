package com.fx23121.DoctorCare.Service;


import com.fx23121.DoctorCare.Exception.JwtAuthenticationException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret-key}")
    private String SECRET_KEY;

    @Value("${jwt.auth-expiration-time-second}")
    private int AUTH_EXPIRE_TIME;
    @Value("${jwt.password-reset-expire-time-second}")
    private int PASSWORD_RESET_EXPIRE_TIME;

    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));
    }

    public String generateToken(Authentication auth) {
        UserDetails userPrincipal = (UserDetails) auth.getPrincipal();
        Date currentDate = new Date();

        String token = Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(currentDate)
                .setExpiration(new Date(currentDate.getTime() + 1000 * AUTH_EXPIRE_TIME))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();

        return token;
    }

    private Claims getClaimsFromToken(String token) {

        return Jwts.parser()
                .setSigningKey(key())
                .parseClaimsJws(token)
                .getBody();
    }

    public String getUserNameFromJwt(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(key()).parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT: {}", e.getMessage());
            throw new JwtAuthenticationException("Invalid JWT");
        } catch (ExpiredJwtException e) {
            logger.error("JWT is expired: {}", e.getMessage());
            throw new JwtAuthenticationException("JWT is expired");
        } catch (UnsupportedJwtException e) {
            logger.error("Token is unsupported: {}", e.getMessage());
            throw new JwtAuthenticationException("Token is unsupported");
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
            throw new JwtAuthenticationException("JWT claims set is empty");
        }
        catch (SignatureException e) {
            logger.error("Signature Exception : {}", e.getMessage());
            throw new JwtAuthenticationException("Invalid key or JWT has been modified");
        }
    }

    public String generatePasswordResetToken(String userEmail) {

        Date currentDate = new Date();

        String token = Jwts.builder()
                .setSubject(userEmail)
                .setIssuedAt(currentDate)
                .setExpiration(new Date(currentDate.getTime() + 1000 * PASSWORD_RESET_EXPIRE_TIME))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();

        return token;
    }
}
