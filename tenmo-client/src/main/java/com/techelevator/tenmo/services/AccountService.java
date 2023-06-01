package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import com.techelevator.util.BasicLogger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;

public class AccountService {
    private static final String API_BASE_URL = "http://localhost:8080/";
    private final RestTemplate restTemplate = new RestTemplate();


    private String authToken = null;

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public List<Account> listAccounts() {
        Account[] accounts = null;
        try {
            ResponseEntity<Account[]> response = restTemplate.exchange(API_BASE_URL + "account", HttpMethod.GET, makeAuthEntity(), Account[].class);
            accounts = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return List.of(accounts);
    }


    public Account getAccountByAccountId(int accountId){
        Account account = null;

        try {
            ResponseEntity<Account> response = restTemplate.exchange(API_BASE_URL + "account/{accountId}" + accountId, HttpMethod.GET, makeAuthEntity(), Account.class);
            account = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {

            BasicLogger.log(e.getMessage());
        }
        return account;
    }

    public Account getAccountByUserId(int userId){
        Account account = null;

        try {
            ResponseEntity<Account> response = restTemplate.exchange(API_BASE_URL + "user/{userId}" + userId, HttpMethod.GET, makeAuthEntity(), Account.class);
            account = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {

            BasicLogger.log(e.getMessage());
        }
        return account;
    }
    public BigDecimal getAccountBalance(Integer userId){
        BigDecimal balance = null;

        try {
            ResponseEntity<BigDecimal> response = restTemplate.exchange(API_BASE_URL + "balance" + userId, HttpMethod.GET, makeAuthEntity(), BigDecimal.class);
            balance = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {

            BasicLogger.log(e.getMessage());
        }
        return balance;
    }

    public Account updateAccountBalance(Integer userId){
        Account updatedAccountBalance= null;

        try {
            ResponseEntity<Account> response = restTemplate.exchange(API_BASE_URL + "balance/{accountId}" + userId, HttpMethod.PUT, makeAuthEntity(), Account.class);
            updatedAccountBalance = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {

            BasicLogger.log(e.getMessage());
        }
        return updatedAccountBalance;
    }

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }
}