package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.*;

import java.math.BigDecimal;

import java.util.List;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private final UserService userService = new UserService();
    private final AccountService accountService = new AccountService();
    private final TransferService transferService = new TransferService();
    private AuthenticatedUser currentUser;

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }

    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1){
                int userId = currentUser.getUser().getId();
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingTransfers();
                approveOrRejectTransfer();
            } else if (menuSelection == 4) {
                viewUsers();
                sendBucks();
            } else if (menuSelection == 5) {
                viewUsers();
                requestBucks();
            } else if (menuSelection == 6) {
                viewTransferDetails();
            } else if (menuSelection == 7) {
                viewUsers();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

    private void viewCurrentBalance() {
        BigDecimal balance = accountService.getAccountBalance(currentUser.getUser().getId());
        if (balance != null) {
            consoleService.printBalance(balance);
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void viewTransferHistory() {
        List<Transfer> transfers = transferService.listAllTransfers();
        consoleService.printTransfers(transfers);
    }

    private void viewTransferDetails() {
        int selectedTransfer = consoleService.promptForInt("Enter ID of transfer to view: ");
        Transfer transfer = transferService.getTransferById(selectedTransfer);
        consoleService.printTransfer(transfer);
    }

    private void viewUsers() {
        List<User> users = userService.listUsers();
        consoleService.printUsers(users);
    }


    private void viewPendingTransfers() {
        List<Transfer> transfers = transferService.listPendingTransfers();
        consoleService.printPendingTransfers(transfers);
    }

    private void approveOrRejectTransfer() {
        List<Transfer> transfers = transferService.listPendingTransfers();
        consoleService.printTransfers(transfers);

        Transfer transfer = null;
        int transferId = consoleService.promptForInt("Please enter transfer ID to approve/reject (0 to cancel): ");

        if (transferId != 0) {
            transfer = transferService.getTransferById(transferId);
            if (transfer == null) {
                System.out.println("Transfer not found.");
                return;
            }
            consoleService.printTransfer(transfer);
            boolean isApproved = consoleService.promptForTransferApproval("Approve transfer? (Y/N) ");
            if (isApproved) {
                transferService.updateTransferStatus(transferId, transfer);
                System.out.println("Transfer approved.");
            } else {
                transferService.updateTransferStatus(transferId, transfer);
                System.out.println("Transfer rejected.");
            }
        }
    }

    private void sendBucks() {
        List<User> users = userService.listUsers();
        consoleService.printUsers(users);
        int recipientId = consoleService.promptForInt("Enter ID of user you are sending to (0 to cancel)");
        if (recipientId != 0) {
            BigDecimal amount = consoleService.promptForBigDecimal("Enter transfer amount:");
            TransferDto transferDto = new TransferDto(currentUser.getUser().getId(), recipientId, amount, 2);
            Transfer transfer = transferService.createTransfer(transferDto, currentUser.getUser().getId());
            if (transfer != null) {
                consoleService.printTransfer(transfer);
            } else {
                consoleService.printErrorMessage();
            }
        }
    }

    private void requestBucks() {
        List<User> users = userService.listUsers();
        consoleService.printUsers(users);
        int requesterId = consoleService.promptForInt("Enter ID of user you are requesting from (0 to cancel)");
        if (requesterId != 0) {
            BigDecimal amount = consoleService.promptForBigDecimal("Enter transfer amount:");
            TransferDto transferDto = new TransferDto(requesterId, currentUser.getUser().getId(), amount, 1);
            Transfer transfer = transferService.createTransferRequest(transferDto, currentUser.getUser().getId());
            if (transfer != null) {
                consoleService.printTransfer(transfer);
            } else {
                consoleService.printErrorMessage();
            }
        }
    }



}
