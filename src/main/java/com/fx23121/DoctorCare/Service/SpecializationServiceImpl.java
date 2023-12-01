package com.fx23121.DoctorCare.Service;

import com.fx23121.DoctorCare.Entity.Specialization;
import com.fx23121.DoctorCare.Repository.SpecializationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SpecializationServiceImpl implements SpecializationService {

    @Autowired
    private SpecializationRepository specializationRepository;
    @Override
    public Page<Specialization> getTopSpecialization(Pageable pageable) {
        return specializationRepository.findAll(pageable);
    }

    @Override
    public Page<Specialization> searchSpecialization(String keyword, Pageable pageable) {
        return specializationRepository.findByNameContaining(keyword, pageable);
    }
}
