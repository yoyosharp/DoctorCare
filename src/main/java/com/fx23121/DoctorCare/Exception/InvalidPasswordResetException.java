package com.fx23121.DoctorCare.Exception;

public class InvalidPasswordResetException extends RuntimeException {
    public InvalidPasswordResetException(String message) {
        super(message);
    }

    public InvalidPasswordResetException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidPasswordResetException(Throwable cause) {
        super(cause);
    }
}
