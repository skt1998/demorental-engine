package com.fintech.demorental_engine.service;

import com.fintech.demorental_engine.entity.RentalOrder;
import com.fintech.demorental_engine.repository.RentalOrderRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class RentalOrderService {

    private final RentalOrderRepository repository;

    public RentalOrderService(RentalOrderRepository repository) {
        this.repository = repository;
    }

    public RentalOrder createBooking(UUID renterId, UUID assetId, BigDecimal rentAmount, BigDecimal depositAmount) {
        // Basic fintech validation: deposit and rent must be positive
        if (rentAmount.compareTo(BigDecimal.ZERO) <= 0 || depositAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Rent and deposit must be greater than zero.");
        }

        RentalOrder order = new RentalOrder(renterId, assetId, rentAmount, depositAmount);
        return repository.save(order);
    }

    public List<RentalOrder> getAllOrders() {
        return repository.findAll();
    }
}