package com.se114p12.backend.services.variation;

import com.se114p12.backend.dtos.variation.VariationOptionRequestDTO;
import com.se114p12.backend.dtos.variation.VariationOptionResponseDTO;

import java.util.List;

public interface VariationOptionService {
    List<VariationOptionResponseDTO> getByVariationId(Long variationId);

    VariationOptionResponseDTO create(VariationOptionRequestDTO dto);
    VariationOptionResponseDTO update(Long id, VariationOptionRequestDTO dto);
    void delete(Long id);
}