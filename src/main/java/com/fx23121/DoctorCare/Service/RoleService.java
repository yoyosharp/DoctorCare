package com.fx23121.DoctorCare.Service;

import com.fx23121.DoctorCare.Entity.Role;

import java.util.List;

public interface RoleService {
    Role getRole(int roleId);

    List<Role> getRoles();
}
