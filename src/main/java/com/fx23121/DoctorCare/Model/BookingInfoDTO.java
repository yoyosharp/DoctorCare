package com.fx23121.DoctorCare.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingInfoDTO {

    private int postId;

    private int timeSlotId;

    private String description;
}
