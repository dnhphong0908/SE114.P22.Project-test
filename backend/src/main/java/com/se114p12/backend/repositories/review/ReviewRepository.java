//package com.se114p12.backend.repositories.review;
//
//import com.se114p12.backend.domains.review.Review;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repositories.EntityGraph;
//import org.springframework.data.jpa.repositories.JpaRepository;
//import org.springframework.data.jpa.repositories.JpaSpecificationExecutor;
//import org.springframework.data.repositories.PagingAndSortingRepository;
//import org.springframework.lang.NonNullApi;
//import org.springframework.stereotype.Repository;
//
//@Repository
//public interface ReviewRepository extends JpaRepository<Review, Long>, JpaSpecificationExecutor<Review>,
//        PagingAndSortingRepository<Review, Long> {
//
//    // Lấy tất cả review (phân trang)
//    Page<Review> findAll(Pageable pageable);
//
//    // Lấy review theo userId (phân trang)
//    @EntityGraph(attributePaths = {"user", "order"})
//    Page<Review> findByUserId(Long userId, Pageable pageable);
//
//    // Lấy review theo orderId (phân trang)
//    Page<Review> findByOrderId(Long orderId, Pageable pageable);
//
//    // Lấy review theo cả userId và orderId (phân trang)
//    Page<Review> findByUserIdAndOrderId(Long userId, Long orderId, Pageable pageable);
//}