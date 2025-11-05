package com.buynow.config;


import com.buynow.entity.User;
import com.buynow.repository.UserRepository;
import com.buynow.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
public class JwtFilter extends OncePerRequestFilter {


    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;


    public JwtFilter(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // Check if the Authorization header is present and starts with "Bearer "
        String authorization = request.getHeader("Authorization");


        if (authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.substring(7); // Remove the "Bearer " part
            String username = jwtUtil.getUsername(token); // Get the username from the token
            String role = jwtUtil.getRole(token);
            Optional<User> byUsername = userRepository.findByEmail(username);
            if(byUsername.isPresent()){
                User user = byUsername.get();
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(user,null,
                                List.of(new SimpleGrantedAuthority("ROLE_" + role)));
                authentication.setDetails(new WebAuthenticationDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        }

        // Proceed with the filter chain
        filterChain.doFilter(request, response);
    }
}
