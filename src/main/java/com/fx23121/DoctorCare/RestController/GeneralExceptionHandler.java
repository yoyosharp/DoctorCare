package com.fx23121.DoctorCare.RestController;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GeneralExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<String> userNotFound(UsernameNotFoundException unf) {
        return new ResponseEntity<>(unf.getMessage(), HttpStatus.NOT_FOUND);
    }
}
