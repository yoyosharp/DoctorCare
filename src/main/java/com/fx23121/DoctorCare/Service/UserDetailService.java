package com.fx23121.DoctorCare.Service;

import com.fx23121.DoctorCare.Entity.User;
import com.fx23121.DoctorCare.Repository.UserRepository;
import jakarta.persistence.NoResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
@Service
public class UserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<User> result = userRepository.getUserByEmail(email);
        if (result.isEmpty()) throw new UsernameNotFoundException("Địa chỉ email chưa đăng ký trên hệ thống");

        return org.springframework.security.core.userdetails.User.withUserDetails(result.get()).build();
    }
}
