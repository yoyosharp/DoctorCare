package com.fx23121.DoctorCare.RestController;

import com.fx23121.DoctorCare.Exception.*;
import com.fx23121.DoctorCare.Response.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
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
    public ResponseEntity<FieldValidateError> registerError(FieldValidateException fve) {
        int status = HttpStatus.BAD_REQUEST.value();
        String message = fve.getMessage();
        long timeStamp = System.currentTimeMillis();

        FieldValidateError error = new FieldValidateError(status, message, timeStamp);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<InvalidPasswordResetError> passwordResetError(InvalidPasswordResetException e) {
        int status = HttpStatus.BAD_REQUEST.value();
        String message = e.getMessage();
        long timeStamp = System.currentTimeMillis();

        InvalidPasswordResetError error = new InvalidPasswordResetError(status, message, timeStamp);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<BookingError> bookingError(BookingException e) {
        int status = HttpStatus.BAD_REQUEST.value();
        String message = e.getMessage();
        long timeStamp = System.currentTimeMillis();

        BookingError error = new BookingError(status, message, timeStamp);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<UserNotFoundError> userNotFound(UserNotFoundException unf) {
        int status = HttpStatus.BAD_REQUEST.value();
        String message = unf.getMessage();
        long timeStamp = System.currentTimeMillis();

        UserNotFoundError error = new UserNotFoundError(status, message, timeStamp);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<?> managerException(ManagerException m) {

        return new ResponseEntity<>(m.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<SeverError> severError(Exception e) {
        int status = HttpStatus.INTERNAL_SERVER_ERROR.value();
        String message = e.getMessage();
        long timeStamp = System.currentTimeMillis();

        SeverError error = new SeverError(status, message, timeStamp);
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
