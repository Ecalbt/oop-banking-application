package com.bankapp.model;

/**
 * Lớp SavingsAccount - hiện thực cụ thể của Account.
 * Đại diện cho tài khoản tiết kiệm với giới hạn rút tiền và lãi suất.
 * Minh họa tính Kế thừa và Đa hình.
 */
public class SavingsAccount extends Account {
    private static final long serialVersionUID = 1L;
    private static final double MINIMUM_BALANCE = 100.0;
    private double interestRate; // Annual interest rate (e.g., 0.03 for 3%)
    private int withdrawalsThisMonth;
    private static final int MAX_MONTHLY_WITHDRAWALS = 6; // Federal regulation example
    private double withdrawalPenalty; // Penalty for exceeding withdrawal limit

    /**
     * Constructor cho SavingsAccount.
     *
     * @param accountNumber   Mã định danh duy nhất cho tài khoản
     * @param initialBalance  Số dư ban đầu
     * @param interestRate    Lãi suất hằng năm (dưới dạng thập phân, ví dụ 0.03 tương đương 3%)
     */
    public SavingsAccount(String accountNumber, double initialBalance, double interestRate) {
        super(accountNumber, initialBalance);
        this.interestRate = interestRate;
        this.withdrawalsThisMonth = 0;
        this.withdrawalPenalty = 25.0; // Default penalty
    }

    /**
     * Constructor dùng lãi suất mặc định.
     *
     * @param accountNumber   Mã định danh duy nhất cho tài khoản
     * @param initialBalance  Số dư ban đầu
     */
    public SavingsAccount(String accountNumber, double initialBalance) {
        this(accountNumber, initialBalance, 0.025); // Default 2.5% interest
    }

    // ============= Getters and Setters =============

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        if (interestRate >= 0) {
            this.interestRate = interestRate;
        }
    }

    public int getWithdrawalsThisMonth() {
        return withdrawalsThisMonth;
    }

    public double getWithdrawalPenalty() {
        return withdrawalPenalty;
    }

    public void setWithdrawalPenalty(double penalty) {
        if (penalty >= 0) {
            this.withdrawalPenalty = penalty;
        }
    }

    // ============= Implementation of Abstract Methods =============

    /**
     * Trả về loại tài khoản.
     *
     * @return "SAVINGS"
     */
    @Override
    public String getAccountType() {
        return "SAVINGS";
    }

    /**
     * Xác định xem có thể rút tiền hay không.
     * Tài khoản tiết kiệm có giới hạn số lần rút trong tháng và yêu cầu số dư tối thiểu.
     *
     * @param amount Số tiền cần rút
     * @return true nếu được phép rút, false nếu không
     */
    @Override
    public boolean canWithdraw(double amount) {
        if (!isActive) {
            return false;
        }
        // Check monthly withdrawal limit
        if (withdrawalsThisMonth >= MAX_MONTHLY_WITHDRAWALS) {
            return false;
        }
        // Check minimum balance requirement
        if ((this.balance - amount) < MINIMUM_BALANCE) {
            return false;
        }
        return true;
    }

    /**
     * Áp dụng các quy tắc riêng cho tài khoản tiết kiệm.
     * Tăng bộ đếm số lần rút và áp dụng phí phạt nếu vượt quá giới hạn.
     */
    @Override
    public void applyAccountSpecificRules() {
        withdrawalsThisMonth++;

        // If withdrawal limit is exceeded, apply penalty
        if (withdrawalsThisMonth > MAX_MONTHLY_WITHDRAWALS) {
            this.balance -= withdrawalPenalty;
            // Log penalty transaction
            Transaction penaltyTxn = new Transaction(
                    "PEN-" + System.nanoTime(),
                    this.accountNumber,
                    null,
                    withdrawalPenalty,
                    "WITHDRAWAL_PENALTY",
                    "Excess withdrawal penalty"
            );
            this.transactions.add(penaltyTxn);
        }
    }

    /**
     * Áp dụng lãi suất lên số dư tài khoản.
     * Nên được gọi theo tháng hoặc theo chu kỳ.
     * Lãi suất năm được chia cho 12 để tính theo tháng.
     *
     * @return Số tiền lãi được cộng
     */
    public double applyMonthlyInterest() {
        double monthlyRate = interestRate / 12.0;
        double interest = this.balance * monthlyRate;
        this.balance += interest;

        // Record interest transaction
        Transaction interestTxn = new Transaction(
                "INT-" + System.nanoTime(),
                this.accountNumber,
                null,
                interest,
                "INTEREST",
                "Monthly interest credit"
        );
        this.transactions.add(interestTxn);

        return interest;
    }

    /**
     * Đặt lại bộ đếm số lần rút tiền trong tháng.
     * Nên được gọi vào đầu mỗi tháng.
     */
    public void resetMonthlyWithdrawals() {
        this.withdrawalsThisMonth = 0;
    }

    /**
     * Tính toán số tiền lãi dự kiến trong năm dựa trên số dư hiện tại.
     *
     * @return Số tiền lãi dự kiến trong năm
     */
    public double getProjectedAnnualInterest() {
        return this.balance * interestRate;
    }

    @Override
    public String toString() {
        return "SavingsAccount{" +
                "accountNumber='" + accountNumber + '\'' +
                ", balance=" + balance +
                ", interestRate=" + (interestRate * 100) + "%" +
                ", withdrawalsThisMonth=" + withdrawalsThisMonth +
                ", isActive=" + isActive +
                ", transactionCount=" + transactions.size() +
                '}';
    }
}
