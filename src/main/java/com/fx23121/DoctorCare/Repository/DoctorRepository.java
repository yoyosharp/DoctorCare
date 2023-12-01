package com.fx23121.DoctorCare.Repository;

import com.fx23121.DoctorCare.Entity.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Integer> {

    @Query("SELECT d FROM Doctor d JOIN d.user u WHERE u.name LIKE %:keyword%")
    Page<Doctor> findByNameContaining(@Param("keyword") String keyword, Pageable pageable);

}
