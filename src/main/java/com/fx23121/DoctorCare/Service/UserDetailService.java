package com.fx23121.DoctorCare.Service;

import com.fx23121.DoctorCare.Entity.User;
import com.fx23121.DoctorCare.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<User> result = userRepository.findByEmail(email);
        if (result.isEmpty()) throw new UsernameNotFoundException("Email is not registered");

        return org.springframework.security.core.userdetails.User.withUserDetails(result.get()).build();
    }
}
