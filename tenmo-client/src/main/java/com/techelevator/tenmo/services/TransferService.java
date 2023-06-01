package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDto;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TransferService {
    private static final String API_BASE_URL = "http://localhost:8080/";
    private final RestTemplate restTemplate = new RestTemplate();
    private String authToken;

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public List<Transfer> listAllTransfers() {
        List<Transfer> transfers = null;
        try {
            ResponseEntity<Transfer[]> response = restTemplate.exchange(API_BASE_URL + "transfer", HttpMethod.GET, makeAuthEntity(), Transfer[].class);
            transfers = Arrays.asList(response.getBody());
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transfers;
    }


    public Transfer getTransferById(int accountId) {
        Transfer transfer = null;

        try {
            ResponseEntity<Transfer> response = restTemplate.exchange(API_BASE_URL + "transfer/{transferId}" + accountId, HttpMethod.GET, makeAuthEntity(), Transfer.class);
            transfer = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {

            BasicLogger.log(e.getMessage());
        }
        return transfer;
    }

    public Transfer createTransfer(TransferDto newTransfer, Integer userId) {
        Transfer returnedTransfer = null;
        try {
            returnedTransfer = restTemplate.postForObject(API_BASE_URL + "transfer",
                    makeTransferEntity(newTransfer), Transfer.class);
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return returnedTransfer;
    }

    public Transfer createTransferRequest(TransferDto newTransfer, Integer userId) {
        Transfer returnedTransfer = null;
        try {
            returnedTransfer = restTemplate.postForObject(API_BASE_URL + "transfer/request",
                    makeTransferEntity(newTransfer), Transfer.class);
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return returnedTransfer;
    }

    public void updateTransferStatus(Integer transferId, Transfer transfer) {
        try {
            restTemplate.put(API_BASE_URL + "transfer/update/{transferId}" + transfer, makeAuthEntity(), Transfer.class);
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
    }

    public List<Transfer> listPendingTransfers() {
        List<Transfer> transfers = new ArrayList<>();
        try {
            Transfer[] transferArray = restTemplate.exchange(API_BASE_URL + "transfer/pending", HttpMethod.GET, makeAuthEntity(), Transfer[].class).getBody();
            if (transferArray != null) {
                transfers = Arrays.asList(transferArray);
            }
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transfers;
    }

    public Account getAccountFromUsername(int userId) {
        return restTemplate.getForObject(API_BASE_URL + "/transfer/accountFrom/" + userId, Account.class);
    }

    public String getTransferTypeDesc(int transferTypeId) {
        return restTemplate.getForObject(API_BASE_URL + "/transferType/" + transferTypeId, String.class);
    }

    public String getTransferStatusDesc(int transferStatusId) {
        return restTemplate.getForObject(API_BASE_URL + "/transferStatus/" + transferStatusId, String.class);
    }


    private HttpEntity<TransferDto> makeTransferEntity(TransferDto transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(transfer, headers);
    }

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }
}