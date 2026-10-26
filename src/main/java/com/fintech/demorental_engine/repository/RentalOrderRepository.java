package com.fintech.demorental_engine.repository;

import com.fintech.demorental_engine.entity.RentalOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RentalOrderRepository extends JpaRepository<RentalOrder, UUID> {
}