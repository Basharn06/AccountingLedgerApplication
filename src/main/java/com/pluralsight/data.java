package com.pluralsight;

import java.math.BigDecimal;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class data {
    // private fields
    private LocalDate date;
    private LocalTime time;
    private String description;
    private String vendor;
    private BigDecimal amount;

    // formats for saving and reading date/time
    public static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("HH:mm:ss");

    // creates a transaction with all info given
    public data(LocalDate date, LocalTime time, String description, String vendor, BigDecimal amount) {
        this.date = date;
        this.time = time;
        this.description = description;
        this.vendor = vendor;
        this.amount = amount;
    }

    // makes a transaction using the current date/time
    public static data now(String description, String vendor, BigDecimal amount) {
        return new data(LocalDate.now(), LocalTime.now().withNano(0), description, vendor, amount);
    }

    // reads one CSV line and turns it into a data object
    public static data fromCsv(String line) {
        try {
            String[] parts = line.split("\\|");
            LocalDate date = LocalDate.parse(parts[0], DATE);
            LocalTime time = LocalTime.parse(parts[1], TIME);
            String description = parts[2];
            String vendor = parts[3];
            BigDecimal amount = new BigDecimal(parts[4]);
            return new data(date, time, description, vendor, amount);
        } catch (Exception e) {
            return null;
        }
    }

    // converts a data object into a CSV line for saving
    public String toCsv() {
        return String.join("|",
                date.format(DATE),
                time.format(TIME),
                description,
                vendor,
                amount.toPlainString());
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return String.format("%s | %s | %s | %s | %s",
                date.format(DATE),
                time.format(TIME),
                description,
                vendor,
                amount.toPlainString()) ;
    }
}
