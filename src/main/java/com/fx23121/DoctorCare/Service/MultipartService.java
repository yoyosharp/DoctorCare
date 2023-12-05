package com.fx23121.DoctorCare.Service;

import org.springframework.web.multipart.MultipartFile;

public interface MultipartService {
    String uploadFile(MultipartFile file);
}
