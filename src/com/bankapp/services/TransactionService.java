package com.bankapp.services;

import com.bankapp.data.InMemoryDataStore;
import com.bankapp.data.AccountRepository;
import com.bankapp.data.TransactionRepository;
import com.bankapp.model.Account;
import com.bankapp.model.Transaction;
import java.util.List;

/**
 * TransactionService - Xử lý các thao tác giao dịch (nạp, rút, chuyển khoản).
 * Tuân theo nguyên lý Trách nhiệm đơn (SRP) - chỉ tập trung vào logic giao dịch.
 */
public class TransactionService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    /**
     * Constructor - khởi tạo với kho dữ liệu.
     */
    public TransactionService() {
        this.accountRepository = InMemoryDataStore.getInstance().getAccountRepository();
        this.transactionRepository = InMemoryDataStore.getInstance().getTransactionRepository();
    }

    /**
     * Nạp tiền vào một tài khoản.
     *
     * @param accountNumber Số tài khoản cần nạp
     * @param amount Số tiền cần nạp
     * @param description Mô tả giao dịch
     * @return true nếu nạp thành công, false nếu thất bại
     */
    public boolean deposit(String accountNumber, double amount, String description) {
        if (amount <= 0) {
            return false;
        }

        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null || !account.isActive()) {
            return false;
        }

        if (account.deposit(amount, description)) {
            accountRepository.update(account);
            // Record transaction (the account already created the transaction internally)
            return true;
        }
        return false;
    }

    /**
     * Rút tiền từ một tài khoản.
     *
     * @param accountNumber Số tài khoản cần rút
     * @param amount Số tiền cần rút
     * @param description Mô tả giao dịch
     * @return true nếu rút thành công, false nếu thất bại
     */
    public boolean withdraw(String accountNumber, double amount, String description) {
        if (amount <= 0) {
            return false;
        }

        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null || !account.isActive()) {
            return false;
        }

        if (account.withdraw(amount, description)) {
            accountRepository.update(account);
            return true;
        }
        return false;
    }

    /**
     * Chuyển tiền giữa hai tài khoản.
     *
     * @param fromAccountNumber Số tài khoản nguồn
     * @param toAccountNumber Số tài khoản đích
     * @param amount Số tiền cần chuyển
     * @param description Mô tả giao dịch
     * @return true nếu chuyển thành công, false nếu thất bại
     */
    public boolean transfer(String fromAccountNumber, String toAccountNumber, double amount, String description) {
        if (amount <= 0) {
            return false;
        }

        // Validate accounts
        Account fromAccount = accountRepository.findByAccountNumber(fromAccountNumber);
        Account toAccount = accountRepository.findByAccountNumber(toAccountNumber);

        if (fromAccount == null || toAccount == null ||
                !fromAccount.isActive() || !toAccount.isActive()) {
            return false;
        }

        // Check if transfer is possible
        if (!fromAccount.canWithdraw(amount) || fromAccount.getBalance() < amount) {
            return false;
        }

        // Perform transfer
        if (fromAccount.transfer(amount, toAccountNumber)) {
            toAccount.receiveTransfer(amount, fromAccountNumber);

            // Update both accounts
            accountRepository.update(fromAccount);
            accountRepository.update(toAccount);
            return true;
        }
        return false;
    }

    /**
     * Lấy lịch sử giao dịch của một tài khoản.
     *
     * @param accountNumber Số tài khoản cần lấy lịch sử
     * @return Danh sách giao dịch
     */
    public List<Transaction> getTransactionHistory(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account != null) {
            return account.getTransactions();
        }
        return List.of();
    }

    /**
     * Lấy các giao dịch gần đây của một tài khoản.
     *
     * @param accountNumber Số tài khoản cần lấy lịch sử
     * @param count Số lượng giao dịch gần nhất cần lấy
     * @return Danh sách các giao dịch gần đây
     */
    public List<Transaction> getRecentTransactions(String accountNumber, int count) {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account != null) {
            return account.getRecentTransactions(count);
        }
        return List.of();
    }

    /**
     * Lấy thông tin một giao dịch cụ thể theo ID.
     *
     * @param transactionId ID giao dịch cần tìm
     * @return Đối tượng Transaction nếu tìm thấy, null nếu không
     */
    public Transaction getTransaction(String transactionId) {
        return transactionRepository.findById(transactionId);
    }
}
