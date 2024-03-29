package com.fx23121.DoctorCare.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingInfoDTO {

    private int postId;

    private int timeSlotId;

    private String description;

    private Date date;

}
