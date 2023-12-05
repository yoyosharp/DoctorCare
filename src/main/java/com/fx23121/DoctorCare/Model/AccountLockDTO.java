package com.fx23121.DoctorCare.Model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccountLockDTO {
    private int userId;

    private int doctorId;

    private String note;

}
