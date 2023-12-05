package com.fx23121.DoctorCare.RestController;

import com.fx23121.DoctorCare.Entity.Booking;
import com.fx23121.DoctorCare.Entity.Doctor;
import com.fx23121.DoctorCare.Entity.User;
import com.fx23121.DoctorCare.Model.AccountLockDTO;
import com.fx23121.DoctorCare.Model.DoctorModel;
import com.fx23121.DoctorCare.Service.BookingService;
import com.fx23121.DoctorCare.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserService userService;
    @Autowired
    private BookingService bookingService;
    
    @GetMapping("/demo")
    public String sayHello() {
        return "Admin authority";
    }

    @PostMapping("/addDoctor")
    public ResponseEntity<String> addDoctor(@RequestBody DoctorModel doctorModel){

        Doctor doctor = userService.addDoctor(doctorModel);

        if (doctor != null) return new ResponseEntity<>("Creating doctor success, doctor ID: " + doctor.getId(), HttpStatus.OK);
        else return new ResponseEntity<>("Failed", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping("/lockUser")
    public ResponseEntity<?> lockUser(@RequestBody AccountLockDTO accountLockDTO) {
        //validate the lock reason is not empty
        if (accountLockDTO.getNote().isEmpty()) return new ResponseEntity<>("Please enter lock reason", HttpStatus.BAD_REQUEST);

        User user = userService.lockUser(accountLockDTO.getUserId(), accountLockDTO.getNote());

        return new ResponseEntity<>("User locked. ID: " + user.getId() + " .Note: " + user.getLockDetail(), HttpStatus.OK);
    }

    @PutMapping("/lockDoctor")
    public ResponseEntity<?> lockDoctor(@RequestBody AccountLockDTO accountLockDTO) {
        //validate the lock reason is not empty
        if (accountLockDTO.getNote().isEmpty()) return new ResponseEntity<>("Please enter lock reason", HttpStatus.BAD_REQUEST);

        Doctor doctor = userService.lockDoctor(accountLockDTO.getDoctorId(), accountLockDTO.getNote());

        return new ResponseEntity<>("User locked. DoctorID: " + doctor.getId() + " .Note: " + doctor.getUser().getLockDetail(), HttpStatus.OK);
    }

    @PutMapping("/unlockUser")
    public ResponseEntity<?> unlockUser(@RequestParam("userId") int userId) {

        userService.unlockUser(userId);

        return new ResponseEntity<>("User unlocked", HttpStatus.OK);
    }

    @PutMapping("/unlockDoctor")
    public ResponseEntity<?> unlockDoctor(@RequestParam("doctorId") int doctorId) {

        userService.unlockDoctor(doctorId);

        return new ResponseEntity<>("User unlocked", HttpStatus.OK);
    }

    @GetMapping("/userBookingDetail")
    public ResponseEntity<?> userBookingDetail(@RequestParam("userId") int userId) {
        List<Booking> userBookingDetail = bookingService.getUserBookingDetail(userId);

        if (userBookingDetail.isEmpty()) return new ResponseEntity<>("User has not book any service yet", HttpStatus.OK);

        return new ResponseEntity<>(userBookingDetail, HttpStatus.OK);
    }

    @GetMapping("/doctorBookingDetail")
    public ResponseEntity<?> doctorBookingDetail(@RequestParam("doctorId") int doctorId,
                                                 @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                 @RequestParam(value = "page", required = false) Integer pageIndex) {
        if (pageSize == null) pageSize = 5;
        pageIndex = pageIndex == null? 0 : pageIndex - 1;

        Page<Booking> doctorBookingDetail = bookingService.getDoctorBookingDetail(doctorId, pageSize, pageIndex);

        if (doctorBookingDetail.getContent().isEmpty()) return new ResponseEntity<>("User has not book any service yet", HttpStatus.OK);

        return new ResponseEntity<>(doctorBookingDetail, HttpStatus.OK);
    }
}
