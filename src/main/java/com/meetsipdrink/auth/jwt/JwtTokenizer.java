package com.meetsipdrink.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
public class JwtTokenizer {

    // RedisTemplate을 사용하여 Redis와 상호작용
    private final RedisTemplate<String, Object> redisTemplate;

    // 생성자에서 RedisTemplate을 주입받아 초기화
    public JwtTokenizer(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Getter
    @Value("${jwt.key}")
    private String secretKey;

    @Getter
    @Value("${jwt.access-token-expiration-minutes}")
    private int accessTokenExpirationMinutes;

    @Getter
    @Value("${jwt.refresh-token-expiration-minutes}")
    private int refreshTokenExpirationMinutes;


    public String encodedBase64SecretKey(String secretKey) {
        return Encoders.BASE64.encode(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    // Access Token 생성 후 Redis에 저장
    public String generateAccessToken(Map<String, Object> claims,
                                      String subject,
                                      Date expiration,
                                      String base64EncodedSecretKey) {
        Key key = getKeyFromBase64EncodedKey(base64EncodedSecretKey);

        String accessToken = Jwts.builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(Calendar.getInstance().getTime())
            .setExpiration(expiration)
            .signWith(key)
            .compact();

        // Redis에 accessToken 저장
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        valueOperations.set((String) claims.get("username"), accessToken, accessTokenExpirationMinutes, TimeUnit.MINUTES);
        return accessToken;
    }

    // Refresh Token 생성 후 Redis에 저장
    public String generateRefreshToken(String subject, Date expiration, String base64EncodedSecretKey, String accessToken) {
        Key key = getKeyFromBase64EncodedKey(base64EncodedSecretKey);

        String refreshToken = Jwts.builder()
            .setSubject(subject)
            .setIssuedAt(Calendar.getInstance().getTime())
            .setExpiration(expiration)
            .signWith(key)
            .compact();

        // Redis에 refreshToken 저장
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(accessToken, refreshToken, refreshTokenExpirationMinutes, TimeUnit.MINUTES);

        return refreshToken;
    }

    // JWT 토큰에서 Claims 반환
    public Jws<Claims> getClaims(String jws, String base64EncodedSecretKey) {
        Key key = getKeyFromBase64EncodedKey(base64EncodedSecretKey);

        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(jws);
    }

    // JWT 토큰 검증
    public void verifySignature(String jws, String base64EncodedSecretKey) {
        Key key = getKeyFromBase64EncodedKey(base64EncodedSecretKey);

        Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(jws);
    }

    // 토큰 만료 시간 설정
    public Date getTokenExpiration(int expirationMinutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, expirationMinutes);

        return calendar.getTime();
    }

    // Base64로 인코딩된 secretKey로 HMAC SHA 키 생성
    private Key getKeyFromBase64EncodedKey(String base64EncodedSecretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(base64EncodedSecretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Redis에서 Access Token 및 Refresh Token 삭제 (로그아웃 시 사용)
    public boolean deleteRegisterToken(String username) {
        return Optional.ofNullable(redisTemplate.hasKey(username))
            .filter(Boolean::booleanValue)
            .map(hasKey -> {
                String accessToken = (String) redisTemplate.opsForValue().get(username);
                redisTemplate.delete(accessToken);
                redisTemplate.delete(username);
                return true;
            })
            .orElse(false);
    }
}