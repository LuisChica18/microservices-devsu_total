package com.devsu.microservicebanking.services.impl;

import com.devsu.microservicebanking.exceptions.AccountNotFoundException;
import com.devsu.microservicebanking.exceptions.ResourceNotFoundException;
import com.devsu.microservicebanking.models.Client;
import com.devsu.microservicebanking.models.entities.Account;
import com.devsu.microservicebanking.externalServices.ClientService;
import com.devsu.microservicebanking.models.entities.AccountClient;
import com.devsu.microservicebanking.repositories.AccountRepository;
import com.devsu.microservicebanking.services.AccountService;
import com.devsu.microservicebanking.services.MovementService;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

    private AccountRepository repository;

    private ClientService clientService;

    private MovementService movementService;

    @Autowired
    public AccountServiceImpl(AccountRepository repository, @Lazy MovementService movementService, ClientService clientService) {
        this.repository = repository;
        this.movementService = movementService;
        this.clientService = clientService;
    }

    @Override
    public List<Account> getAllAccounts() {
        return repository.findAll();
    }

    @Override
    public Optional<Account> getAccountById(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public Account createOrUpdateAccount(Account account) throws ResourceNotFoundException, AccountNotFoundException {
        List<AccountClient> accountClients = new ArrayList<>();
        try {
            Client client = clientService.getClient(account.getClient().getId());
            AccountClient accountClient = new AccountClient();
            accountClient.setClientId(client.getId());
            accountClients.add(accountClient);
            account.setClient(client);
            account.setAccountClient(accountClients);

            Account accountSaved = repository.save(account);
            movementService.initialDeposit(accountSaved, account.getInitialBalance());
            return accountSaved;
        }catch (FeignException e){
            throw new ResourceNotFoundException(e.getMessage());
        }catch (DataIntegrityViolationException e){
            throw new AccountNotFoundException("Cuenta ya registrada");
        }
    }

    @Override
    public void deleteAccountById(Long id) {
        repository.deleteById(id);
    }
}
