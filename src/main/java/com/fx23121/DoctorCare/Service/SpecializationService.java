package com.fx23121.DoctorCare.Service;

import com.fx23121.DoctorCare.Entity.Specialization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SpecializationService {

    Page<Specialization> getTopSpecialization(Pageable pageable);

    Page<Specialization> searchSpecialization(String keyword, Pageable pageable);
}
