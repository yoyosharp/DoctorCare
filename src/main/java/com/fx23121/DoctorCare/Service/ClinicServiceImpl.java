package com.fx23121.DoctorCare.Service;

import com.fx23121.DoctorCare.Entity.Clinic;
import com.fx23121.DoctorCare.Repository.ClinicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ClinicServiceImpl implements ClinicService {
    @Autowired
    private ClinicRepository clinicRepository;

    @Override
    public Page<Clinic> getTopClinic(Pageable pageable) {
        return clinicRepository.findAll(pageable);
    }

    @Override
    public Page<Clinic> searchClinic(String keyword, Pageable pageable) {
        return clinicRepository.findByNameContaining(keyword, pageable);
    }
}
