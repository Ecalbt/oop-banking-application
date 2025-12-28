package com.bankapp.data;

import com.bankapp.model.Account;
import java.util.HashMap;
import java.util.Map;

/**
 * AccountRepository - Đối tượng truy xuất dữ liệu cho thực thể Account.
 * Xử lý việc lưu trữ và truy vấn tài khoản.
 */
public class AccountRepository {
    private Map<String, Account> accounts; // accountNumber -> Account

    public AccountRepository() {
        this.accounts = new HashMap<>();
    }

    /**
     * Lưu một tài khoản vào repository.
     *
     * @param account Đối tượng Account cần lưu
     * @return true nếu lưu thành công, false nếu tài khoản đã tồn tại
     */
    public boolean save(Account account) {
        if (account == null || accounts.containsKey(account.getAccountNumber())) {
            return false;
        }
        accounts.put(account.getAccountNumber(), account);
        return true;
    }

    /**
     * Tìm tài khoản theo số tài khoản.
     *
     * @param accountNumber Số tài khoản cần tìm
     * @return Đối tượng Account nếu tìm thấy, null nếu không
     */
    public Account findByAccountNumber(String accountNumber) {
        return accounts.get(accountNumber);
    }

    /**
     * Cập nhật một tài khoản đã tồn tại.
     *
     * @param account Đối tượng Account cần cập nhật
     * @return true nếu cập nhật thành công, false nếu không tìm thấy tài khoản
     */
    public boolean update(Account account) {
        if (account == null || !accounts.containsKey(account.getAccountNumber())) {
            return false;
        }
        accounts.put(account.getAccountNumber(), account);
        return true;
    }

    /**
     * Xóa một tài khoản khỏi repository.
     *
     * @param accountNumber Số tài khoản cần xóa
     * @return true nếu xóa thành công, false nếu không tìm thấy
     */
    public boolean delete(String accountNumber) {
        return accounts.remove(accountNumber) != null;
    }

    /**
     * Kiểm tra một tài khoản có tồn tại không.
     *
     * @param accountNumber Số tài khoản cần kiểm tra
     * @return true nếu tồn tại, false nếu không
     */
    public boolean exists(String accountNumber) {
        return accounts.containsKey(accountNumber);
    }

    /**
     * Lấy tổng số lượng tài khoản.
     *
     * @return Số lượng tài khoản
     */
    public int getAccountCount() {
        return accounts.size();
    }

    /**
     * Xóa toàn bộ tài khoản khỏi repository.
     */
    public void clear() {
        accounts.clear();
    }
}
