package com.pluralsight;

import java.util.Scanner;   // Scanner = reads user typing from keyboard
import java.io.*;           // file stuff (read/write)
import java.math.BigDecimal; // BigDecimal = safe number for money (no rounding issues)
import java.time.*;         // date/time (LocalDate, LocalTime)
import java.util.*;         // ArrayList, Comparator, etc.

public class accountingLedgerApplication {

    // shared stuff for the whole app
    public static final Scanner Scanner = new Scanner(System.in);      // one Scanner for input
    public static final String CSV_PATH = "transactions.csv";          // where we save the rows
    public static final ArrayList<data> ALL = new ArrayList<>();       // in-memory list of all rows (data objects)

    // load old rows, show the menu loop
    public static void main(String[] args) {
        loadCsv();  // pull in any existing transactions from the csv so L can show them

        while (true) { // main loop // keeps asking till user exits
            System.out.print("""
                
                Welcome To The Accounting Ledger:
                Please select one of the options below 
                
                D) Add Deposit 
                P) Make Payment (Debit)
                L) Ledger - display the ledger screen
                X) Exit - exit the application
                Choose: """
            );

            String choice = Scanner.nextLine().trim().toUpperCase(); // read choice (trim = remove spaces)

            switch (choice) { // route to the feature they picked
                case "D":
                    addDeposit(); // money in (positive)
                    break;
                case "P":
                    addPayment(); // money out (negative)
                    break;
                case "L":
                    ledgerMenu(); // show ledger screen (i’m keeping it simple)
                    break;
                case "X":
                    System.out.println("Goodbye!");
                    return; // end program
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    // asks details and saves a positive row
    public static void addDeposit() {
        System.out.print("Description Of Purchase"); // per your exact string
        String desc = Scanner.nextLine().trim();

        System.out.print("Vendor: ");
        String vendor = Scanner.nextLine().trim();

        BigDecimal Check = readMoney("Amount (positive): "); // read money safely

        if (Check.compareTo(BigDecimal.ZERO) <= 0) { // must be > 0 for deposit
            System.out.println("Amount must be positive.");
            return;
        }

        addRow(desc, vendor, Check); // positive = deposit
        System.out.println("Deposit saved.");
    }

    // asks details and saves a negative row
    public static void addPayment() {
        System.out.print("Description: ");
        String desc = Scanner.nextLine().trim();

        System.out.print("Vendor: ");
        String vendor = Scanner.nextLine().trim();

        BigDecimal check = readMoney("Amount (positive): "); // user types positive

        if (check.compareTo(BigDecimal.ZERO) <= 0) { // must be > 0 before we flip it
            System.out.println("Amount must be positive.");
            return;
        }

        addRow(desc, vendor, check.negate()); // negate = make negative for payment
        System.out.println("Payment saved.");
    }

    // for now just shows all so L does something useful
    public static void ledgerMenu() {

                while (true) {
                    System.out.println("""
                
                Ledger Menu:
                A) All - Display all entries
                D) Deposits - Display only the entries that are deposits into the account
                P) Payments - Display only the negative entries (or payments)
                R) Reports - Run pre-defined reports or a custom search
                H) Home - go back to the home page
                """);

                    System.out.print("Choose: ");
                    String choice = Scanner.nextLine().trim().toUpperCase();

                    switch (choice) { // classic switch version
                        case "A":
                            showAll();       // show everything
                            break;
                        case "D":
                            showDeposits();  // show deposits only
                            break;
                        case "P":
                            showPayments();  // show payments only
                            break;
                        case "R":
                            reportsMenu();   // go to reports page
                            break;
                        case "H":
                            return;          // go back to home screen
                        default:
                            System.out.println("Invalid choice."); // catch wrong inputs
                            break;

                    }
                }
    }

    // creates a data object with now() and appends to csv + memory
    public static void addRow(String desc, String vendor, BigDecimal amount) {
        data row = data.now(desc, vendor, amount); // data.now = build row w/ current date/time
        ALL.add(row);                               // keep in memory
        appendCsv(row);                             // save to file
    }

    // keep asking till user types a valid money number
    public static BigDecimal readMoney(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = Scanner.nextLine().trim();
            try {
                return new BigDecimal(s); // parse text → BigDecimal (money)
            } catch (Exception e) {
                System.out.println("Enter a valid number (e.g., 123.45).");
            }
        }
    }

    // read every line from transactions.csv into ALL
    public static void loadCsv() {
        File f = new File(CSV_PATH); // the csv file
        if (!f.exists()) return;     // no file yet = nothing to load

        try (BufferedReader br = new BufferedReader(new FileReader(f))) { // open file reader
            String line;
            while ((line = br.readLine()) != null) { // read line by line
                data row = data.fromCsv(line);       // use your data.fromCsv parser
                if (row != null) ALL.add(row);       // add good lines to memory
            }
        } catch (IOException ex) {
            System.err.println("Read error: " + ex.getMessage()); // show file error
        }
    }

   // append exactly one row to the csv file
    public static void appendCsv(data row) {
        try (PrintWriter out = new PrintWriter(new FileWriter(CSV_PATH, true))) { // true = append
            out.println(row.toCsv()); // use your data.toCsv builder (date|time|desc|vendor|amount)
        } catch (IOException ex) {
            System.err.println("Write error: " + ex.getMessage());
        }
    }

   //  prints all rows in a simple table, newest first
    public static void showAll() {
        if (ALL.isEmpty()) {
            System.out.println("\n(no entries)\n");
            return;
        }

        // make a copy and sort by date + time descending (newest first)
        ArrayList<data> sorted = new ArrayList<>(ALL); // copy so we don’t reorder ALL
        sorted.sort(
                Comparator.comparing(data::getDate)
                        .thenComparing(data::getTime)
                        .reversed()
        );

        System.out.println("\nDate       | Time     | Description          | Vendor            | Amount");
        System.out.println("-----------+----------+----------------------+-------------------+-----------");
        for (data d : sorted) {
            System.out.printf("%s | %-8s | %-20s | %-17s | %s%n",
                    d.getDate(), d.getTime(), d.getDescription(), d.getVendor(), d.getAmount().toPlainString());
        }
        System.out.println();
    }

    // shows only deposit transactions (money in)
    public static void showDeposits() {

        ArrayList<data> deposits = new ArrayList<>(); // make a new list to store just deposits

        // go through every transaction in ALL
        for (data d : ALL) {
            if (d.getAmount().compareTo(BigDecimal.ZERO) > 0) { // if amount > 0, it’s a deposit
                deposits.add(d); // add it to our deposits list
            }
        }

        // if no deposits, say so
        if (deposits.isEmpty()) {
            System.out.println("\n(No deposit entries found)\n");
            return;
        }

        // sort them by date & time (newest first)
        deposits.sort(
                Comparator.comparing(data::getDate)
                        .thenComparing(data::getTime)
                        .reversed()
        );

        // print table header
        System.out.println("\nDate       | Time     | Description          | Vendor            | Amount");
        System.out.println("-----------+----------+----------------------+-------------------+-----------");

        // print every deposit
        for (data d : deposits) {
            System.out.printf("%s | %-8s | %-20s | %-17s | %s%n",
                    d.getDate(),
                    d.getTime(),
                    d.getDescription(),
                    d.getVendor(),
                    d.getAmount().toPlainString()); // show amount
        }

        System.out.println(); // just an empty line at end for spacing
    }

    // shows only payment transactions (money out)
    public static void showPayments() {

        ArrayList<data> payments = new ArrayList<>(); // make a new list just for payments

        // go through all transactions
        for (data d : ALL) {
            if (d.getAmount().compareTo(BigDecimal.ZERO) < 0) { // if amount < 0 → payment
                payments.add(d); // add it to the payments list
            }
        }

        // if no payments found
        if (payments.isEmpty()) {
            System.out.println("\n(No payment entries found)\n");
            return;
        }

        // sort newest first
        payments.sort(
                Comparator.comparing(data::getDate)
                        .thenComparing(data::getTime)
                        .reversed()
        );

        // print header row
        System.out.println("\nDate       | Time     | Description          | Vendor            | Amount");
        System.out.println("-----------+----------+----------------------+-------------------+-----------");

        // print every payment
        for (data d : payments) {
            System.out.printf("%s | %-8s | %-20s | %-17s | %s%n",
                    d.getDate(),
                    d.getTime(),
                    d.getDescription(),
                    d.getVendor(),
                    d.getAmount().toPlainString()); // show amount
        }

        System.out.println();
    }

    public static void reportsMenu() {





    }



}
