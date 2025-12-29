package com.bankapp.controllers;

import com.bankapp.model.Account;
import com.bankapp.model.Transaction;
import com.bankapp.model.User;
import com.bankapp.services.AuthService;
import com.bankapp.services.TransactionService;
import com.bankapp.utils.ConsoleUtils;
import com.bankapp.utils.InputValidator;
import java.util.List;

/**
 * TransactionController - Xử lý các thao tác giao dịch.
 * Implements the MVC Controller pattern - validates input and delegates to service layer.
 */
public class TransactionController {
    private final TransactionService transactionService;
    private final AuthService authService;

    /**
        * Constructor - khởi tạo với TransactionService.
     *
     * @param transactionService TransactionService instance
     * @param authService AuthService instance (để xác minh PIN)
     */
    public TransactionController(TransactionService transactionService, AuthService authService) {
        this.transactionService = transactionService;
        this.authService = authService;
    }

    /**
        * Xử lý thao tác nạp tiền.
     *
     * @param account Account to deposit to
     * @param user    Owner user (để xác minh PIN)
     * @return true if deposit successful, false otherwise
     */
    public boolean handleDeposit(Account account, User user) {
        ConsoleUtils.printHeader("DEPOSIT MONEY");
        ConsoleUtils.printInfo("Account: " + account.getAccountNumber() + 
                " (" + account.getAccountType() + ")");

        if (!verifyPin(user)) {
            return false;
        }

        // Get amount
        double amount = ConsoleUtils.readDouble("Enter deposit amount: $");
        if (!InputValidator.isValidAmount(amount)) {
            ConsoleUtils.printError("Invalid amount");
            return false;
        }

        // Get description
        String description = ConsoleUtils.readString("Enter description (optional): ");
        if (description.isEmpty()) {
            description = "Deposit";
        }

        // Perform deposit
        if (transactionService.deposit(account.getAccountNumber(), amount, description)) {
            ConsoleUtils.printSuccess("Deposit successful!");
            ConsoleUtils.printInfo("New Balance: " + ConsoleUtils.formatAmount(account.getBalance() + amount));
            return true;
        } else {
            ConsoleUtils.printError("Deposit failed");
            return false;
        }
    }

    /**
        * Xử lý thao tác rút tiền.
     *
     * @param account Account to withdraw from
     * @param user    Owner user (để xác minh PIN)
     * @return true if withdrawal successful, false otherwise
     */
    public boolean handleWithdraw(Account account, User user) {
        ConsoleUtils.printHeader("WITHDRAW MONEY");
        ConsoleUtils.printInfo("Account: " + account.getAccountNumber() + 
                " (" + account.getAccountType() + ")");
        ConsoleUtils.printInfo("Current Balance: " + ConsoleUtils.formatAmount(account.getBalance()));

        if (!verifyPin(user)) {
            return false;
        }

        // Get amount
        double amount = ConsoleUtils.readDouble("Enter withdrawal amount: $");
        if (!InputValidator.isValidAmount(amount)) {
            ConsoleUtils.printError("Invalid amount");
            return false;
        }

        // Check balance
        if (amount > account.getBalance()) {
            ConsoleUtils.printError("Insufficient balance");
            return false;
        }

        // Get description
        String description = ConsoleUtils.readString("Enter description (optional): ");
        if (description.isEmpty()) {
            description = "Withdrawal";
        }

        // Perform withdrawal
        if (transactionService.withdraw(account.getAccountNumber(), amount, description)) {
            ConsoleUtils.printSuccess("Withdrawal successful!");
            ConsoleUtils.printInfo("New Balance: " + ConsoleUtils.formatAmount(account.getBalance() - amount));
            return true;
        } else {
            ConsoleUtils.printError("Withdrawal failed - check account restrictions");
            return false;
        }
    }

    /**
        * Xử lý thao tác chuyển tiền.
     *
     * @param fromAccount Source account
     * @param user Current user (to validate destination account)
     * @return true if transfer successful, false otherwise
     */
    public boolean handleTransfer(Account fromAccount, User user) {
        ConsoleUtils.printHeader("TRANSFER MONEY");
        ConsoleUtils.printInfo("From Account: " + fromAccount.getAccountNumber() + 
                " (" + fromAccount.getAccountType() + ")");
        ConsoleUtils.printInfo("Current Balance: " + ConsoleUtils.formatAmount(fromAccount.getBalance()));

        if (!verifyPin(user)) {
            return false;
        }

        // Get destination account number
        String toAccountNumber = ConsoleUtils.readString("Enter destination account number: ");
        if (!InputValidator.isValidAccountNumber(toAccountNumber)) {
            ConsoleUtils.printError("Invalid account number");
            return false;
        }

        // Get amount
        double amount = ConsoleUtils.readDouble("Enter transfer amount: $");
        if (!InputValidator.isValidAmount(amount)) {
            ConsoleUtils.printError("Invalid amount");
            return false;
        }

        // Check balance
        if (amount > fromAccount.getBalance()) {
            ConsoleUtils.printError("Insufficient balance");
            return false;
        }

        // Get description
        String description = ConsoleUtils.readString("Enter transfer reason (optional): ");
        if (description.isEmpty()) {
            description = "Transfer";
        }

        // Perform transfer
        if (transactionService.transfer(fromAccount.getAccountNumber(), toAccountNumber, amount, description)) {
            ConsoleUtils.printSuccess("Transfer successful!");
            ConsoleUtils.printInfo("Recipient Account: " + toAccountNumber);
            ConsoleUtils.printInfo("Amount Transferred: " + ConsoleUtils.formatAmount(amount));
            ConsoleUtils.printInfo("New Balance: " + ConsoleUtils.formatAmount(fromAccount.getBalance() - amount));
            return true;
        } else {
            ConsoleUtils.printError("Transfer failed - recipient account may not exist");
            return false;
        }
    }

    /**
        * Hiển thị lịch sử giao dịch của một tài khoản.
     *
     * @param account Account to display history for
     */
    public void displayTransactionHistory(Account account) {
        ConsoleUtils.printSubHeader("TRANSACTION HISTORY");
        ConsoleUtils.printInfo("Account: " + account.getAccountNumber());

        List<Transaction> transactions = account.getTransactions();
        if (transactions.isEmpty()) {
            ConsoleUtils.printInfo("No transactions found");
            return;
        }

        System.out.println();
        for (Transaction txn : transactions) {
            System.out.println(txn.toString());
        }
    }

    /**
        * Hiển thị thông tin chi tiết về một giao dịch cụ thể.
     *
     * @param transaction Transaction to display
     */
    public void displayTransactionDetails(Transaction transaction) {
        ConsoleUtils.printSubHeader("TRANSACTION DETAILS");
        System.out.println(transaction.getFormattedDetails());
    }

    /**
     * Yêu cầu và xác minh PIN trước khi thực hiện giao dịch.
     */
    private boolean verifyPin(User user) {
        if (user == null) {
            ConsoleUtils.printError("No user session found");
            return false;
        }

        if (authService.isPinLocked(user)) {
            ConsoleUtils.printError("PIN locked. Contact support to reset.");
            return false;
        }

        String pin = ConsoleUtils.readString("Enter transaction PIN: ");
        if (!InputValidator.isValidPin(pin)) {
            ConsoleUtils.printError("Invalid PIN format (4-6 digits)");
            return false;
        }

        boolean ok = authService.verifyPin(user, pin);
        if (!ok) {
            ConsoleUtils.printError("Incorrect PIN. Remaining attempts: " + authService.getRemainingPinAttempts(user));
        }
        return ok;
    }
}
