package com.meetsipdrink.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meetsipdrink.auth.dto.LoginDto;
import com.meetsipdrink.auth.jwt.JwtTokenizer;
import com.meetsipdrink.member.entity.Member;
import com.meetsipdrink.member.repository.MemberRepository;
import lombok.SneakyThrows;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.FilterChain;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenizer jwtTokenizer;
    private final MemberRepository memberRepository;


    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtTokenizer jwtTokenizer, MemberRepository memberRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenizer = jwtTokenizer;
        this.memberRepository = memberRepository;
    }

    @Override
    @SneakyThrows
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response){
        ObjectMapper objectMapper = new ObjectMapper();
        LoginDto loginDto = objectMapper
                .readValue(request.getInputStream(), LoginDto.class);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

        request.setAttribute("fcmtoken", loginDto.getFcmtoken());

        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected  void successfulAuthentication(HttpServletRequest request,
                                             HttpServletResponse response,
                                             FilterChain chain,
                                             Authentication authResult) {
        // UserDetails로 캐스팅
        UserDetails userDetails = (UserDetails) authResult.getPrincipal();

        // UserDetails에서 이메일 가져오기
        String email = userDetails.getUsername();

        // 이메일을 사용해 Member 객체를 찾기
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Member not found with email: " + email));

        // FCM 토큰 저장 로직
        String fcmtoken = (String) request.getAttribute("fcmtoken");

        if (fcmtoken != null && !fcmtoken.isEmpty()) {
            member.setFcmToken(fcmtoken);  // FCM 토큰 저장
            memberRepository.save(member);  // FCM 토큰 업데이트
        }

        String accessToken = delegateAccessToken(member);
        String refreshToken = delegateRefreshToken(member, accessToken);
        response.addCookie(createCookie(member.getEmail(), refreshToken));
        response.setHeader("Authorization", "Bearer " + accessToken);
        response.setHeader("Refresh", refreshToken);

        System.out.println("accessToken : " + accessToken);
        System.out.println("refreshToken : " + refreshToken);
    }

    private String delegateAccessToken(Member member){
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", member.getEmail());
        claims.put("roles", member.getRoles());

        String subject = member.getEmail();
        Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getAccessTokenExpirationMinutes());
        String base64encodedSecretKey = jwtTokenizer.encodedBase64SecretKey(jwtTokenizer.getSecretKey());

        String acceessKey = jwtTokenizer.generateAccessToken(claims,
                subject, expiration, base64encodedSecretKey);

        return acceessKey;
    }

    private String delegateRefreshToken(Member member , String accessToken){
        String subject = member.getEmail();
        Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getRefreshTokenExpirationMinutes());
        String base64encodedSecretKey = jwtTokenizer.encodedBase64SecretKey(jwtTokenizer.getSecretKey());

        String refreshKey = jwtTokenizer.generateRefreshToken(
                subject, expiration, base64encodedSecretKey, accessToken);

        return refreshKey;
    }

    private URI createURI(String accessToken, String refreshToken) {
        MultiValueMap<String, String > quertParams = new LinkedMultiValueMap<>();
        quertParams.add("access_token", accessToken);
        quertParams.add("refresh_token", refreshToken);
        return UriComponentsBuilder
                .newInstance()
                .scheme("http")
                .host("127.0.0.1")
                .port(3000)
                .queryParams(quertParams)
                .build()
                .toUri();
    }

    public Cookie createCookie(String userName, String refreshToken) {
        String cookieName = "refreshtoken";
        String cookieValue = refreshToken;
        Cookie cookie = new Cookie(cookieName, cookieValue);
        // 쿠키 속성 설정
        cookie.setHttpOnly(true);  //httponly 옵션 설정
        cookie.setSecure(true); //https 옵션 설정(디폴트 false/요즘은 브라우저에서 거름)
        cookie.setPath("/"); // 모든 곳에서 쿠키열람이 가능하도록 설정
        cookie.setMaxAge(60 * 60 * 24); //쿠키 만료시간 설정
        return cookie;
    }
}
