package com.fx23121.DoctorCare.Response;

public class InvalidPasswordResetError extends AbstractErrorResponse{
    public InvalidPasswordResetError(int httpStatus, String message, Long timeStamp) {
        super(httpStatus, message, timeStamp);
    }
}
