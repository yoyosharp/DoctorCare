package com.fx23121.DoctorCare.Response;

public class UserNotFoundError extends AbstractErrorResponse {
    public UserNotFoundError(int httpStatus, String message, Long timeStamp) {
        super(httpStatus, message, timeStamp);
    }
}
