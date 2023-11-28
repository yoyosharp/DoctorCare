package com.fx23121.DoctorCare.Response;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Data
@RequiredArgsConstructor
public class JwtResponse {
    private String message;

    private Integer id;

    private String token;

    private String type = "Bearer ";

    private Collection<? extends GrantedAuthority> authorities;

    public JwtResponse(String message, Integer id, String token, Collection<? extends GrantedAuthority> authorities) {
        this.message = message;
        this.id = id;
        this.token = token;
        this.authorities = authorities;
    }
}
