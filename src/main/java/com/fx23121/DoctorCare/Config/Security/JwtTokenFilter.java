package com.fx23121.DoctorCare.Config.Security;

import com.fx23121.DoctorCare.Service.JwtService;
import com.fx23121.DoctorCare.Service.UserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenFilter.class);

    @Autowired
    private JwtService jwtService;

    @Autowired
    private JwtEntryPoint jwtEntryPoint;

    @Autowired
    private UserDetailService userDetailService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            //extract token from the request
            String token = getJwtToken(request);
            //validate the token
            if (token != null && jwtService.validateToken(token)) {
                String email = jwtService.getUserNameFromJwt(token);
                //load UserDetails from database
                UserDetails userDetails = userDetailService.loadUserByUsername(email);

                UsernamePasswordAuthenticationToken authenticationToken
                        = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
            filterChain.doFilter(request, response);
        } catch (AuthenticationException e) {
            logger.error("Cannot set user authentication -> Message: {}", e.getMessage());
            jwtEntryPoint.commence(request, response, e);
        } catch (Exception e) {
            logger.error("Cannot set user authentication -> Message: {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred");
        }
    }

    private String getJwtToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        return null;
    }

}
