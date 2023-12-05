package com.fx23121.DoctorCare.Model;

import com.fx23121.DoctorCare.Entity.Booking;
import com.fx23121.DoctorCare.Entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserInfo {

    List<Booking> bookingList;
    private User user;
}
