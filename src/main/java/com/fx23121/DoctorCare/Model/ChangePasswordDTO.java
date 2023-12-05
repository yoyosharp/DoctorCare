package com.fx23121.DoctorCare.Model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChangePasswordDTO {
    private String token;
    private String password;
    private String verifyPassword;
}
