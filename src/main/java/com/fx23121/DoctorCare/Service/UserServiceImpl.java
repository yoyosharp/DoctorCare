package com.fx23121.DoctorCare.Service;

import com.fx23121.DoctorCare.Entity.*;
import com.fx23121.DoctorCare.Exception.FieldValidateException;
import com.fx23121.DoctorCare.Exception.InvalidPasswordResetException;
import com.fx23121.DoctorCare.Exception.ManagerException;
import com.fx23121.DoctorCare.Exception.UserNotFoundException;
import com.fx23121.DoctorCare.Model.*;
import com.fx23121.DoctorCare.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final String EMAIL_REGEX = "^[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$";

    @Value("${password-reset-expire-time-second}")
    private int EXPIRE_TIME;
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
    @Autowired
    private ForgotPasswordTokenRepository forgotPasswordTokenRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private SpecializationRepository specializationRepository;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private ClinicRepository clinicRepository;

    @Override
    public User addUser(UserModel userModel) {

        //verify input fields -> create new User -> save
        if (verifyUserInfo(userModel)) {

            User newUser = createNewUser(userModel);

            return userRepository.save(newUser);
        }

        return null;
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Could not find the user with email " + email));
    }

    @Override
    public String userLogin(LoginDTO loginDTO) {
        //check if the user email existed
        if (!userRepository.existsByEmail(loginDTO.getEmail())) throw new UserNotFoundException("Email not registered");

        //try to authenticate the user with email and password
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));
        //authenticate successfully -> set authentication for SecurityContextHolder
        SecurityContextHolder.getContext().setAuthentication(auth);
        //return generated jwt token
        return jwtService.generateToken(auth);
    }

    private boolean verifyUserInfo(UserModel userModel) {

        //Validate fields data
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

        return true;
    }

    private User createNewUser(UserModel userModel){
        User newUser = new User();
        newUser.setName(userModel.getName());
        newUser.setEmail(userModel.getEmail());
        newUser.setPassword(passwordEncoder.encode(userModel.getPassword()));
        newUser.setAddress(userModel.getAddress());
        newUser.setPhone(userModel.getPhone());
        newUser.setGender(userModel.getGender());
        newUser.setCreateAt(Date.valueOf(LocalDate.now()));
        newUser.setAvatar("/image/anonymous-user");

        //create new role set
        Role role_user = roleRepository.findById(2)
                .orElseThrow(() -> new RuntimeException("Cannot find role"));
        Set<Role> roles = new HashSet<>();
        roles.add(role_user);
        newUser.setRoles(roles);

        newUser.setEnabled(1);
        newUser.setNonLocked(1);

        return newUser;
    }

    //get current login user from SecurityContext
    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Could not find the user with email " + auth.getName()));
    }

    @Override
    public String requestChangePassword(String email) {

        //Find the user in database
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Could not find the user with email " + email));

        //generate forgot password token and save to the database
        String token = UUID.randomUUID().toString();
        ForgotPasswordToken forgotPasswordToken = new ForgotPasswordToken();
        forgotPasswordToken.setUser(currentUser);
        forgotPasswordToken.setToken(token);
        forgotPasswordToken.setExpiredTime(LocalDateTime.now().plusSeconds(EXPIRE_TIME));

        forgotPasswordTokenRepository.save(forgotPasswordToken);

        return token;
    }

    @Override
    public boolean changePassword(ChangePasswordDTO changePasswordDTO) {
        //validate password and verify password field
        if (!changePasswordDTO.getVerifyPassword().equals(changePasswordDTO.getPassword())) throw new InvalidPasswordResetException("Verify password does not match");
        //validate token
        ForgotPasswordToken token = forgotPasswordTokenRepository.findByToken(changePasswordDTO.getToken())
                .orElseThrow(() -> new InvalidPasswordResetException("Invalid token"));

        if (LocalDateTime.now().isAfter(token.getExpiredTime())) throw new InvalidPasswordResetException("Request token expired");

        //all validated, change password as per request
        User currentUser = token.getUser();

        currentUser.setPassword(passwordEncoder.encode(changePasswordDTO.getPassword()));
        userRepository.save(currentUser);

        return true;
    }

    @Override
    public UserInfo getUserInfo() {

        User currentUser = getCurrentUser();

        List<Booking> bookingList = bookingRepository.getUserBookingList(currentUser.getId());

        return new UserInfo(currentUser, bookingList);
    }

    @Override
    public PatientInfo getPatientInfo(int userId) {
        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Could not find the user"));

        List<Booking> bookingList = bookingRepository.getUserBookingList(userId);

        return new PatientInfo(currentUser.getName(), currentUser.getGender(), currentUser.getAddress(), bookingList);
    }

    @Override
    public Doctor addDoctor(DoctorModel doctorModel) {
        //create user from doctor model
        UserModel userModel = new UserModel(doctorModel.getName(), doctorModel.getEmail(), doctorModel.getPassword(),
                doctorModel.getVerifyPassword(), doctorModel.getAddress(), doctorModel.getPhone(), doctorModel.getGender());

        Specialization specialization = specializationRepository.findById(doctorModel.getSpecializationId())
                .orElseThrow(() -> new FieldValidateException("Cannot find selected specialization"));

        Clinic clinic = clinicRepository.findById(doctorModel.getClinicId())
                .orElseThrow(() -> new FieldValidateException("Cannot find selected clinic"));

        Role doctorRole = roleRepository.findById(3).orElseThrow(() -> new RuntimeException("Cannot find role"));

        if (verifyUserInfo(userModel)) {
            User newUser = createNewUser(userModel);

            newUser.getRoles().add(doctorRole);
            newUser = userRepository.save(newUser);

            Doctor newDoctor = new Doctor();
            newDoctor.setUser(newUser);
            newDoctor.setClinic(clinic);
            newDoctor.setSpecialization(specialization);
            newDoctor.setEducation(doctorModel.getEducation());
            newDoctor.setAchievement(doctorModel.getAchievement());
            newDoctor.setDescription(doctorModel.getDescription());

            return doctorRepository.save(newDoctor);
        }
        return null;
    }

    @Override
    public User lockUser(int userId, String note) {

        //current user and to be locked user cannot be the same
        User currentUser = getCurrentUser();
        if (currentUser.getId() == userId) throw new ManagerException("Cannot lock yourself");
        //validate if to be locked user existed
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Cannot find the user"));
        //lock the user
        if (user.getNonLocked() != 1) throw new ManagerException("Can only lock user with non-locked status");
        user.setNonLocked(0);
        user.setLockDetail(note);

        return userRepository.save(user);
    }

    @Override
    public Doctor lockDoctor(int doctorId, String note) {
        //validate to be locked doctor existed
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ManagerException("Cannot find the doctor"));

        if (doctor.getUser().getNonLocked() != 1) throw new ManagerException("Can only lock user with non-locked status");
        doctor.getUser().setNonLocked(0);
        doctor.getUser().setLockDetail(note);

        return doctorRepository.save(doctor);
    }

    @Override
    public void unlockUser(int userId) {
        //validate to be unlocked user existed
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Cannot find the user"));

        if (user.getNonLocked() != 0) throw new ManagerException("Can only unlock user with locked status");
        user.setNonLocked(1);

        userRepository.save(user);
    }

    @Override
    public void unlockDoctor(int doctorId) {
        //validate to be unlocked doctor existed
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ManagerException("Cannot find the doctor"));

        if (doctor.getUser().getNonLocked() != 0) throw new ManagerException("Can only unlock user with locked status");
        doctor.getUser().setNonLocked(0);
    }

}
