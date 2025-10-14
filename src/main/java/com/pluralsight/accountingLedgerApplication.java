package com.pluralsight;

import java.lang.invoke.SwitchPoint;
import java.util.Scanner;

public class accountingLedgerApplication {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

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

            int choice = scanner.nextLine().trim().toUpperCase(); //

            switch (choice) { //user's choice
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
    public static void addDeposit()




    public static void addPayment()






    public static void ledgerMenu() {

         {




    }
}
