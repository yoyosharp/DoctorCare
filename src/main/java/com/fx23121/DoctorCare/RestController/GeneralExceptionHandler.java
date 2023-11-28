package com.fx23121.DoctorCare.RestController;

import com.fx23121.DoctorCare.Exception.JwtAuthenticationException;
import com.fx23121.DoctorCare.Response.FieldValidateError;
import com.fx23121.DoctorCare.Response.JwtResponse;
import com.fx23121.DoctorCare.Response.SeverErrorResponse;
import com.fx23121.DoctorCare.Response.UserNotFoundError;
import com.fx23121.DoctorCare.Exception.FieldValidateException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GeneralExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<UserNotFoundError> userNotFound(UsernameNotFoundException unf) {
        int status = HttpStatus.NOT_FOUND.value();
        String message = unf.getMessage();
        long timeStamp = System.currentTimeMillis();

        UserNotFoundError error = new UserNotFoundError(status, message, timeStamp);

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<FieldValidateError> registerError(FieldValidateException rge) {
        int status = HttpStatus.BAD_REQUEST.value();
        String message = rge.getMessage();
        long timeStamp = System.currentTimeMillis();

        FieldValidateError error = new FieldValidateError(status, message, timeStamp);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<SeverErrorResponse> severError(Exception e) {
        int status = HttpStatus.INTERNAL_SERVER_ERROR.value();
        String message = e.getMessage();
        long timeStamp = System.currentTimeMillis();

        SeverErrorResponse error = new SeverErrorResponse(status, message, timeStamp);
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
