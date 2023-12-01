package com.fx23121.DoctorCare.Service;

import com.fx23121.DoctorCare.Entity.Post;
import com.fx23121.DoctorCare.Model.PageableResult;
import com.fx23121.DoctorCare.Model.PostSearchFilter;

public interface PostService {

    PageableResult<Post> searchPost(PostSearchFilter filter, Integer pageSize, Integer pageIndex);
}
