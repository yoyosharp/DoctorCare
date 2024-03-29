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

    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);
    @Value("${jwt.secret-key}")
    private String SECRET_KEY;
    @Value("${jwt.auth-expiration-time-second}")
    private long AUTH_EXPIRE_TIME;

    //generate key
    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));
    }

    //generate token from a fully populated Authentication
    public String generateToken(Authentication auth) {
        UserDetails userPrincipal = (UserDetails) auth.getPrincipal();
        Date currentDate = new Date();

        //add username to 'sub' claim or add new claim
        String token = Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(currentDate)
                .setExpiration(new Date(currentDate.getTime() + 1000 * AUTH_EXPIRE_TIME))
                .signWith(key(), SignatureAlgorithm.HS512)
                .compact();

        return token;
    }

    private Claims getClaimsFromToken(String token) {
        //build jwtParser and parse Claims from token
        return Jwts.parserBuilder().setSigningKey(key()).build().
                parseClaimsJws(token).getBody();

    }

    public String getUserNameFromJwt(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token);
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
        } catch (SignatureException e) {
            logger.error("Signature Exception : {}", e.getMessage());
            throw new JwtAuthenticationException("Invalid key or JWT has been modified");
        }
    }

}
