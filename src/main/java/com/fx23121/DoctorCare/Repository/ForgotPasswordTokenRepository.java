package com.fx23121.DoctorCare.Repository;

import com.fx23121.DoctorCare.Entity.ForgotPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ForgotPasswordTokenRepository extends JpaRepository<ForgotPasswordToken, Integer> {
    Optional<ForgotPasswordToken> findByToken(String token);
}
