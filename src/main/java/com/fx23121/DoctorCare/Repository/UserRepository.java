package com.fx23121.DoctorCare.Repository;

import com.fx23121.DoctorCare.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query(value = "FROM User WHERE email = :email")
    Optional<User> getUserByEmail(@Param("email") String email);
}
