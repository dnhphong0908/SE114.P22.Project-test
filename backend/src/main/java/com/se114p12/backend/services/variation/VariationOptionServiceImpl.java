package com.se114p12.backend.services.variation;

import com.se114p12.backend.dtos.variation.VariationOptionRequestDTO;
import com.se114p12.backend.dtos.variation.VariationOptionResponseDTO;
import com.se114p12.backend.entities.variation.Variation;
import com.se114p12.backend.entities.variation.VariationOption;
import com.se114p12.backend.exceptions.DataConflictException;
import com.se114p12.backend.mappers.variation.VariationOptionMapper;
import com.se114p12.backend.repositories.variation.VariationOptionRepository;
import com.se114p12.backend.repositories.variation.VariationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VariationOptionServiceImpl implements VariationOptionService {

    private final VariationOptionRepository variationOptionRepository;
    private final VariationRepository variationRepository;
    private final VariationOptionMapper variationOptionMapper;

    @Override
    public List<VariationOptionResponseDTO> getByVariationId(Long variationId) {
        List<VariationOption> options = variationOptionRepository.findByVariationId(variationId);
        return options.stream()
                .map(variationOptionMapper::toDto)
                .toList();
    }

    @Override
    public VariationOptionResponseDTO create(VariationOptionRequestDTO dto) {
        Variation variation = variationRepository.findById(dto.getVariationId())
                .orElseThrow(() -> new DataConflictException("Variation not found"));

        if (variationOptionRepository.existsByValueAndVariationId(dto.getValue(), dto.getVariationId())) {
            throw new DataConflictException("Duplicate option value within the same variation.");
        }

        VariationOption entity = variationOptionMapper.toEntity(dto);
        entity.setVariation(variation);

        return variationOptionMapper.toDto(variationOptionRepository.save(entity));
    }

    @Override
    public VariationOptionResponseDTO update(Long id, VariationOptionRequestDTO dto) {
        VariationOption existing = variationOptionRepository.findById(id)
                .orElseThrow(() -> new DataConflictException("Variation option not found"));

        if (!existing.getValue().equals(dto.getValue()) &&
                variationOptionRepository.existsByValueAndVariationId(dto.getValue(), dto.getVariationId())) {
            throw new DataConflictException("Duplicate option value within the same variation.");
        }

        existing.setValue(dto.getValue());
        existing.setAdditionalPrice(dto.getAdditionalPrice());

        Variation variation = variationRepository.findById(dto.getVariationId())
                .orElseThrow(() -> new DataConflictException("Variation not found"));
        existing.setVariation(variation);

        return variationOptionMapper.toDto(variationOptionRepository.save(existing));
    }

    @Override
    public void delete(Long id) {
        VariationOption option = variationOptionRepository.findById(id)
                .orElseThrow(() -> new DataConflictException("Variation option not found"));
        variationOptionRepository.delete(option);
    }
}