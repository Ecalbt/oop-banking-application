package com.bankapp.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Transaction class representing a single banking transaction.
 * Records all account activities for audit and history purposes.
 */
public class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;
    private String transactionId;
    private String fromAccountNumber;
    private String toAccountNumber;
    private double amount;
    private String type; // DEPOSIT, WITHDRAWAL, TRANSFER_IN, TRANSFER_OUT, INTEREST, PENALTY
    private String description;
    private long timestamp;
    private String status; // SUCCESS, PENDING, FAILED

    /**
     * Constructor for Transaction.
     *
     * @param transactionId   Unique transaction identifier
     * @param fromAccountNumber Source account (null for deposits)
     * @param toAccountNumber Destination account (null for withdrawals)
     * @param amount          Transaction amount
     * @param type            Type of transaction
     * @param description     Transaction description
     */
    public Transaction(String transactionId, String fromAccountNumber, String toAccountNumber,
                       double amount, String type, String description) {
        this.transactionId = transactionId;
        this.fromAccountNumber = fromAccountNumber;
        this.toAccountNumber = toAccountNumber;
        this.amount = amount;
        this.type = type;
        this.description = description;
        this.timestamp = System.currentTimeMillis();
        this.status = "SUCCESS";
    }

    // ============= Getters =============

    public String getTransactionId() {
        return transactionId;
    }

    public String getFromAccountNumber() {
        return fromAccountNumber;
    }

    public String getToAccountNumber() {
        return toAccountNumber;
    }

    public double getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // ============= Utility Methods =============

    /**
     * Returns formatted date string for the transaction timestamp.
     *
     * @return Formatted date string (yyyy-MM-dd HH:mm:ss)
     */
    public String getFormattedDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(timestamp));
    }

    /**
     * Returns formatted transaction details.
     *
     * @return Formatted transaction string
     */
    public String getFormattedDetails() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-20s %s%n", "Transaction ID:", transactionId));
        sb.append(String.format("%-20s %s%n", "Type:", type));
        sb.append(String.format("%-20s $%.2f%n", "Amount:", amount));
        sb.append(String.format("%-20s %s%n", "Description:", description));
        sb.append(String.format("%-20s %s%n", "Date:", getFormattedDate()));
        sb.append(String.format("%-20s %s%n", "Status:", status));
        return sb.toString();
    }

    @Override
    public String toString() {
        return String.format("[%s] %s: $%.2f - %s (%s)", 
                getFormattedDate(), type, amount, description, status);
    }
}
