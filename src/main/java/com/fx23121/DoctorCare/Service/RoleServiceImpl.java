package com.fx23121.DoctorCare.Service;

import com.fx23121.DoctorCare.Entity.Role;
import com.fx23121.DoctorCare.Repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Role getRole(int roleId) {
        return roleRepository.findById(roleId).orElseThrow(() -> new RuntimeException("Exception when find role by id"));
    }

    @Override
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }
}
