package com.demo.security2.service;


import com.demo.security2.repository.RevokedTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String SECRET_KEY = "1fd80bd1d08e3d87cf23e8d24db97014aed422e338dbced06343f99e1ab3a3fc" ;


    // EXTRACT ALL CLAIMS FROM THE TOKEN
    private Claims extractAllClaims(String token){
        return Jwts.parser().setSigningKey(getSignInKey())
                .build().parseClaimsJws(token).getBody();
    }

    // EXTRACT ONE SINGLE CLAIM
    public <T> T extractClaim(String token , Function<Claims , T> claimsResolver ){
        final Claims claims = extractAllClaims(token) ;
        return claimsResolver.apply(claims) ;
    }

    // EXTRACT USERNAME ( IN OUR CASE EMAIL )
    public String extractUsername(String token) {
        return extractClaim(token , Claims::getSubject) ;
    }

    // EXTRACT THE EXPIRATION DATE :
    private Date extractExpiration(String token) {
        return extractClaim(token , Claims::getExpiration);
    }

    // RETURN THE SECRET KEY FOR THE SIGNATURE
    private Key getSignInKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes) ;
    }

    // GENERATE TOKEN WITH EXTRACLAIMS
    public String generateToken(
            Map<String , Object> extraclaims ,
            UserDetails userDetails
    ){
        return Jwts.builder()
                .setClaims(extraclaims)
                .subject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 24*60*60*100 ))
                .signWith(getSignInKey() , SignatureAlgorithm.HS256)
                .compact() ;
    }

    // GENERATE TOKEN WITHOUT EXTRACLAIMS
    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>() , userDetails) ;
    }

    // TOKEN VALIDATION :
    public boolean isTokenValid(String token , UserDetails userDetails) {
        final String username = extractUsername(token) ;
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token) && !isTokenRevoked(token) );   }

    // CHECK IF THE TOKEN EXPIRED OR NOT
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date()) ;
    }

    // Logout :

    private final RevokedTokenRepository revokedTokenRepository;

    public JwtService(RevokedTokenRepository revokedTokenRepository) {
        this.revokedTokenRepository = revokedTokenRepository;
    }

    public boolean isTokenRevoked(String token) {
        return revokedTokenRepository.findByToken(token).isPresent();
    }






}
