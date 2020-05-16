package com.store.demo.security;

import com.store.demo.domain.User;
import com.store.demo.repository.UserRepo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtVerifier extends OncePerRequestFilter {

    private final UserRepo userRepo;

    @Autowired
    public JwtVerifier(UserRepo userRepo) {
        this.userRepo = userRepo;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String header=request.getHeader(HttpHeaders.AUTHORIZATION);

        if(header==null || !header.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }
        String token= header.replace("Bearer ", "");

        try {
            Jws<Claims> jws = Jwts.parserBuilder()
                    .setSigningKey(JwtAuthenticationFilter.getKey())
                    .build()
                    .parseClaimsJws(token);

            String username = jws.getBody().getSubject();

            User user= userRepo.findByUsername(username);

            Authentication authentication= new UsernamePasswordAuthenticationToken(
                    username,
                    user.getPassword(),
                    user.getAuthorities()
            );

            SecurityContextHolder
                    .getContext()
                    .setAuthentication(authentication);
        }catch (JwtException e){
            throw new IllegalStateException(String.format("Token cannot be trusted %s", token));
        }
        filterChain.doFilter(request, response);
    }
}
