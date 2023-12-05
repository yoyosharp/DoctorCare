package com.fx23121.DoctorCare.RestController;

import com.fx23121.DoctorCare.Entity.Booking;
import com.fx23121.DoctorCare.Entity.User;
import com.fx23121.DoctorCare.Model.EmailDetails;
import com.fx23121.DoctorCare.Model.PatientInfo;
import com.fx23121.DoctorCare.Model.ReviewBookingDTO;
import com.fx23121.DoctorCare.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/doctor")
public class DoctorController {
    @Autowired
    private UserDetailService userDetailService;
    @Autowired
    private UserService userService;
    @Autowired
    private DoctorService doctorService;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private MultipartService multipartService;
    @Autowired
    private EmailService emailService;

    @GetMapping("/demo")
    public String sayHello() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        System.out.println(auth.getPrincipal());

        return "Doctor authority";
    }

    @GetMapping("/patientInfo")
    public ResponseEntity<?> showBookedPatients(@RequestParam("userId") int userId,
                                                @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                @RequestParam(value = "page", required = false) Integer pageIndex) {

        PatientInfo patientInfo = userService.getPatientInfo(userId);

        return new ResponseEntity<>(patientInfo, HttpStatus.OK);
    }

    @PutMapping("/reviewBooking")
    public ResponseEntity<?> reviewBooking(@RequestBody ReviewBookingDTO reviewBookingDTO){
        boolean result =  bookingService.reviewBooking(reviewBookingDTO);

        if (result) return new ResponseEntity<>("Success", HttpStatus.OK);

        else return new ResponseEntity<>("Empty rejection reason", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/uploadFile")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        String path = multipartService.uploadFile(file);
        return new ResponseEntity<>(path, HttpStatus.OK);
    }

    @PostMapping("/sendExaminationResult")
    public ResponseEntity<?> sendExaminationResult(@RequestParam("file") MultipartFile file,
                                                   @RequestParam("bookingId") int bookingId) {
        //upload file and take path to the temp file
        String uploadPath = multipartService.uploadFile(file);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentDoctor = userService.findUserByEmail(auth.getName());
        //validate the doctor that send the result is the one that take the booking
        Booking currentBooking = bookingService.getBooking(bookingId);
        if (currentBooking.getPost().getDoctor().getUser().getId() != currentDoctor.getId())
            return new ResponseEntity<>("Cannot send examination result for booking that is not by current doctor", HttpStatus.BAD_REQUEST);

        //create email details and send the email
        EmailDetails emailDetails = new EmailDetails(currentBooking.getUser().getEmail(),
                "Your examination result for booking No." + currentBooking.getId(),
                "Examination result for booking No." + currentBooking.getId(),
                uploadPath);

        emailService.sendEmail(emailDetails);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }
}
