package com.fx23121.DoctorCare.Model;

import com.fx23121.DoctorCare.Entity.Booking;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PatientInfo {
    private String userName;
    private String gender;
    private String address;
    private List<Booking> examinationHistory;
}

