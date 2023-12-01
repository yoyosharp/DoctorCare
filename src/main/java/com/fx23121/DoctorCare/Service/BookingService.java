package com.fx23121.DoctorCare.Service;

import com.fx23121.DoctorCare.Entity.Booking;
import com.fx23121.DoctorCare.Entity.User;
import com.fx23121.DoctorCare.Model.BookingInfoDTO;

public interface BookingService {
    Booking bookNewAppointment(BookingInfoDTO bookingInfoDTO);
}
