package com.fx23121.DoctorCare.Response;


public class FieldValidateError extends AbstractErrorResponse {
    public FieldValidateError(int httpStatus, String message, Long timeStamp) {
        super(httpStatus, message, timeStamp);
    }
}
