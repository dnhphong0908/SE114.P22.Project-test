package com.se114p12.backend.controllers.shipper;

import com.se114p12.backend.dtos.shipper.ShipperRequest;
import com.se114p12.backend.dtos.shipper.ShipperResponse;
import com.se114p12.backend.services.shipper.ShipperServiceImpl;
import com.se114p12.backend.vo.PageVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/shippers")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class ShipperController {

    private final ShipperServiceImpl shipperServiceImpl;

    @PostMapping
    public ResponseEntity<ShipperResponse> create(@RequestBody ShipperRequest request) {
        return ResponseEntity.ok(shipperServiceImpl.create(request));
    }

    @GetMapping
    public ResponseEntity<PageVO<ShipperResponse>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(shipperServiceImpl.getAll(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShipperResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(shipperServiceImpl.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShipperResponse> update(@PathVariable Long id, @RequestBody ShipperRequest request) {
        return ResponseEntity.ok(shipperServiceImpl.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        shipperServiceImpl.delete(id);
        return ResponseEntity.noContent().build();
    }
}