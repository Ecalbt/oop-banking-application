package com.bankapp.model;

/**
 * Lớp CheckingAccount - hiện thực cụ thể của Account.
 * Đại diện cho tài khoản thanh toán thông thường, không có ràng buộc đặc biệt.
 * Minh họa tính Kế thừa và Đa hình.
 */
public class CheckingAccount extends Account {
    private static final long serialVersionUID = 1L;
    private static final double MINIMUM_BALANCE = 0.0;
    private double overdraftLimit;
    private int monthlyWithdrawals;
    private int maxMonthlyWithdrawals;

    /**
     * Constructor cho CheckingAccount.
     *
     * @param accountNumber    Mã định danh duy nhất cho tài khoản
     * @param initialBalance   Số dư ban đầu
     * @param overdraftLimit   Hạn mức thấu chi tối đa cho phép
     */
    public CheckingAccount(String accountNumber, double initialBalance, double overdraftLimit) {
        super(accountNumber, initialBalance);
        this.overdraftLimit = overdraftLimit;
        this.monthlyWithdrawals = 0;
        this.maxMonthlyWithdrawals = Integer.MAX_VALUE; // Unlimited by default
    }

    /**
     * Constructor không có hạn mức thấu chi.
     *
     * @param accountNumber   Mã định danh duy nhất cho tài khoản
     * @param initialBalance  Số dư ban đầu
     */
    public CheckingAccount(String accountNumber, double initialBalance) {
        this(accountNumber, initialBalance, 0.0);
    }

    // ============= Getters and Setters =============

    public double getOverdraftLimit() {
        return overdraftLimit;
    }

    public void setOverdraftLimit(double overdraftLimit) {
        if (overdraftLimit >= 0) {
            this.overdraftLimit = overdraftLimit;
        }
    }

    public int getMonthlyWithdrawals() {
        return monthlyWithdrawals;
    }

    // ============= Implementation of Abstract Methods =============

    /**
     * Trả về loại tài khoản.
     *
     * @return "CHECKING"
     */
    @Override
    public String getAccountType() {
        return "CHECKING";
    }

    /**
     * Xác định xem có thể rút tiền hay không.
     * Tài khoản thanh toán cho phép rút nếu số dư + hạn mức thấu chi đủ.
     *
     * @param amount Số tiền cần rút
     * @return true nếu được phép rút, false nếu không
     */
    @Override
    public boolean canWithdraw(double amount) {
        return !isActive || (this.balance + this.overdraftLimit) >= amount;
    }

    /**
     * Áp dụng các quy tắc riêng cho tài khoản thanh toán.
     * Hiện tại chưa có quy tắc đặc biệt sau khi rút.
     */
    @Override
    public void applyAccountSpecificRules() {
        monthlyWithdrawals++;
        // Could add overdraft fee logic here if balance goes negative
        if (this.balance < 0) {
            // Apply overdraft fee (example)
            // this.balance -= 35.0; // Overdraft fee
        }
    }

    /**
     * Đặt lại bộ đếm số lần rút tiền trong tháng (nên gọi mỗi tháng).
     */
    public void resetMonthlyWithdrawals() {
        this.monthlyWithdrawals = 0;
    }

    @Override
    public String toString() {
        return "CheckingAccount{" +
                "accountNumber='" + accountNumber + '\'' +
                ", balance=" + balance +
                ", overdraftLimit=" + overdraftLimit +
                ", isActive=" + isActive +
                ", transactionCount=" + transactions.size() +
                '}';
    }
}
