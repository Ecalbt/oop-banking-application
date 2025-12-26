package com.bankapp.services;

import com.bankapp.model.SavingsAccount;
import java.util.HashMap;
import java.util.Map;

/**
 * BankService - Service facade điều phối nhiều service khác.
 * Cung cấp một giao diện thống nhất cho các thao tác ngân hàng.
 * Áp dụng mẫu thiết kế Facade để đơn giản hóa việc tương tác từ phía client.
 */
public class BankService {
    private final AuthService authService;
    private final AccountService accountService;
    private final TransactionService transactionService;
    private final Map<String, SavingsAccount> interestRates; // For demonstration

    /**
     * Constructor - khởi tạo tất cả các service con.
     */
    public BankService() {
        this.authService = new AuthService();
        this.accountService = new AccountService();
        this.transactionService = new TransactionService();
        this.interestRates = new HashMap<>();
    }

    /**
     * Lấy AuthService.
     *
     * @return Đối tượng AuthService
     */
    public AuthService getAuthService() {
        return authService;
    }

    /**
     * Lấy AccountService.
     *
     * @return Đối tượng AccountService
     */
    public AccountService getAccountService() {
        return accountService;
    }

    /**
     * Lấy TransactionService.
     *
     * @return Đối tượng TransactionService
     */
    public TransactionService getTransactionService() {
        return transactionService;
    }

    /**
     * Lấy thống kê hệ thống.
     *
     * @return Chuỗi tóm tắt thống kê
     */
    public String getSystemStats() {
        StringBuilder stats = new StringBuilder();
        stats.append("=== BANK SYSTEM STATISTICS ===\n");
        stats.append(String.format("Total Users: %d%n",
                authService.getUserById("USER_001") != null ? "N/A" : 0));
        stats.append(String.format("Total Accounts: %d%n", accountService.accountExists("ACC_001") ? "N/A" : 0));
        return stats.toString();
    }
}
