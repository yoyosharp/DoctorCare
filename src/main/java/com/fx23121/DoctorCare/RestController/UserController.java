package com.fx23121.DoctorCare.RestController;

import com.fx23121.DoctorCare.Entity.User;
import com.fx23121.DoctorCare.Service.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserDetailService userDetailService;

    @GetMapping("/demo")
    public String sayHello() {
        return "Hello";
    }

}
