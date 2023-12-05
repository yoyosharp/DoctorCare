package com.fx23121.DoctorCare.Repository;

import com.fx23121.DoctorCare.Entity.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    @Query("FROM Booking WHERE user.id = :userId")
    List<Booking> getUserBookingList(@Param("userId") int userId);

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.post.id = :postId AND b.timeSlot.id = :timeSlotId AND date = :date")
    long getPostBookingAtTimeSlotCount(@Param("postId") int postId,
                                       @Param("timeSlotId") int timeSlotId,
                                       @Param("date") Date date);

    @Query("FROM Booking WHERE user.id = :userId AND post.id = :postId AND timeSlot.id = :timeSlotId AND date = :date")
    Optional<Booking> findUserBooking(@Param("userId") int userId,
                                      @Param("postId") int postId,
                                      @Param("timeSlotId") int timeSlotId,
                                      @Param("date") Date date);

    @Query("FROM Booking WHERE post.doctor.id = :doctorId")
    Page<Booking> findDoctorBookingList(@Param("doctorId") int doctorId, Pageable pageable);
}
