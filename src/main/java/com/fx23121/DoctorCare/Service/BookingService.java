package com.fx23121.DoctorCare.Service;

import com.fx23121.DoctorCare.Entity.Booking;
import com.fx23121.DoctorCare.Entity.User;
import com.fx23121.DoctorCare.Model.BookingInfoDTO;
import com.fx23121.DoctorCare.Model.ReviewBookingDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BookingService {
    Booking bookNewAppointment(BookingInfoDTO bookingInfoDTO);

    Page<User> getDoctorBookedPatient(Integer pageSize, Integer pageIndex);

    boolean reviewBooking(ReviewBookingDTO reviewBookingDTO);

    Booking getBooking(int bookingId);

    List<Booking> getUserBookingDetail(int userId);

    Page<Booking> getDoctorBookingDetail(int doctorId, Integer pageSize, Integer pageIndex);
}
