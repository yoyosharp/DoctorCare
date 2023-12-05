package com.fx23121.DoctorCare.Service;

import com.fx23121.DoctorCare.Entity.Category;
import com.fx23121.DoctorCare.Repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Page<Category> searchCategory(String keyword, Pageable pageable) {
        return categoryRepository.findByNameContaining(keyword, pageable);
    }
}
