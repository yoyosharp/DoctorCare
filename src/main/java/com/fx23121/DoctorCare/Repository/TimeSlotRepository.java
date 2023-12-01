package com.fx23121.DoctorCare.Repository;

import com.fx23121.DoctorCare.Entity.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeSlotRepository extends JpaRepository<TimeSlot, Integer> {

}
