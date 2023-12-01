package com.fx23121.DoctorCare.Repository;

import com.fx23121.DoctorCare.Entity.Clinic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClinicRepository extends JpaRepository<Clinic, Integer> {

    Page<Clinic> findByNameContaining(String keyword, Pageable pageable);
}
