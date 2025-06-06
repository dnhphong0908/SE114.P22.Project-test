package com.se114p12.backend.repositories.order;

import com.se114p12.backend.entities.order.Order;
import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository
    extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

  Page<Order> findByUser_Id(Long userId, Specification<Order> spec, Pageable pageable);

  @Query(
      "SELECT o FROM Order o WHERE (:start IS NULL OR o.createdAt >= :start) AND (:end IS NULL OR"
          + " o.createdAt <= :end)")
  List<Order> findByCreatedAtOptional(@Param("start") Instant start, @Param("end") Instant end);
}
