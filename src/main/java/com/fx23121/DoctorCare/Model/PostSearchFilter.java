package com.fx23121.DoctorCare.Model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostSearchFilter {

    private String keyword;

    private Integer categoryId;

    private Integer locationId;

    private Integer specializationId;

    private Integer priceRangeId;

}
