package com.fx23121.DoctorCare.Repository;

import com.fx23121.DoctorCare.Entity.Post;
import com.fx23121.DoctorCare.Model.PageableResult;
import com.fx23121.DoctorCare.Model.PostSearchFilter;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.List;
import java.util.Optional;

@Repository
@EnableTransactionManagement
public class PostRepositoryImpl implements PostRepository {

    @Autowired
    private EntityManager entityManager;

    @Override
    public PageableResult<Post> searchPost(PostSearchFilter filter, Integer pageSize, Integer pageIndex) {

        //Create queries and set queries' parameters based on filter
        String keyword = "%" + filter.getKeyword() + "%";
        String strTotalResultQuery = "SELECT COUNT(p) FROM Post p WHERE p.title LIKE :keyword";
        String strResultQuery = "FROM Post p WHERE p.title LIKE :keyword";

        int categoryId = 0;
        if (filter.getCategoryId() != 0) {
            categoryId = filter.getCategoryId();
            strTotalResultQuery += " AND p.category.id = :categoryId";
            strResultQuery += " AND p.category.id = :categoryId";
        }

        int locationId = 0;
        if (filter.getLocationId() != 0) {
            locationId = filter.getLocationId();
            strTotalResultQuery += " AND p.clinic.location.id = :locationId";
            strResultQuery += " AND p.clinic.location.id = :locationId";
        }

        int specializationId = 0;
        if (filter.getSpecializationId() != 0) {
            specializationId = filter.getSpecializationId();
            strTotalResultQuery += " AND p.specialization.id = :specializationId";
            strResultQuery += " AND p.specialization.id = :specializationId";
        }

        int priceRangeId = 0;
        if (filter.getPriceRangeId() != 0) {
            priceRangeId = filter.getPriceRangeId();
            strTotalResultQuery += " AND p.priceRange.id = :priceRangeId";
            strResultQuery += " AND p.priceRange.id = :priceRangeId";
        }

        strResultQuery += " ORDER by p.booked DESC";

        TypedQuery<Long> totalResultQuery = entityManager.createQuery(strTotalResultQuery, Long.class);
        TypedQuery<Post> resultQuery = entityManager.createQuery(strResultQuery, Post.class);

        totalResultQuery.setParameter("keyword", keyword);
        resultQuery.setParameter("keyword", keyword);

        if (categoryId != 0) {
            totalResultQuery.setParameter("categoryId", categoryId);
            resultQuery.setParameter("categoryId", categoryId);
        }

        if (locationId != 0) {
            totalResultQuery.setParameter("locationId", locationId);
            resultQuery.setParameter("locationId", locationId);
        }

        if (specializationId != 0) {
            totalResultQuery.setParameter("specializationId", specializationId);
            resultQuery.setParameter("specializationId", specializationId);
        }

        if (priceRangeId != 0) {
            totalResultQuery.setParameter("priceRangeId", priceRangeId);
            resultQuery.setParameter("priceRangeId", priceRangeId);
        }

        //get the required results
        long totalResult = totalResultQuery.getSingleResult();
        int resultCount = (int) Math.min(pageSize, totalResult - pageIndex * pageSize);

        resultQuery.setFirstResult(pageSize * pageIndex);
        resultQuery.setMaxResults(resultCount);

        List<Post> list = resultQuery.getResultList();

        int maxPageCount = (int) (totalResult/pageSize);
        if (totalResult % pageSize != 0) maxPageCount ++;

        return new PageableResult<>(list, totalResult, maxPageCount);
    }

    @Override
    public Post findById(int postId) {

        TypedQuery<Post> query = entityManager.createQuery("FROM Post WHERE id = :postId", Post.class);
        query.setParameter("postId", postId);
        try{
            return query.getSingleResult();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void save(Post currentPost) {
        entityManager.merge(currentPost);
    }
}
