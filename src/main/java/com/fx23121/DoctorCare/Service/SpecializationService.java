package com.fx23121.DoctorCare.Service;

import com.fx23121.DoctorCare.Entity.Specialization;
import com.fx23121.DoctorCare.Repository.SpecializationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

public interface SpecializationService {

    Page<Specialization> getTopSpecialization(Pageable pageable);

    Page<Specialization> searchSpecialization(String keyword, Pageable pageable);
}
