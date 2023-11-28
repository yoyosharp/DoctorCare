package com.fx23121.DoctorCare.Service;

import com.fx23121.DoctorCare.Entity.User;
import com.fx23121.DoctorCare.Model.ChangePasswordDTO;
import com.fx23121.DoctorCare.Model.LoginDTO;
import com.fx23121.DoctorCare.Model.UserModel;

public interface UserService {
    User addUser(UserModel userModel);

    User findUserByEmail(String email);

    String requestChangePassword(String email);

    String userLogin(LoginDTO loginDTO);

    boolean changePassword(ChangePasswordDTO changePasswordDTO);
}
