package com.se114p12.backend.repositories.promotion;

import com.se114p12.backend.entities.promotion.UserPromotion;
import com.se114p12.backend.entities.promotion.UserPromotionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface UserPromotionRepository extends JpaRepository<UserPromotion, UserPromotionId>, JpaSpecificationExecutor<UserPromotion> {
    List<UserPromotion> findByUserId(Long userId);
    Optional<UserPromotion> findByUserIdAndPromotionId(Long userId, Long promotionId);}
