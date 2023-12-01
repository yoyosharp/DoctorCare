package com.fx23121.DoctorCare.Repository;

import com.fx23121.DoctorCare.Entity.Specialization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecializationRepository extends JpaRepository<Specialization, Integer> {
    Page<Specialization> findByNameContaining(String keyword, Pageable pageable);
}
