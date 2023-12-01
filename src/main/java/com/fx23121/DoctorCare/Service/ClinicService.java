package com.fx23121.DoctorCare.Service;

import com.fx23121.DoctorCare.Entity.Clinic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ClinicService {
    Page<Clinic> getTopClinic(Pageable pageable);

    Page<Clinic> searchClinic(String keyword, Pageable pageable);
}
