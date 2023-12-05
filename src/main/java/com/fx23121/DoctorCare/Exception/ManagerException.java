package com.fx23121.DoctorCare.Exception;

public class ManagerException extends RuntimeException{
    public ManagerException(String message) {
        super(message);
    }

    public ManagerException(String message, Throwable cause) {
        super(message, cause);
    }

    public ManagerException(Throwable cause) {
        super(cause);
    }
}
