package com.fx23121.DoctorCare.Model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReviewBookingDTO {
    private int bookingId;
    private boolean rejected;
    private String note;
}
