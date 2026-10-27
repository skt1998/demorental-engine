package com.fintech.rental_engine.controller;

import com.fintech.rental_engine.entity.RentalOrder;
import com.fintech.rental_engine.service.RentalOrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/rentals")
public class RentalOrderController {

    private final RentalOrderService service;

    public RentalOrderController(RentalOrderService service) {
        this.service = service;
    }

    // DTO (Data Transfer Object) for incoming JSON requests
    public record CreateBookingRequest(
            UUID renterId,
            UUID assetId,
            BigDecimal rentAmount,
            BigDecimal depositAmount
    ) {}

    // POST /api/v1/rentals
    @PostMapping
    public ResponseEntity<RentalOrder> createRental(@RequestBody CreateBookingRequest request) {
        RentalOrder order = service.createBooking(
                request.renterId(),
                request.assetId(),
                request.rentAmount(),
                request.depositAmount()
        );
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    // GET /api/v1/rentals
    @GetMapping
    public ResponseEntity<List<RentalOrder>> getAllRentals() {
        return ResponseEntity.ok(service.getAllOrders());
    }
}