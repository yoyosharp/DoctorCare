package com.fx23121.DoctorCare.Response;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class JwtResponse {
    private String message;

    private Integer id;

    private String token;


    public JwtResponse(String message, Integer id, String token) {
        this.message = message;
        this.id = id;
        this.token = token;
        ;
    }
}
