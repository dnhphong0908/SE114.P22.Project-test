package com.se114p12.backend.controllers.variation;

import com.se114p12.backend.dtos.variation.VariationRequestDTO;
import com.se114p12.backend.dtos.variation.VariationResponseDTO;
import com.se114p12.backend.services.variation.VariationService;
import com.se114p12.backend.vo.PageVO;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Variation Module", description = "APIs for managing variations")
@RestController
@RequestMapping("/api/v1/variations")
@RequiredArgsConstructor
public class VariationController {
  private final VariationService variationService;

  @GetMapping
  public ResponseEntity<PageVO<VariationResponseDTO>> getVariationsByProduct(
          @RequestParam Long productId,
          @RequestParam(required = false) String name,
          @ParameterObject Pageable pageable
  ) {
    Page<VariationResponseDTO> pageResult = variationService.getVariationsByProductId(productId, name, pageable);
    PageVO<VariationResponseDTO> pageVO = PageVO.<VariationResponseDTO>builder()
            .page(pageResult.getNumber())
            .size(pageResult.getSize())
            .totalElements(pageResult.getTotalElements())
            .totalPages(pageResult.getTotalPages())
            .numberOfElements(pageResult.getNumberOfElements())
            .content(pageResult.getContent())
            .build();

    return ResponseEntity.ok(pageVO);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping
  public ResponseEntity<VariationResponseDTO> createVariation(@Valid @RequestBody VariationRequestDTO dto) {
    return ResponseEntity.ok(variationService.create(dto));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PutMapping("/{id}")
  public ResponseEntity<VariationResponseDTO> updateVariation(
          @PathVariable Long id, @Valid @RequestBody VariationRequestDTO dto) {
    return ResponseEntity.ok(variationService.update(id, dto));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteVariation(@PathVariable Long id) {
    variationService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
