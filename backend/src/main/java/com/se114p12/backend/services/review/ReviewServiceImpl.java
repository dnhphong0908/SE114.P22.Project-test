package com.se114p12.backend.services.review;

import com.se114p12.backend.entities.order.Order;
import com.se114p12.backend.entities.review.Review;
import com.se114p12.backend.dtos.review.ReviewRequestDTO;
import com.se114p12.backend.dtos.review.ReviewResponseDTO;
import com.se114p12.backend.mappers.review.ReviewMapper;
import com.se114p12.backend.repositories.review.ReviewRepository;
import com.se114p12.backend.repositories.order.OrderRepository;
import com.se114p12.backend.vo.PageVO;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;
    private final ReviewMapper reviewMapper;

    @Override
    @Transactional
    public ReviewResponseDTO createReview(ReviewRequestDTO request) {
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        Review review = new Review()
                .setOrder(order)
                .setRate(request.getRate())
                .setContent(request.getContent())
                .setReply(request.getReply());

        return reviewMapper.toResponse(reviewRepository.save(review));
    }

    @Override
    @Transactional(readOnly = true)
    public PageVO<ReviewResponseDTO> getReviews(int page, int size) {
        Page<Review> reviewPage = reviewRepository.findAll(PageRequest.of(page, size, Sort.by("createdAt").descending()));
        return convertToPageVO(reviewPage);
    }

    @Override
    @Transactional(readOnly = true)
    public PageVO<ReviewResponseDTO> getReviewsByOrder(Long orderId, int page, int size) {
        Page<Review> reviewPage = reviewRepository.findByOrderId(orderId, PageRequest.of(page, size, Sort.by("createdAt").descending()));
        return convertToPageVO(reviewPage);
    }

    @Override
    @Transactional
    public ReviewResponseDTO replyToReview(Long reviewId, String reply) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Review not found"));
        review.setReply(reply);
        return reviewMapper.toResponse(reviewRepository.save(review));
    }

    @Override
    @Transactional
    public void deleteReview(Long reviewId) {
        if (!reviewRepository.existsById(reviewId)) {
            throw new EntityNotFoundException("Review not found");
        }
        reviewRepository.deleteById(reviewId);
    }

    private PageVO<ReviewResponseDTO> convertToPageVO(Page<Review> reviewPage) {
        return PageVO.<ReviewResponseDTO>builder()
                .page(reviewPage.getNumber())
                .size(reviewPage.getSize())
                .totalElements(reviewPage.getTotalElements())
                .totalPages(reviewPage.getTotalPages())
                .numberOfElements(reviewPage.getNumberOfElements())
                .content(reviewPage.map(reviewMapper::toResponse).getContent())
                .build();
    }
}