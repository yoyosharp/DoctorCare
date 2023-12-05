package com.fx23121.DoctorCare.Response;

public class SeverError extends AbstractErrorResponse{

    public SeverError(int httpStatus, String message, Long timeStamp) {
        super(httpStatus, message, timeStamp);
    }
}
