package com.fx23121.DoctorCare.Service;

import com.fx23121.DoctorCare.Entity.Role;
import com.fx23121.DoctorCare.Entity.User;
import com.fx23121.DoctorCare.Exception.FieldValidateException;
import com.fx23121.DoctorCare.Model.ChangePasswordDTO;
import com.fx23121.DoctorCare.Model.LoginDTO;
import com.fx23121.DoctorCare.Model.UserModel;
import com.fx23121.DoctorCare.Repository.RoleRepository;
import com.fx23121.DoctorCare.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService{

    private final String EMAIL_REGEX = "^[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$";
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;


    @Override
    public User addUser(UserModel userModel) {

        //Validate field data
        if (userModel.getName().isEmpty()) throw new FieldValidateException("Please enter name");

        String email = userModel.getEmail();
        if (email.isEmpty()) throw new FieldValidateException("Please enter email");
        if (userRepository.existsByEmail(email)) throw new FieldValidateException("Email already registered");
        if (!Pattern.matches(EMAIL_REGEX, email)) throw new FieldValidateException("Not a valid email address");

        if (userModel.getAddress().isEmpty()) throw new FieldValidateException("Please enter address");
        if (userModel.getPhone().isEmpty()) throw new FieldValidateException("Please enter phone number");
        if (userModel.getGender().isEmpty()) throw new FieldValidateException("Please select a gender");

        //Password validation
        if (userModel.getPassword().isEmpty()) throw new FieldValidateException("Please enter password");
        if (!userModel.getPassword().equals(userModel.getVerifyPassword())) throw new FieldValidateException("Verify password does not match");

        //All validate then add and return the user
        User newUser = new User();
        newUser.setName(userModel.getName());
        newUser.setEmail(userModel.getEmail());
        newUser.setPassword(passwordEncoder.encode(userModel.getPassword()));
        newUser.setAddress(userModel.getAddress());
        newUser.setPhone(userModel.getPhone());
        newUser.setGender(userModel.getGender());
        newUser.setCreateAt(Date.valueOf(LocalDate.now()));
        newUser.setAvatar("/image/anonymous-user");

        Role role_user = roleRepository.findById(2)
                .orElseThrow(() -> new RuntimeException("Cannot find role"));
        Set<Role> roles = new HashSet<>();
        roles.add(role_user);
        newUser.setRoles(roles);

        newUser.setEnabled(1);
        newUser.setNotLocked(1);

        return userRepository.save(newUser);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Could not find the user with email " + email));
    }

    @Override
    public String requestChangePassword(String email) {

        //Find the user in database
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Could not find the user with email " + email));

        return jwtService.generatePasswordResetToken(currentUser.getEmail());
    }

    @Override
    public String userLogin(LoginDTO loginDTO) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(auth);

        return jwtService.generateToken(auth);
    }

    @Override
    public boolean changePassword(ChangePasswordDTO changePasswordDTO) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        User currentUser = userRepository.findByEmail(auth.getName()).
                orElseThrow(() -> new UsernameNotFoundException("Could not find the user with email " + auth.getName()));

        if (!changePasswordDTO.getVerifyPassword().equals(changePasswordDTO.getPassword())) throw new FieldValidateException("Verify password does not match");

        currentUser.setPassword(passwordEncoder.encode(changePasswordDTO.getPassword()));
        userRepository.save(currentUser);

        return true;
    }


}
