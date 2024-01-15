package com.devsu.microservicebanking.services;

import com.devsu.microservicebanking.models.Report;
import com.devsu.microservicebanking.models.entities.Account;
import com.devsu.microservicebanking.models.entities.Movement;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface MovementService {
    List<Movement> getAllMovements();

    Optional<Movement> getMovementById(Long id);

    Movement createOrUpdateMovement(Movement movement);

    //List<Map<String, Object>> getMovementClientAccountRecords(Long clientId, Date dateFrom, Date dateTo);
    List<Report> getMovementClientAccountRecords(Long clientId, Date dateFrom, Date dateTo);

    void initialDeposit(Account account, BigDecimal amount);
}
