package com.fx23121.DoctorCare.RestController;

import com.fx23121.DoctorCare.Entity.Booking;
import com.fx23121.DoctorCare.Model.BookingInfoDTO;
import com.fx23121.DoctorCare.Model.UserInfo;
import com.fx23121.DoctorCare.Service.BookingService;
import com.fx23121.DoctorCare.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private BookingService bookingService;

    @GetMapping("/demo")
    public String sayHello() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        System.out.println(auth.getPrincipal());

        return "User authority";
    }

    @GetMapping("/personalInfo")
    public ResponseEntity<UserInfo> getUserInfo() {
        UserInfo userInfo = userService.getUserInfo();

        return new ResponseEntity<>(userInfo, HttpStatus.OK);
    }

    @PostMapping("/bookNewAppointment")
    public ResponseEntity<?> bookAppointment(@RequestBody BookingInfoDTO bookingInfoDTO) {

        Booking addedBooking = bookingService.bookNewAppointment(bookingInfoDTO);

        if (addedBooking != null) return new ResponseEntity<>(addedBooking.getId(), HttpStatus.OK);
        else return new ResponseEntity<>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
