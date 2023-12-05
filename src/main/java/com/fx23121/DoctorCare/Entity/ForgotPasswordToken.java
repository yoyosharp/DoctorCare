package com.fx23121.DoctorCare.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "forgot_password_token")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForgotPasswordToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private String token;

    @Column(name = "expired_time")
    private LocalDateTime expiredTime;

}
