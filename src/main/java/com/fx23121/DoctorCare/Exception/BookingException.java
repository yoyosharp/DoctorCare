package com.fx23121.DoctorCare.Exception;

public class BookingException extends RuntimeException{
    public BookingException(String message) {
        super(message);
    }

    public BookingException(String message, Throwable cause) {
        super(message, cause);
    }

    public BookingException(Throwable cause) {
        super(cause);
    }
}
