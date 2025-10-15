package com.pluralsight;

import java.util.Scanner;
import java.io.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;

public class accountingLedgerApplication {

    public static final Scanner Scanner = new Scanner(System.in);

    public static void main(String[] args) {

        System.out.println("""
                
                Welcome To The Accounting Ledger:
                Please select one of the options below 
                
                D) Add Deposit 
                P) Make Payment (Debit)
                L) Ledger - display the ledger screen
                X) Exit - exit the application
                
                """
        );

        while (true) { // creating a loop for the main menu
            System.out.println("""
                
                Welcome To The Accounting Ledger:
                Please select one of the options below 
                
                D) Add Deposit 
                P) Make Payment (Debit)
                L) Ledger - display the ledger screen
                X) Exit - exit the application
                Choose: """
            );

            String choice = Scanner.nextLine().trim().toUpperCase(); // read choice

            switch (choice) { // user's choice
                case "D":
                    addDeposit();
                    break;
                case "P":
                    addPayment();
                    break;
                case "L":
                    ledgerMenu();
                    break;
                case "X":
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    public static void addDeposit() {
        System.out.print("Description Of Purchase");
        String desc = Scanner.nextLine().trim();
        System.out.print("Vendor: ");
        String vendor = Scanner.nextLine().trim();  // ask user for the info
        BigDecimal Check = readMoney("Amount (positive): "); // checks

        if (Check.compareTo(BigDecimal.ZERO) <= 0) { // checks to see if number is postive
            System.out.println("Amount must be positive.");
            return;
        }
        addRow(desc, vendor, Check); // positive for deposits
        System.out.println("Deposit saved.");
    }

    public static void addPayment() {
        System.out.print("Description: ");
        String desc = Scanner.nextLine().trim();
        System.out.print("Vendor: ");
        String vendor = Scanner.nextLine().trim();
        BigDecimal check = readMoney("Amount (positive): ");

        if (check.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("Amount must be positive.");
            return;
        }
        addRow(desc, vendor, check.negate()); // negative for payments
        System.out.println("Payment saved.");
    }

    public static void ledgerMenu() {
        // (implement your ledger options here as needed)
    }

    public static void addRow(String desc, String vendor, BigDecimal amount) {
        Transaction t = new Transaction(LocalDate.now(), LocalTime.now().withNano(0), desc, vendor, amount);
        ALL.add(t);
        appendCsv(t);
    }

    // this method makes reading a money value safely easier
    public static BigDecimal readMoney(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = Scanner.nextLine().trim();
            try {
                return new BigDecimal(s);
            } catch (Exception e) {
                System.out.println("Enter a valid number (e.g., 123.45).");
            }
        }
    }
}
