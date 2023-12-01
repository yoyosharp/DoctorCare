package com.fx23121.DoctorCare.Repository;

import com.fx23121.DoctorCare.Entity.Post;
import com.fx23121.DoctorCare.Model.PageableResult;
import com.fx23121.DoctorCare.Model.PostSearchFilter;

public interface PostRepository {
    PageableResult<Post> searchPost(PostSearchFilter filter, Integer pageSize, Integer pageIndex);

    Post findById(int postId);

    void save(Post currentPost);
}
