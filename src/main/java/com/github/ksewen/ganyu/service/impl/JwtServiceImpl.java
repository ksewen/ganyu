package com.github.ksewen.ganyu.service.impl;

import com.github.ksewen.ganyu.configuration.properties.JwtProperties;
import com.github.ksewen.ganyu.model.JwtTokenModel;
import com.github.ksewen.ganyu.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * @author ksewen
 * @date 10.05.2023 12:28
 */
@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

  private final JwtProperties jwtProperties;

  @Override
  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  @Override
  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  @Override
  public JwtTokenModel generateToken(UserDetails userDetails) {
    return generateToken(new HashMap<>(), userDetails);
  }

  @Override
  public JwtTokenModel generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
    return buildToken(extraClaims, userDetails, this.jwtProperties.getExpiration());
  }

  @Override
  public JwtTokenModel generateRefreshToken(UserDetails userDetails) {
    return buildToken(new HashMap<>(), userDetails, this.jwtProperties.getRefreshTokenExpiration());
  }

  @Override
  public boolean validateToken(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
  }

  private JwtTokenModel buildToken(
      Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
    Date now = new Date();
    Date expireAt = new Date(now.getTime() + expiration);
    return JwtTokenModel.builder()
        .token(
            Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(now)
                .setExpiration(expireAt)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact())
        .expireAt(LocalDateTime.ofInstant(expireAt.toInstant(), ZoneId.systemDefault()))
        .build();
  }

  private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  private Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(getSignInKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  private Key getSignInKey() {
    byte[] keyBytes = Decoders.BASE64.decode(this.jwtProperties.getSecretKey());
    return Keys.hmacShaKeyFor(keyBytes);
  }
}
