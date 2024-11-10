//package com.shavi.RealTimeEventTicketingSystem.util;
//
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import org.springframework.stereotype.Component;
//
//import java.util.Date;
//
//@Component
//public class JwtUtil {
//
//    private String secretKey = "123456789"; // Replace with your secret key
//
//    // Generate JWT token
//    public String generateToken(String username) {
//        return Jwts.builder()
//                .setSubject(username)
//                //.claim("role", role)
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour expiration
//                .signWith(SignatureAlgorithm.HS256, secretKey)
//                .compact();
//    }
//
//    // Validate JWT token
//    public boolean validateToken(String token) {
//        try {
//            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
//            return true;
//        } catch (Exception e) {
//            return false;
//        }
//    }
//
////    public String extractUsername(String token) {
////        return Jwts.parser()
////                .setSigningKey(secretKey)
////                .parseClaimsJws(token)
////                .getBody()
////                .getSubject();
////    }
//
//}



package com.shavi.RealTimeEventTicketingSystem.util;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    // It's better to inject the secret key from application properties
    @Value("${jwt.secret}")
    private String secretKey;


    @Value("${jwt.expirationMs}")
    private long jwtExpirationMs;

    // Generate JWT token
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                //.claim("role", role) // Optionally add roles or other claims
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // Validate JWT token
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // Log token validation errors if necessary
            return false;
        }
    }

    // Extract username from token
    public String extractUsername(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
