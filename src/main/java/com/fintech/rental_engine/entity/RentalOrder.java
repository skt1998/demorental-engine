package com.fintech.rental_engine.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "rental_orders")
public class RentalOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID renterId;

    @Column(nullable = false)
    private UUID assetId;

    @Column(nullable = false)
    private String status; // PENDING_PAYMENT, ACTIVE, COMPLETED, CANCELLED

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal rentAmount;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal depositAmount;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    public RentalOrder() {
    }

    public RentalOrder(UUID renterId, UUID assetId, BigDecimal rentAmount, BigDecimal depositAmount) {
        this.renterId = renterId;
        this.assetId = assetId;
        this.rentAmount = rentAmount;
        this.depositAmount = depositAmount;
        this.status = "PENDING_PAYMENT";
        this.createdAt = Instant.now();
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getRenterId() { return renterId; }
    public void setRenterId(UUID renterId) { this.renterId = renterId; }

    public UUID getAssetId() { return assetId; }
    public void setAssetId(UUID assetId) { this.assetId = assetId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public BigDecimal getRentAmount() { return rentAmount; }
    public void setRentAmount(BigDecimal rentAmount) { this.rentAmount = rentAmount; }

    public BigDecimal getDepositAmount() { return depositAmount; }
    public void setDepositAmount(BigDecimal depositAmount) { this.depositAmount = depositAmount; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}