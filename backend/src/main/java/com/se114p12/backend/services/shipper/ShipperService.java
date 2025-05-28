package com.se114p12.backend.services.shipper;

import com.se114p12.backend.dtos.shipper.ShipperRequest;
import com.se114p12.backend.dtos.shipper.ShipperResponse;
import com.se114p12.backend.vo.PageVO;

public interface ShipperService {
    ShipperResponse create(ShipperRequest request);
    PageVO<ShipperResponse> getAll(int page, int size);
    ShipperResponse getById(Long id);
    ShipperResponse update(Long id, ShipperRequest request);
    void delete(Long id);
}