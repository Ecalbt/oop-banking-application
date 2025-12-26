package com.bankapp.services;

import com.bankapp.data.InMemoryDataStore;
import com.bankapp.data.AccountRepository;
import com.bankapp.model.Account;
import com.bankapp.model.User;
import com.bankapp.model.CheckingAccount;
import com.bankapp.model.SavingsAccount;
import com.bankapp.utils.IDGenerator;

/**
 * AccountService - Xử lý các thao tác tài khoản (tạo, truy vấn, đóng tài khoản).
 * Tuân theo nguyên lý Trách nhiệm đơn (SRP) - chỉ tập trung vào quản lý tài khoản.
 */
public class AccountService {
    private final AccountRepository accountRepository;

    /**
     * Constructor - khởi tạo với kho dữ liệu.
     */
    public AccountService() {
        this.accountRepository = InMemoryDataStore.getInstance().getAccountRepository();
    }

    /**
     * Tạo tài khoản thanh toán (checking) mới cho người dùng.
     *
     * @param user Người dùng cần tạo tài khoản
     * @param initialBalance Số dư ban đầu
     * @param overdraftLimit Hạn mức thấu chi tối đa
     * @return Đối tượng CheckingAccount nếu thành công, null nếu thất bại
     */
    public CheckingAccount createCheckingAccount(User user, double initialBalance, double overdraftLimit) {
        if (user == null || initialBalance < 0 || overdraftLimit < 0) {
            return null;
        }

        String accountNumber = IDGenerator.generateAccountNumber();
        CheckingAccount account = new CheckingAccount(accountNumber, initialBalance, overdraftLimit);

        // Save account to repository
        if (accountRepository.save(account)) {
            // Add account to user
            if (user.addAccount(account)) {
                return account;
            }
        }
        return null;
    }

    /**
     * Tạo tài khoản tiết kiệm (savings) mới cho người dùng.
     *
     * @param user Người dùng cần tạo tài khoản
     * @param initialBalance Số dư ban đầu
     * @param interestRate Lãi suất hằng năm
     * @return Đối tượng SavingsAccount nếu thành công, null nếu thất bại
     */
    public SavingsAccount createSavingsAccount(User user, double initialBalance, double interestRate) {
        if (user == null || initialBalance < 0 || interestRate < 0) {
            return null;
        }

        String accountNumber = IDGenerator.generateAccountNumber();
        SavingsAccount account = new SavingsAccount(accountNumber, initialBalance, interestRate);

        // Save account to repository
        if (accountRepository.save(account)) {
            // Add account to user
            if (user.addAccount(account)) {
                return account;
            }
        }
        return null;
    }

    /**
     * Lấy thông tin tài khoản theo số tài khoản.
     *
     * @param accountNumber Số tài khoản cần tìm
     * @return Đối tượng Account nếu tìm thấy, null nếu không
     */
    public Account getAccount(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber);
    }

    /**
     * Đóng một tài khoản.
     *
     * @param accountNumber Số tài khoản cần đóng
     * @return true nếu đóng thành công, false nếu không
     */
    public boolean closeAccount(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account != null) {
            account.closeAccount();
            accountRepository.update(account);
            return true;
        }
        return false;
    }

    /**
     * Kiểm tra một tài khoản có tồn tại hay không.
     *
     * @param accountNumber Số tài khoản cần kiểm tra
     * @return true nếu tồn tại, false nếu không
     */
    public boolean accountExists(String accountNumber) {
        return accountRepository.exists(accountNumber);
    }

    /**
     * Lấy số dư của tài khoản.
     *
     * @param accountNumber Số tài khoản
     * @return Số dư tài khoản, hoặc -1 nếu không tìm thấy
     */
    public double getAccountBalance(String accountNumber) {
        Account account = getAccount(accountNumber);
        return account != null ? account.getBalance() : -1;
    }

    /**
     * Lấy loại tài khoản.
     *
     * @param accountNumber Số tài khoản
     * @return Kiểu tài khoản (CHECKING, SAVINGS), hoặc null nếu không tìm thấy
     */
    public String getAccountType(String accountNumber) {
        Account account = getAccount(accountNumber);
        return account != null ? account.getAccountType() : null;
    }
}
