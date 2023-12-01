package com.fx23121.DoctorCare.Service;

import com.fx23121.DoctorCare.Entity.Booking;
import com.fx23121.DoctorCare.Entity.Post;
import com.fx23121.DoctorCare.Entity.TimeSlot;
import com.fx23121.DoctorCare.Entity.User;
import com.fx23121.DoctorCare.Exception.BookingException;
import com.fx23121.DoctorCare.Model.BookingInfoDTO;
import com.fx23121.DoctorCare.Repository.BookingRepository;
import com.fx23121.DoctorCare.Repository.PostRepository;
import com.fx23121.DoctorCare.Repository.TimeSlotRepository;
import com.fx23121.DoctorCare.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService {
    @Value("${booking.max-booking-for-post-at-the-same-time-slot}")
    private int maxPostBookingAtTimeSlot;

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private TimeSlotRepository timeSlotRepository;

    @Override
    @Transactional
    public Booking bookNewAppointment(BookingInfoDTO bookingInfoDTO) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userRepository.findByEmail(auth.getName()).orElseThrow(() -> new UsernameNotFoundException("Could not authenticate the user"));

        //check if user already booked the same post at the same time
        Optional<Booking> result = bookingRepository.findUserBooking(currentUser.getId(), bookingInfoDTO.getPostId(), bookingInfoDTO.getTimeSlotId());
        if (result.isPresent()) throw new BookingException("User has already booked an appointment for current post at the same time");
        //check if too many booking for the same post at the same time
        long currentPostBookedAtTimeSlot = bookingRepository.getPostBookingAtTimeSlotCount(bookingInfoDTO.getPostId(), bookingInfoDTO.getTimeSlotId());
        if (currentPostBookedAtTimeSlot >= maxPostBookingAtTimeSlot) throw new BookingException("Booking queue for current post at this time has full already");

        //load current Post
        Post currentPost = postRepository.findById(bookingInfoDTO.getPostId());
        if (currentPost == null) throw new BookingException("Cannot parse current post");
        //load the timeSlot
        TimeSlot timeSlot = timeSlotRepository.findById(bookingInfoDTO.getTimeSlotId())
                .orElseThrow(() -> new BookingException("Cannot parse current time slot"));
        //check if the timeslot is available for the current post
        List<Integer> postTimeSlotIdList = currentPost.getTimeSlots().stream()
                .map(postTimeSlot -> postTimeSlot.getId()).toList();
        if (!postTimeSlotIdList.contains(timeSlot.getId())) throw new BookingException("Current time slot is not available for current post");

        Booking newBooking = new Booking();
        newBooking.setUser(currentUser);
        newBooking.setPost(currentPost);
        newBooking.setTimeSlot(timeSlot);
        newBooking.setDescription(bookingInfoDTO.getDescription());
        newBooking.setStatus(0);

        currentPost.setBooked(currentPost.getBooked() + 1);
        currentPost.getClinic().setBooked(currentPost.getClinic().getBooked() + 1);
        currentPost.getSpecialization().setBooked(currentPost.getSpecialization().getBooked() + 1);

        postRepository.save(currentPost);

        return bookingRepository.save(newBooking);
    }
}
