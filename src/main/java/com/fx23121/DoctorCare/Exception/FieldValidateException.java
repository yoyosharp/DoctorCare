package com.fx23121.DoctorCare.Exception;

public class FieldValidateException extends RuntimeException{
    public FieldValidateException(String message) {
        super(message);
    }

    public FieldValidateException(String message, Throwable cause) {
        super(message, cause);
    }

    public FieldValidateException(Throwable cause) {
        super(cause);
    }
}
