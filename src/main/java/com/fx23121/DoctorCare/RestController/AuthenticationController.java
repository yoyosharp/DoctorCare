package com.fx23121.DoctorCare.RestController;

import com.fx23121.DoctorCare.Entity.User;
import com.fx23121.DoctorCare.Exception.UserNotFoundException;
import com.fx23121.DoctorCare.Model.EmailDetails;
import com.fx23121.DoctorCare.Model.LoginDTO;
import com.fx23121.DoctorCare.Model.UserModel;
import com.fx23121.DoctorCare.Model.ChangePasswordDTO;
import com.fx23121.DoctorCare.Response.JwtResponse;
import com.fx23121.DoctorCare.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthenticationController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserDetailService userDetailService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private EmailService emailService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserModel userModel) {

        User user = userService.addUser(userModel);

        String message = "User created. UserID: " + user.getId();

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> userLogin(@RequestBody LoginDTO loginDTO) {

        try{
            String jwtToken = userService.userLogin(loginDTO);

            User currentUser = userService.findUserByEmail(loginDTO.getEmail());

            return new ResponseEntity<>(new JwtResponse(
                    "Login success", currentUser.getId(), jwtToken), HttpStatus.OK);
        }
        catch (UserNotFoundException e) {
            return new ResponseEntity<>(new JwtResponse(e.getMessage(), null, null), HttpStatus.BAD_REQUEST);
        }
        catch (LockedException e) {
            User currentUser = userService.findUserByEmail(loginDTO.getEmail());
            return new ResponseEntity<>(new JwtResponse("Account is locked, note: " + currentUser.getLockDetail(), null, null), HttpStatus.UNAUTHORIZED);
        }
        catch (AuthenticationException e)
        {
            return new ResponseEntity<>(new JwtResponse(e.getMessage(), null, null), HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/forgetPassword")
    public ResponseEntity<?> forgetPassword(@RequestParam String email) {
        String token = userService.requestChangePassword(email);

        EmailDetails emailDetails = new EmailDetails(email, "Your token: " + token, "Password reset token ", null);

        emailService.sendEmailWithoutAttachment(emailDetails);

        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @PostMapping("/changePassword")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) {

        userService.changePassword(changePasswordDTO);

        return new ResponseEntity<>("Successfully updated password", HttpStatus.OK);
    }
}
