package com.fx23121.DoctorCare.Service;

import com.fx23121.DoctorCare.Entity.Post;
import com.fx23121.DoctorCare.Model.PageableResult;
import com.fx23121.DoctorCare.Model.PostSearchFilter;
import com.fx23121.DoctorCare.Repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Override
    @Transactional
    public PageableResult<Post> searchPost(PostSearchFilter filter, Integer pageSize, Integer pageIndex) {

        return postRepository.searchPost(filter, pageSize, pageIndex);
    }
}
