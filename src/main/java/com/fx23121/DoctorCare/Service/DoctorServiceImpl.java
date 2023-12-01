package com.fx23121.DoctorCare.Service;

import com.fx23121.DoctorCare.Entity.Doctor;
import com.fx23121.DoctorCare.Repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorServiceImpl implements DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Override
    public Page<Doctor> searchDoctor(String keyword, Pageable pageable) {
        return doctorRepository.findByNameContaining(keyword, pageable);
    }
}
