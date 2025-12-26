package com.bankapp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp trừu tượng cơ sở cho tất cả các loại tài khoản.
 * Thể hiện nguyên lý Trừu tượng - định nghĩa giao diện chung cho mọi tài khoản.
 * Sử dụng Bao đóng (Encapsulation) - che giấu chi tiết triển khai bên trong.
 */
public abstract class Account implements Serializable {
    private static final long serialVersionUID = 1L;
    protected String accountNumber;
    protected double balance;
    protected long createdAt;
    protected List<Transaction> transactions;
    protected boolean isActive;

    /**
     * Constructor cho Account.
     *
     * @param accountNumber Mã định danh duy nhất của tài khoản
     * @param initialBalance Số dư ban đầu của tài khoản
     */
    public Account(String accountNumber, double initialBalance) {
        this.accountNumber = accountNumber;
        this.balance = initialBalance;
        this.createdAt = System.currentTimeMillis();
        this.transactions = new ArrayList<>();
        this.isActive = true;
    }

    // ============= Getters =============

    public String getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public List<Transaction> getTransactions() {
        return new ArrayList<>(transactions);
    }

    public boolean isActive() {
        return isActive;
    }

    // ============= Abstract Methods =============

    /**
     * Phương thức trừu tượng để lấy loại tài khoản.
     * Thể hiện tính Đa hình - mỗi lớp con hiện thực khác nhau.
     *
     * @return Kiểu tài khoản dạng chuỗi
     */
    public abstract String getAccountType();

    /**
     * Phương thức trừu tượng kiểm tra điều kiện rút tiền.
     * Mỗi loại tài khoản có quy tắc rút tiền khác nhau.
     *
     * @param amount Số tiền cần rút
     * @return true nếu được phép rút, false nếu không
     */
    public abstract boolean canWithdraw(double amount);

    /**
     * Phương thức trừu tượng áp dụng các quy tắc riêng của từng loại tài khoản.
     * Ví dụ: SavingsAccount có thể áp dụng phạt hoặc lãi suất.
     */
    public abstract void applyAccountSpecificRules();

    // ============= Transaction Methods =============

    /**
     * Nạp tiền vào tài khoản.
     *
     * @param amount  Số tiền cần nạp
     * @param description Mô tả giao dịch nạp tiền
     * @return true nếu nạp thành công, false nếu thất bại
     */
    public boolean deposit(double amount, String description) {
        if (amount <= 0) {
            return false;
        }
        this.balance += amount;
        Transaction transaction = new Transaction(
                "DEP-" + System.nanoTime(),
                this.accountNumber,
                null,
                amount,
                "DEPOSIT",
                description
        );
        this.transactions.add(transaction);
        return true;
    }

    /**
     * Rút tiền khỏi tài khoản.
     *
     * @param amount  Số tiền cần rút
     * @param description Mô tả giao dịch rút tiền
     * @return true nếu rút thành công, false nếu thất bại
     */
    public boolean withdraw(double amount, String description) {
        if (amount <= 0 || !canWithdraw(amount) || this.balance < amount) {
            return false;
        }
        this.balance -= amount;
        Transaction transaction = new Transaction(
                "WTH-" + System.nanoTime(),
                this.accountNumber,
                null,
                amount,
                "WITHDRAWAL",
                description
        );
        this.transactions.add(transaction);
        applyAccountSpecificRules();
        return true;
    }

    /**
     * Ghi nhận giao dịch chuyển tiền đi (gửi tiền sang tài khoản khác).
     *
     * @param amount  Số tiền cần chuyển
     * @param toAccountNumber Số tài khoản nhận
     * @return true nếu ghi nhận thành công, false nếu thất bại
     */
    public boolean transfer(double amount, String toAccountNumber) {
        if (amount <= 0 || !canWithdraw(amount) || this.balance < amount) {
            return false;
        }
        this.balance -= amount;
        Transaction transaction = new Transaction(
                "TRF-" + System.nanoTime(),
                this.accountNumber,
                toAccountNumber,
                amount,
                "TRANSFER_OUT",
                "Transfer to " + toAccountNumber
        );
        this.transactions.add(transaction);
        applyAccountSpecificRules();
        return true;
    }

    /**
     * Ghi nhận giao dịch nhận tiền (nhận tiền từ tài khoản khác).
     *
     * @param amount  Số tiền nhận được
     * @param fromAccountNumber Số tài khoản gửi
     */
    public void receiveTransfer(double amount, String fromAccountNumber) {
        this.balance += amount;
        Transaction transaction = new Transaction(
                "TRF-" + System.nanoTime(),
                this.accountNumber,
                fromAccountNumber,
                amount,
                "TRANSFER_IN",
                "Transfer from " + fromAccountNumber
        );
        this.transactions.add(transaction);
    }

    /**
     * Đóng tài khoản (chuyển trạng thái sang không hoạt động).
     */
    public void closeAccount() {
        this.isActive = false;
    }

    /**
     * Lấy các giao dịch gần đây (n giao dịch cuối cùng).
     *
     * @param count Số lượng giao dịch gần nhất cần lấy
     * @return Danh sách các giao dịch gần đây
     */
    public List<Transaction> getRecentTransactions(int count) {
        int size = transactions.size();
        int startIndex = Math.max(0, size - count);
        return new ArrayList<>(transactions.subList(startIndex, size));
    }

    @Override
    public String toString() {
        return getAccountType() + "{" +
                "accountNumber='" + accountNumber + '\'' +
                ", balance=" + balance +
                ", isActive=" + isActive +
                ", transactionCount=" + transactions.size() +
                '}';
    }
}
