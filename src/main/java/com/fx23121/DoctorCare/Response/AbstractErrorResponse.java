package com.fx23121.DoctorCare.Response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public abstract class AbstractErrorResponse {
    private int httpStatus;

    private String message;

    private Long timeStamp;
}
