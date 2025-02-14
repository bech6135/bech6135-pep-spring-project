package com.example.service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;


@Service
public class AccountService {
    
    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Optional<Account> registerUser(Account account) {
        if (account.getUsername() == null || account.getUsername().isBlank() ||
            account.getPassword() == null || account.getPassword().length() < 4 ||
            accountRepository.findByUsername(account.getUsername()).isPresent()) {
            return Optional.empty(); // Invalid registration
        }
        return Optional.of(accountRepository.save(account));
    }

    public Optional<Account> loginUser(String username, String password) {
        return accountRepository.findByUsername(username)
                .filter(account -> account.getPassword().equals(password));
    }
}

