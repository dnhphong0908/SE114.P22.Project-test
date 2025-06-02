package com.se114p12.backend.controllers.variation;

import com.se114p12.backend.dtos.variation.VariationOptionRequestDTO;
import com.se114p12.backend.dtos.variation.VariationOptionResponseDTO;
import com.se114p12.backend.services.variation.VariationOptionService;
import com.se114p12.backend.vo.PageVO;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Variation Option Module")
@RestController
@RequestMapping("/api/v1/variation-options")
@RequiredArgsConstructor
public class VariationOptionController {

    private final VariationOptionService variationOptionService;

    @GetMapping
    public ResponseEntity<PageVO<VariationOptionResponseDTO>> getByVariationId(
            @RequestParam Long variationId,
            Pageable pageable
    ) {
        return ResponseEntity.ok(variationOptionService.getByVariationId(variationId, pageable));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<VariationOptionResponseDTO> create(@Valid @RequestBody VariationOptionRequestDTO dto) {
        return ResponseEntity.ok(variationOptionService.create(dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<VariationOptionResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody VariationOptionRequestDTO dto) {
        return ResponseEntity.ok(variationOptionService.update(id, dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        variationOptionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
