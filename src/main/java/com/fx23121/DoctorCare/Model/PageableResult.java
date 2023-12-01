package com.fx23121.DoctorCare.Model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PageableResult<T> {
    private List<T> content;

    private long totalElement;

    private int totalPage;
}
