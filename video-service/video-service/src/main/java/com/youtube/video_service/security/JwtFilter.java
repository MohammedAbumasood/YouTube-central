package com.youtube.video_service.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    JwtUtil jwUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // How we pass the jwtToken
        //bearer 2u92dwwjhwihdvijdihdiuh
        //01234567
        //If token is invalid then we will not set the authentication and we will directly call doFilter
        //If token is valid then we will set the authentication and after that we will call do filter mehtod and it will see authentication is set then will pass the request
        String bToken = request.getHeader("Authorization");
        if(bToken != null && bToken.startsWith("Bearer")){
            String token = bToken.substring(7);
            // validate token -> JwtUtil
            boolean isValid = jwUtil.isValidToken(token);
            if(isValid == false){
                //if token was invalid then i am not setting ny authentication ans returning filterChain from here itSelf
                filterChain.doFilter(request, response);
                return;
            }

            // if token is valid then we are setting authentication for filter chain
            String credentials = jwUtil.decryptToken(token);
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(credentials, null, Collections.emptyList());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        filterChain.doFilter(request, response);
    }
}
