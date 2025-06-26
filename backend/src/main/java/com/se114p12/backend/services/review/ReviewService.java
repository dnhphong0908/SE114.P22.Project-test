package com.se114p12.backend.services.review;

import com.se114p12.backend.dtos.review.ReviewRequestDTO;
import com.se114p12.backend.dtos.review.ReviewResponseDTO;
import com.se114p12.backend.vo.PageVO;

public interface ReviewService {
    ReviewResponseDTO createReview(ReviewRequestDTO request);

    PageVO<ReviewResponseDTO> getReviews(int page, int size);

    PageVO<ReviewResponseDTO> getReviewsByOrder(Long orderId, int page, int size);

    ReviewResponseDTO replyToReview(Long reviewId, String reply);

    void deleteReview(Long reviewId);
}