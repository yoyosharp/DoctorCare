package com.fx23121.DoctorCare.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class MultipartServiceImpl implements MultipartService {

    private final String UPLOAD_PATH = "/upload/";
    @Autowired
    private ResourceLoader resourceLoader;

    @Override
    public String uploadFile(MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                String folderName = authentication.getName();
                Resource resource = resourceLoader.getResource(UPLOAD_PATH);
                String fileName = file.getOriginalFilename();

                Path uploadPath = Paths.get(resource.getFile().getAbsolutePath()).resolve(folderName);
                System.out.println(uploadPath);
                if (!Files.exists(uploadPath) || !Files.isDirectory(uploadPath))Files.createDirectories(uploadPath);

                Path uploadFile = uploadPath.resolve(fileName).normalize();
                try (InputStream inputStream = file.getInputStream()) {
                    Files.copy(inputStream, uploadFile, StandardCopyOption.REPLACE_EXISTING);
                }

                return uploadFile.toString();
            }
            catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }
}
