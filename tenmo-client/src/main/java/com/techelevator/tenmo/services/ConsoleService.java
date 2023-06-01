package com.techelevator.tenmo.services;


import com.techelevator.tenmo.model.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.Scanner;

public class ConsoleService {

    private final Scanner scanner;
    private final UserService userService;
    private final TransferService transferService;
    private final AccountService accountService;
    private java.security.Principal Principal;

    public ConsoleService() {
        scanner = new Scanner(System.in);
        userService = new UserService();
        transferService = new TransferService();
        accountService = new AccountService();
    }

    public int promptForMenuSelection(String prompt) {
        int menuSelection;
        System.out.print(prompt);
        try {
            menuSelection = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            menuSelection = -1;
        }
        return menuSelection;
    }

    public void printGreeting() {
        System.out.println("*********************");
        System.out.println("* Welcome to TEnmo! *");
        System.out.println("*********************");
    }

    public void printLoginMenu() {
        System.out.println();
        System.out.println("1: Register");
        System.out.println("2: Login");
        System.out.println("0: Exit");
        System.out.println();
    }

    public void printMainMenu() {
        System.out.println();
        System.out.println("1: View your current balance");
        System.out.println("2: View your past transfers");
        System.out.println("3: View your pending requests");
        System.out.println("4: Send TE bucks");
        System.out.println("5: Request TE bucks");
        System.out.println("6: View transfer details");
        System.out.println("7: View all users");
        System.out.println("0: Exit");
        System.out.println();
    }

    public UserCredentials promptForCredentials() {
        String username = promptForString("Username: ");
        String password = promptForString("Password: ");
        return new UserCredentials(username, password);
    }

    public String promptForString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public int promptForInt(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }

    public BigDecimal promptForBigDecimal(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return new BigDecimal(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a decimal number.");
            }
        }
    }

    public boolean promptForTransferApproval(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                if (input.equalsIgnoreCase("Y")) {
                    return true;
                } else if (input.equalsIgnoreCase("N")) {
                    return false;
                } else {
                    throw new IllegalArgumentException("Invalid input. Please enter Y or N.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void pause() {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    public void printErrorMessage() {
        System.out.println("An error occurred. Check the log for details.");
    }

    public void printBalance(BigDecimal balance) {
        System.out.println("Your current balance: $" + balance);
    }
//TODO:create 3 new transfer service methods on transferDao, controller, and client side transfer service to fix print transfer.
    public void printTransfer(Transfer transfer) {
        System.out.println("--------------------------------------------");
        System.out.println("Transfer Details");
        System.out.println("--------------------------------------------");
        System.out.println("Transfer ID: " + transfer.getTransferId());
        System.out.println("From: " + transferService.getAccountFromUsername(transfer.getAccount_from()));
        System.out.println("To: " + transferService.getAccountFromUsername(transfer.getAccount_to()));
        System.out.println("Type: " + transferService.getTransferTypeDesc(transfer.getTransfer_type_id()));
        System.out.println("Status: " + transferService.getTransferStatusDesc(transfer.getTransfer_status_id()));
        System.out.println("Amount: $" + transfer.getAmount());
    }

    public void printTransfers(List<Transfer> transfers) {
        Principal principal = Principal;
        System.out.println("--------------------------------------------");
        System.out.println("Transfers");
        System.out.println("ID          From/To                 Amount");
        System.out.println("--------------------------------------------");
        for (Transfer transfer : transfers) {
            String direction;
            if (transferService.getAccountFromUsername(transfer.getAccount_from()).equals(userService.getUserByUsername(principal.getName()))){
                direction = "To:   " + transferService.getAccountFromUsername(transfer.getAccount_to());
            } else {
                direction = "From: " + transferService.getAccountFromUsername(transfer.getAccount_from());
            }
            System.out.printf("%-10s %-20s $%.2f%n", transfer.getTransferId(), direction, transfer.getAmount());
        }
    }



    public void printPendingTransfers(List<Transfer> transfers) {
        System.out.println("--------------------------------------------");
        System.out.println("Pending Transfers");
        System.out.println("ID          From                 Amount");
        System.out.println("--------------------------------------------");
        for (Transfer transfer : transfers) {
            System.out.printf("%-10s %-20s $%.2f%n", transfer.getTransferId(), userService.getUserById(transfer.getAccount_from()).getUsername(), transfer.getAmount());
        }
    }


    public void printUsers(List<User> users) {
        System.out.println("-------------------------------------------");
        System.out.println("Users");
        System.out.println("ID            Name");
        System.out.println("-------------------------------------------");
        for (User user : users) {
            System.out.println(user.getId() + "             " + user.getUsername());
        }
        System.out.println("-------------------------------------------");
    }


}
