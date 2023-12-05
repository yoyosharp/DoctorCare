package com.fx23121.DoctorCare.Service;

import com.fx23121.DoctorCare.Entity.*;
import com.fx23121.DoctorCare.Exception.BookingException;
import com.fx23121.DoctorCare.Exception.UserNotFoundException;
import com.fx23121.DoctorCare.Model.BookingInfoDTO;
import com.fx23121.DoctorCare.Model.ReviewBookingDTO;
import com.fx23121.DoctorCare.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
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
    private DoctorRepository doctorRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private TimeSlotRepository timeSlotRepository;

    @Override
    @Transactional
    public Booking bookNewAppointment(BookingInfoDTO bookingInfoDTO) {

        User currentUser = getCurrentUser();
        //check if user already booked the same post at the same time
        Optional<Booking> result = bookingRepository.findUserBooking(currentUser.getId(), bookingInfoDTO.getPostId(), bookingInfoDTO.getTimeSlotId(), bookingInfoDTO.getDate());
        if (result.isPresent())
            throw new BookingException("User has already booked an appointment for current post at the same time");
        //check if too many booking for the same post at the same time
        long currentPostBookedAtTimeSlot = bookingRepository.getPostBookingAtTimeSlotCount(bookingInfoDTO.getPostId(), bookingInfoDTO.getTimeSlotId(), bookingInfoDTO.getDate());
        if (currentPostBookedAtTimeSlot >= maxPostBookingAtTimeSlot)
            throw new BookingException("Booking queue for current post at this time has full already");

        //load current Post
        Post currentPost = postRepository.findById(bookingInfoDTO.getPostId());
        if (currentPost == null) throw new BookingException("Cannot parse current post");
        //load the timeSlot
        TimeSlot timeSlot = timeSlotRepository.findById(bookingInfoDTO.getTimeSlotId())
                .orElseThrow(() -> new BookingException("Cannot parse current time slot"));
        //check if the timeslot is available for the current post
        List<Integer> postTimeSlotIdList = currentPost.getTimeSlots().stream()
                .map(postTimeSlot -> postTimeSlot.getId()).toList();
        if (!postTimeSlotIdList.contains(timeSlot.getId()))
            throw new BookingException("Current time slot is not available for current post");

        Booking newBooking = new Booking();
        newBooking.setUser(currentUser);
        newBooking.setPost(currentPost);
        newBooking.setTimeSlot(timeSlot);
        newBooking.setDescription(bookingInfoDTO.getDescription());
        newBooking.setDate(Date.valueOf(LocalDate.now()));
        newBooking.setStatus(0);

        currentPost.setBooked(currentPost.getBooked() + 1);
        currentPost.getClinic().setBooked(currentPost.getClinic().getBooked() + 1);
        currentPost.getSpecialization().setBooked(currentPost.getSpecialization().getBooked() + 1);

        postRepository.save(currentPost);

        return bookingRepository.save(newBooking);
    }

    @Override
    public Page<User> getDoctorBookedPatient(Integer pageSize, Integer pageIndex) {

        //get the doctor id from current login user
        User currentUser = getCurrentUser();
        Doctor currentDoctor = doctorRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new UsernameNotFoundException("Could not authorize the user"));

        pageIndex = (pageIndex == null) ? 0 : pageIndex - 1;
        if (pageSize == null) pageSize = 5;
        Pageable pageable = PageRequest.of(pageIndex, pageSize);

        //load the doctor's booking
        Page<Booking> bookings = bookingRepository.findDoctorBookingList(currentDoctor.getId(), pageable);

        //create user list from booking list
        List<User> users = bookings.getContent().stream()
                .map(booking -> booking.getUser()).toList();

        return new PageImpl<>(users, pageable, bookings.getTotalElements());
    }

    @Override
    public boolean reviewBooking(ReviewBookingDTO reviewBookingDTO) {
        //load to be reviewed booking
        Booking currentBooking = bookingRepository.findById(reviewBookingDTO.getBookingId())
                .orElseThrow(() -> new BookingException("Cannot find the booking"));

        //load current doctor form login user
        User currentUser = getCurrentUser();
        Doctor currentDoctor = doctorRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new UsernameNotFoundException("Could not authorize the user"));

        //check if current doctor is associated with the booking
        if (currentBooking.getPost().getDoctor().getId() != currentDoctor.getId())
            throw new BookingException("Wrong authentication");

        //check if the reject reason is empty
        if (reviewBookingDTO.isRejected() && reviewBookingDTO.getNote().isEmpty()) return false;

        currentBooking.setStatus(reviewBookingDTO.isRejected() ? -1 : 1);
        if (currentBooking.getStatus() == -1) currentBooking.setRejectionDetail(reviewBookingDTO.getNote());
        bookingRepository.save(currentBooking);
        return true;
    }

    @Override
    public Booking getBooking(int bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingException("Cannot find the booking"));
    }

    @Override
    public List<Booking> getUserBookingDetail(int userId) {
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Cannot find the user"));
        return bookingRepository.getUserBookingList(userId);
    }

    @Override
    public Page<Booking> getDoctorBookingDetail(int doctorId, Integer pageSize, Integer pageIndex) {
        if (!doctorRepository.existsById(doctorId)) throw new UserNotFoundException("Cannot find the doctor");

        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        return bookingRepository.findDoctorBookingList(doctorId, pageable);
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        return userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Could not authenticate the user"));
    }
}
