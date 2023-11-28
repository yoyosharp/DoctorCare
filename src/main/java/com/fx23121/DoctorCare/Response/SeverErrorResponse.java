package com.fx23121.DoctorCare.Response;

public class SeverErrorResponse extends AbstractErrorResponse{

    public SeverErrorResponse(int httpStatus, String message, Long timeStamp) {
        super(httpStatus, message, timeStamp);
    }
}
