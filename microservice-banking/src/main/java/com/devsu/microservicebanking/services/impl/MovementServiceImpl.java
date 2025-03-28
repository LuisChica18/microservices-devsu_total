package com.devsu.microservicebanking.services.impl;

import com.devsu.microservicebanking.enums.AccountTypeEnum;
import com.devsu.microservicebanking.enums.MovementTypeEnum;
import com.devsu.microservicebanking.models.Report;
import com.devsu.microservicebanking.models.entities.Account;
import com.devsu.microservicebanking.models.entities.Movement;
import com.devsu.microservicebanking.exceptions.AccountNotFoundException;
import com.devsu.microservicebanking.exceptions.BalanceLessThanZeroException;
import com.devsu.microservicebanking.exceptions.MovementTypeNotFoundException;
import com.devsu.microservicebanking.repositories.MovementRepository;
import com.devsu.microservicebanking.services.AccountService;
import com.devsu.microservicebanking.services.MovementService;
import com.devsu.microservicebanking.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MovementServiceImpl implements MovementService {
    
    private MovementRepository movementRepository;
    private AccountService accountService;

    @Autowired
    public MovementServiceImpl(MovementRepository movementRepository, AccountService accountService) {
        this.movementRepository = movementRepository;
        this.accountService = accountService;
    }

    @Override
    public List<Movement> getAllMovements() {
        return movementRepository.findAll();
    }

    @Override
    public Optional<Movement> getMovementById(Long id) {
        return movementRepository.findById(id);
    }

    @Override
    public Movement createOrUpdateMovement(Movement movement) {
        validateMovement(movement);

        Account account = getCuentaOrThrowException(movement.getAccount().getId());
        BigDecimal lastBalance = calculateLastBalance(account);
        BigDecimal newBalance = calculateNewBalance(movement, account, lastBalance);

        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new BalanceLessThanZeroException("No se puede realizar el retiro porque el valor supera el saldo de la cuenta");
        }

        updateMovementData(movement, account, newBalance);
        return movementRepository.save(movement);
    }

    private void validateMovement(Movement movement) {
        Optional<Account> cuentaOptional = accountService.getAccountById(movement.getAccount().getId());
        if (cuentaOptional.isEmpty()) {
            throw new AccountNotFoundException("La cuenta asociada no existe");
        }

        if (!Objects.equals(movement.getMovementType().name(), Constants.RETIRO)
                && !Objects.equals(movement.getMovementType().name(), Constants.DEPOSITO)) {
            throw new MovementTypeNotFoundException("El tipo de movimiento no existe");
        }
    }

    private Account getCuentaOrThrowException(Long cuentaId) {
        return accountService.getAccountById(cuentaId)
                .orElseThrow(() -> new AccountNotFoundException("La cuenta asociada no existe"));
    }

    private BigDecimal calculateLastBalance(Account cuenta) {
        Movement lastMovement = movementRepository.findLastMovement();
        return (lastMovement != null) ? lastMovement.getBalance() : cuenta.getInitialBalance();
    }

    private BigDecimal calculateNewBalance(Movement movimientos, Account account, BigDecimal lastBalance) {
        BigDecimal amount = movimientos.getAmount();
        return (Objects.equals(movimientos.getMovementType().name(), Constants.RETIRO)) ?
                lastBalance.subtract(amount) : lastBalance.add(amount);
    }

    private void updateMovementData(Movement movement, Account account, BigDecimal newBalance) {
        movement.setAccount(account);
        movement.setMovementDate(LocalDateTime.now());
        movement.setBalance(newBalance);
    }

    @Override
    public List<Report> getMovementClientAccountRecords(Long clientId, Date dateFrom, Date dateTo) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        List<Map<String, Object>> movements =  movementRepository.getMovementClientAccountRecords(clientId, dateFrom, dateTo);
        return movements.stream()
                .map(data -> Report.builder()
                        .client(data.get("Cliente").toString())
                        .accountNumber(data.get("Numero Cuenta").toString())
                        .previousBalance(new BigDecimal(data.get("Saldo Inicial").toString()))
                        .movement(new BigDecimal(data.get("Movimiento").toString()))
                        .balance(new BigDecimal(data.get("Saldo Disponible").toString()))
                        .dateTransaction(LocalDateTime.parse(data.get("Fecha").toString(), formatter))
                        .movementType(AccountTypeEnum.valueOf(data.get("Tipo").toString()))
                        .status(Boolean.parseBoolean(data.get("Estado").toString()))
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public void initialDeposit(Account account, BigDecimal amount){
        Movement movement = new Movement();
        movement.setMovementDate(LocalDateTime.now());
        movement.setMovementType(MovementTypeEnum.DEPOSITO);
        movement.setAmount(amount);
        movement.setBalance(amount);
        movement.setAccount(account);
        movementRepository.save(movement);
    }
}
