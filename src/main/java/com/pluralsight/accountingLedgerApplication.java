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

    // main start // what it does: load old rows, show the menu loop
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

    // addDeposit // what it does: asks details and saves a positive row
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

    // addPayment // what it does: asks details and saves a negative row
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

    // ledgerMenu // what it does: for now just shows all so L does something useful
    public static void ledgerMenu() {
        showAll(); // simple: print everything newest first
    }

    // addRow // what it does: creates a data object with now() and appends to csv + memory
    public static void addRow(String desc, String vendor, BigDecimal amount) {
        data row = data.now(desc, vendor, amount); // data.now = build row w/ current date/time
        ALL.add(row);                               // keep in memory
        appendCsv(row);                             // save to file
    }

    // readMoney // what it does: keep asking till user types a valid money number
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

    // loadCsv // what it does: read every line from transactions.csv into ALL
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

    // appendCsv // what it does: append exactly one row to the csv file
    public static void appendCsv(data row) {
        try (PrintWriter out = new PrintWriter(new FileWriter(CSV_PATH, true))) { // true = append
            out.println(row.toCsv()); // use your data.toCsv builder (date|time|desc|vendor|amount)
        } catch (IOException ex) {
            System.err.println("Write error: " + ex.getMessage());
        }
    }

    // showAll // what it does: prints all rows in a simple table, newest first
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
}
