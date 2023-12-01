package com.fx23121.DoctorCare.Service;

import com.fx23121.DoctorCare.Entity.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DoctorService {
    Page<Doctor> searchDoctor(String keyword, Pageable pageable);

}
