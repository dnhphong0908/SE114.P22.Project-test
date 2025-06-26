package com.se114p12.backend.controllers.review;

import com.se114p12.backend.constants.AppConstant;
import com.se114p12.backend.dtos.review.ReviewRequestDTO;
import com.se114p12.backend.dtos.review.ReviewResponseDTO;
import com.se114p12.backend.services.review.ReviewService;
import com.se114p12.backend.vo.PageVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Review Module", description = "APIs for managing reviews")
@RestController
@RequestMapping(AppConstant.API_BASE_PATH + "/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "Create a new review", description = "Create a new review for the completed order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Create successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @PostMapping
    public ResponseEntity<ReviewResponseDTO> createReview(@Valid @RequestBody ReviewRequestDTO request) {
        return ResponseEntity.ok(reviewService.createReview(request));
    }

    @Operation(summary = "Get all reviews", description = "Return the list of reviews")
    @GetMapping
    public ResponseEntity<PageVO<ReviewResponseDTO>> getAllReviews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(reviewService.getReviews(page, size));
    }

    @Operation(summary = "Get the review of the order", description = "Return the list of reviews related to the order")
    @GetMapping("/order/{orderId}")
    public ResponseEntity<PageVO<ReviewResponseDTO>> getReviewsByOrder(
            @PathVariable Long orderId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(reviewService.getReviewsByOrder(orderId, page, size));
    }

    @Operation(summary = "Create a reply to the review", description = "Add new content to the reply")
    @PatchMapping("/{reviewId}/reply")
    public ResponseEntity<ReviewResponseDTO> replyToReview(
            @PathVariable Long reviewId,
            @RequestParam String reply
    ) {
        return ResponseEntity.ok(reviewService.replyToReview(reviewId, reply));
    }

    @Operation(summary = "Delete review", description = "Delete a review by the id")
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }
}