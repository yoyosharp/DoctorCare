package com.fx23121.DoctorCare.Service;

import com.fx23121.DoctorCare.Entity.Doctor;
import com.fx23121.DoctorCare.Entity.User;
import com.fx23121.DoctorCare.Model.*;

public interface UserService {
    User addUser(UserModel userModel);

    User findUserByEmail(String email);

    String requestChangePassword(String email);

    String userLogin(LoginDTO loginDTO);

    boolean changePassword(ChangePasswordDTO changePasswordDTO);

    UserInfo getUserInfo();

    PatientInfo getPatientInfo(int userId);

    Doctor addDoctor(DoctorModel doctorModel);

    User lockUser(int userId, String note);

    Doctor lockDoctor(int doctorId, String note);

    void unlockUser(int userId);

    void unlockDoctor(int doctorId);
}
