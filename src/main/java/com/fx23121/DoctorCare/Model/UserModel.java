package com.fx23121.DoctorCare.Model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserModel {

    private String name;

    private String email;

    private String password;

    private String verifyPassword;

    private String address;

    private String phone;

    private String gender;
}
