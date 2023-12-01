package com.fx23121.DoctorCare.Service;

import com.fx23121.DoctorCare.Entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {
    Page<Category> searchCategory(String keyword, Pageable pageable);
}
