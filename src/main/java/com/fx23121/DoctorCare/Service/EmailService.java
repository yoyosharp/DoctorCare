package com.fx23121.DoctorCare.Service;

import com.fx23121.DoctorCare.Model.EmailDetails;

public interface EmailService {

    void sendEmail(EmailDetails emailDetails);

    void sendEmailWithoutAttachment(EmailDetails emailDetails);
}
