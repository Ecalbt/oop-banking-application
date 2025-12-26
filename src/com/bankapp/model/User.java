package com.bankapp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp User đại diện cho một khách hàng ngân hàng.
 * Bao đóng thông tin người dùng và quản lý các tài khoản liên kết.
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String userId;
    private String username;
    private String passwordHash;
    private String fullName;
    private String email;
    private List<Account> accounts;
    private long createdAt;

    /**
     * Constructor tạo một User mới.
     *
     * @param userId       Mã định danh duy nhất cho người dùng
     * @param username     Tên đăng nhập
     * @param passwordHash Mật khẩu đã được băm để bảo mật
     * @param fullName     Họ tên đầy đủ của người dùng
     * @param email        Địa chỉ email của người dùng
     */
    public User(String userId, String username, String passwordHash, String fullName, String email) {
        this.userId = userId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
        this.email = email;
        this.accounts = new ArrayList<>();
        this.createdAt = System.currentTimeMillis();
    }

    // ============= Getters and Setters =============

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Account> getAccounts() {
        return new ArrayList<>(accounts);
    }

    public long getCreatedAt() {
        return createdAt;
    }

    // ============= Account Management =============

    /**
     * Thêm một tài khoản vào danh sách tài khoản của người dùng.
     * Ngăn chặn việc thêm trùng tài khoản.
     *
     * @param account Tài khoản cần thêm
     * @return true nếu thêm thành công, false nếu không
     */
    public boolean addAccount(Account account) {
        if (account != null && !accounts.contains(account)) {
            return accounts.add(account);
        }
        return false;
    }

    /**
     * Lấy một tài khoản theo số tài khoản.
     *
     * @param accountNumber Số tài khoản cần tìm
     * @return Đối tượng Account nếu tìm thấy, null nếu không
     */
    public Account getAccountByNumber(String accountNumber) {
        return accounts.stream()
                .filter(acc -> acc.getAccountNumber().equals(accountNumber))
                .findFirst()
                .orElse(null);
    }

    /**
     * Lấy tất cả tài khoản thanh toán (checking) của người dùng.
     *
     * @return Danh sách các đối tượng CheckingAccount
     */
    public List<Account> getCheckingAccounts() {
        List<Account> checkingAccounts = new ArrayList<>();
        for (Account account : accounts) {
            if (account instanceof CheckingAccount) {
                checkingAccounts.add(account);
            }
        }
        return checkingAccounts;
    }

    /**
     * Lấy tất cả tài khoản tiết kiệm (savings) của người dùng.
     *
     * @return Danh sách các đối tượng SavingsAccount
     */
    public List<Account> getSavingsAccounts() {
        List<Account> savingsAccounts = new ArrayList<>();
        for (Account account : accounts) {
            if (account instanceof SavingsAccount) {
                savingsAccounts.add(account);
            }
        }
        return savingsAccounts;
    }

    /**
     * Lấy tổng số dư trên tất cả tài khoản.
     *
     * @return Tổng số dư
     */
    public double getTotalBalance() {
        return accounts.stream()
                .mapToDouble(Account::getBalance)
                .sum();
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", accountCount=" + accounts.size() +
                ", totalBalance=" + getTotalBalance() +
                '}';
    }
}
