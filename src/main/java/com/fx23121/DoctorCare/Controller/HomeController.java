package com.fx23121.DoctorCare.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String showHome() {
        return "index";
    }

    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }
}
