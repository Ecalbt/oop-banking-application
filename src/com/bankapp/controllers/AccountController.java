package com.bankapp.controllers;

import com.bankapp.model.Account;
import com.bankapp.model.CheckingAccount;
import com.bankapp.model.SavingsAccount;
import com.bankapp.model.User;
import com.bankapp.services.AccountService;
import com.bankapp.services.TransactionService;
import com.bankapp.utils.ConsoleUtils;
import com.bankapp.utils.InputValidator;
import java.util.List;

/**
 * AccountController - Xử lý các thao tác liên quan đến tài khoản.
 * Áp dụng mẫu Controller trong MVC - kiểm tra dữ liệu đầu vào và ủy quyền cho tầng service.
 */
public class AccountController {
    private final AccountService accountService;
    private final TransactionService transactionService;

    /**
     * Constructor - khởi tạo với các service cần thiết.
     *
     * @param accountService Đối tượng AccountService
     * @param transactionService Đối tượng TransactionService
     */
    public AccountController(AccountService accountService, TransactionService transactionService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    /**
     * Hiển thị tất cả tài khoản của một người dùng.
     *
     * @param user Đối tượng User
     */
    public void displayUserAccounts(User user) {
        List<Account> accounts = user.getAccounts();
        if (accounts.isEmpty()) {
            ConsoleUtils.printInfo("No accounts found");
            return;
        }

        ConsoleUtils.printSubHeader("Your Accounts");
        for (int i = 0; i < accounts.size(); i++) {
            Account account = accounts.get(i);
            System.out.println((i + 1) + ". " + account.getAccountType() + " Account: " +
                    account.getAccountNumber() + " | Balance: " +
                    ConsoleUtils.formatAmount(account.getBalance()));
        }
    }

    /**
     * Xử lý mở tài khoản thanh toán (checking account) mới.
     *
     * @param user Đối tượng User
     * @return Đối tượng CheckingAccount nếu thành công, null nếu thất bại
     */
    public CheckingAccount handleOpenCheckingAccount(User user) {
        ConsoleUtils.printHeader("OPEN CHECKING ACCOUNT");

        // Get initial balance
        double initialBalance = ConsoleUtils.readDouble("Enter initial deposit amount: $");
        if (initialBalance < 0 || !InputValidator.isValidAmount(initialBalance)) {
            ConsoleUtils.printError("Invalid amount");
            return null;
        }

        // Get overdraft limit
        double overdraftLimit = ConsoleUtils.readDouble("Enter overdraft limit (optional, press 0 for none): $");
        if (overdraftLimit < 0) {
            ConsoleUtils.printError("Invalid overdraft limit");
            return null;
        }

        // Create account
        CheckingAccount account = accountService.createCheckingAccount(user, initialBalance, overdraftLimit);
        if (account != null) {
            ConsoleUtils.printSuccess("Checking account created successfully!");
            ConsoleUtils.printInfo("Account Number: " + account.getAccountNumber());
            ConsoleUtils.printInfo("Initial Balance: " + ConsoleUtils.formatAmount(account.getBalance()));
            return account;
        } else {
            ConsoleUtils.printError("Failed to create checking account");
            return null;
        }
    }

    /**
     * Xử lý mở tài khoản tiết kiệm (savings account) mới.
     *
     * @param user Đối tượng User
     * @return Đối tượng SavingsAccount nếu thành công, null nếu thất bại
     */
    public SavingsAccount handleOpenSavingsAccount(User user) {
        ConsoleUtils.printHeader("OPEN SAVINGS ACCOUNT");

        // Get initial balance
        double initialBalance = ConsoleUtils.readDouble("Enter initial deposit amount: $");
        if (initialBalance < 0 || !InputValidator.isValidAmount(initialBalance)) {
            ConsoleUtils.printError("Invalid amount");
            return null;
        }

        // Get interest rate
        double interestRate = ConsoleUtils.readDouble("Enter annual interest rate (e.g., 0.025 for 2.5%): ");
        if (interestRate < 0 || interestRate > 1) {
            ConsoleUtils.printError("Invalid interest rate (must be between 0 and 1)");
            return null;
        }

        // Create account
        SavingsAccount account = accountService.createSavingsAccount(user, initialBalance, interestRate);
        if (account != null) {
            ConsoleUtils.printSuccess("Savings account created successfully!");
            ConsoleUtils.printInfo("Account Number: " + account.getAccountNumber());
            ConsoleUtils.printInfo("Initial Balance: " + ConsoleUtils.formatAmount(account.getBalance()));
            ConsoleUtils.printInfo("Interest Rate: " + String.format("%.2f%%", interestRate * 100));
            return account;
        } else {
            ConsoleUtils.printError("Failed to create savings account");
            return null;
        }
    }

    /**
     * Chọn một tài khoản từ danh sách tài khoản của người dùng.
     *
     * @param user Đối tượng User
     * @return Tài khoản được chọn hoặc null nếu không hợp lệ
     */
    public Account selectAccount(User user) {
        List<Account> accounts = user.getAccounts();
        if (accounts.isEmpty()) {
            ConsoleUtils.printError("No accounts available");
            return null;
        }

        displayUserAccounts(user);
        String input = ConsoleUtils.readString("Select account (enter number or account number): ");
        
        // Try parsing as index number first (1, 2, 3, etc.)
        try {
            int choice = Integer.parseInt(input);
            if (choice >= 1 && choice <= accounts.size()) {
                return accounts.get(choice - 1);
            }
        } catch (NumberFormatException e) {
            // Not a number, try matching account number directly
        }
        
        // Try matching account number (ACC123456)
        for (Account account : accounts) {
            if (account.getAccountNumber().equals(input)) {
                return account;
            }
        }
        
        ConsoleUtils.printError("Invalid selection");
        return null;
    }

    /**
     * Hiển thị chi tiết tài khoản.
     *
     * @param account Tài khoản cần hiển thị
     */
    public void displayAccountDetails(Account account) {
        ConsoleUtils.printSubHeader("ACCOUNT DETAILS");
        System.out.println("Account Number: " + account.getAccountNumber());
        System.out.println("Account Type: " + account.getAccountType());
        System.out.println("Balance: " + ConsoleUtils.formatAmount(account.getBalance()));
        System.out.println("Status: " + (account.isActive() ? "Active" : "Inactive"));
        System.out.println("Total Transactions: " + account.getTransactions().size());

        if (account instanceof SavingsAccount) {
            SavingsAccount sa = (SavingsAccount) account;
            System.out.println("Interest Rate: " + String.format("%.2f%%", sa.getInterestRate() * 100));
            System.out.println("Monthly Withdrawals: " + sa.getWithdrawalsThisMonth() + "/6");
        }
    }
}
