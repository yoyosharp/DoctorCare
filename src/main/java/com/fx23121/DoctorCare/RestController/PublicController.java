package com.fx23121.DoctorCare.RestController;

import com.fx23121.DoctorCare.Entity.*;
import com.fx23121.DoctorCare.Model.PageableResult;
import com.fx23121.DoctorCare.Model.PostSearchFilter;
import com.fx23121.DoctorCare.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/public")
public class PublicController {

    @Autowired
    private SpecializationService specializationService;
    @Autowired
    private ClinicService clinicService;
    @Autowired
    private PostService postService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private DoctorService doctorService;

    @GetMapping("/topSpecialization")
    public ResponseEntity<?> getTopSpecialization(@RequestParam(value = "page", required = false) Integer pageIndex,
                                                  @RequestParam(value = "pageSize", required = false) Integer pageSize) {

        pageIndex = (pageIndex == null) ? 0 : pageIndex - 1;
        if (pageSize == null) pageSize = 5;
        //display by most searched / most booked specialization
        Pageable pageable = PageRequest.of(pageIndex, pageSize,
                Sort.by("visited").descending().and(Sort.by("booked").descending()));

        Page<Specialization> specializations = specializationService.getTopSpecialization(pageable);

        return new ResponseEntity<>(specializations, HttpStatus.OK);
    }

    @GetMapping("/topClinic")
    public ResponseEntity<?> getTopClinic(@RequestParam(value = "page", required = false) Integer pageIndex,
                                          @RequestParam(value = "pageSize", required = false) Integer pageSize) {

        pageIndex = (pageIndex == null) ? 0 : pageIndex - 1;
        if (pageSize == null) pageSize = 5;
        //display by most searched / most booked specialization
        Pageable pageable = PageRequest.of(pageIndex, pageSize,
                Sort.by("booked").descending());

        Page<Clinic> clinics = clinicService.getTopClinic(pageable);

        return new ResponseEntity<>(clinics, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<?> postSearch(@RequestParam(value = "type", required = false) Integer type,
                                        @RequestParam(value = "page", required = false) Integer pageIndex,
                                        @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                        @RequestBody PostSearchFilter filter) {
        pageIndex = (pageIndex == null) ? 0 : pageIndex - 1;
        if (pageSize == null) pageSize = 5;

        Pageable pageable = PageRequest.of(pageIndex, pageSize);

        if (type == null) type = 0;
        if (filter.getKeyword() == null) filter.setKeyword("");
        if (filter.getCategoryId() == null) filter.setCategoryId(0);
        if (filter.getLocationId() == null) filter.setLocationId(0);
        if (filter.getSpecializationId() == null) filter.setSpecializationId(0);
        if (filter.getPriceRangeId() == null) filter.setPriceRangeId(0);

        //API will return 404 if the result list is empty
        switch (type) {

            //search category e.g at home, examine, surgery...
            case 1 -> {
                Page<Category> categories = categoryService.searchCategory(filter.getKeyword(), pageable);
                if (categories.isEmpty()) return new ResponseEntity<>("No result found", HttpStatus.NOT_FOUND);
                PageableResult<Category> result = new PageableResult<>(categories.getContent(), categories.getTotalElements(), categories.getTotalPages());
                return new ResponseEntity<>(result, HttpStatus.OK);
            }

            //search clinic
            case 2 -> {
                Page<Clinic> clinics = clinicService.searchClinic(filter.getKeyword(), pageable);
                if (clinics.isEmpty()) return new ResponseEntity<>("No result found", HttpStatus.NOT_FOUND);
                PageableResult<Clinic> result = new PageableResult<>(clinics.getContent(), clinics.getTotalElements(), clinics.getTotalPages());
                return new ResponseEntity<>(result, HttpStatus.OK);
            }

            //search doctor
            case 3 -> {
                Page<Doctor> doctors = doctorService.searchDoctor(filter.getKeyword(), pageable);
                if (doctors.isEmpty()) return new ResponseEntity<>("No result found", HttpStatus.NOT_FOUND);
                PageableResult<Doctor> result = new PageableResult<>(doctors.getContent(), doctors.getTotalElements(), doctors.getTotalPages());
                return new ResponseEntity<>(result, HttpStatus.OK);
            }

            //search specialization
            case 4 -> {
                Page<Specialization> specializations = specializationService.searchSpecialization(filter.getKeyword(), pageable);
                if (specializations.isEmpty()) return new ResponseEntity<>("No result found", HttpStatus.NOT_FOUND);
                PageableResult<Specialization> result = new PageableResult<>(specializations.getContent(), specializations.getTotalElements(), specializations.getTotalPages());
                return new ResponseEntity<>(result, HttpStatus.OK);
            }

            //search post with filter
            default -> {
                PageableResult<Post> result = postService.searchPost(filter, pageSize, pageIndex);
                if (result.getContent().isEmpty()) return new ResponseEntity<>("No result found", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(result, HttpStatus.OK);
            }
        }
    }

}
