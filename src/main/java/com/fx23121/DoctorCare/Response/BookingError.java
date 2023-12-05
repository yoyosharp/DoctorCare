package com.fx23121.DoctorCare.Response;

public class BookingError extends AbstractErrorResponse {
    public BookingError(int httpStatus, String message, Long timeStamp) {
        super(httpStatus, message, timeStamp);
    }
}
