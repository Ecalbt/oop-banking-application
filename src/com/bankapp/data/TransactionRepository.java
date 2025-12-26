package com.bankapp.data;

import com.bankapp.model.Transaction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TransactionRepository - Đối tượng truy xuất dữ liệu cho thực thể Transaction.
 * Xử lý việc lưu trữ và truy vấn các giao dịch.
 */
public class TransactionRepository {
    private Map<String, List<Transaction>> accountTransactions; // accountNumber -> List of Transactions
    private List<Transaction> allTransactions; // Global transaction log

    public TransactionRepository() {
        this.accountTransactions = new HashMap<>(); // khởi tạo kho lưu trữ giao dịch
        this.allTransactions = new ArrayList<>(); // khởi tạo danh sách giao dịch toàn cục
    }

    /**
     * Ghi nhận một giao dịch vào repository.
     *
     * @param accountNumber Số tài khoản liên quan tới giao dịch
     * @param transaction   Đối tượng Transaction cần lưu
     * @return true nếu lưu thành công
     */
    public boolean saveTransaction(String accountNumber, Transaction transaction) {
        if (accountNumber == null || transaction == null) {
            return false;
        }
        accountTransactions.computeIfAbsent(accountNumber, k -> new ArrayList<>()).add(transaction);
        allTransactions.add(transaction);
        return true;
    }

    /**
     * Lấy tất cả giao dịch của một tài khoản.
     *
     * @param accountNumber Số tài khoản cần lấy danh sách giao dịch
     * @return Danh sách giao dịch của tài khoản
     */
    public List<Transaction> getTransactionsByAccount(String accountNumber) {
        return new ArrayList<>(accountTransactions.getOrDefault(accountNumber, new ArrayList<>()));
        // Trả về một bản sao của danh sách giao dịch để tránh bị sửa đổi từ bên ngoài
    }

    /**
     * Lấy các giao dịch gần đây của một tài khoản.
     *
     * @param accountNumber Số tài khoản cần lấy giao dịch
     * @param count         Số lượng giao dịch gần nhất cần lấy
     * @return Danh sách các giao dịch gần đây
     */
    public List<Transaction> getRecentTransactions(String accountNumber, int count) {
        List<Transaction> transactions = getTransactionsByAccount(accountNumber);
        int size = transactions.size();
        int startIndex = Math.max(0, size - count);
        return new ArrayList<>(transactions.subList(startIndex, size));
    }

    /**
     * Lấy một giao dịch theo ID.
     *
     * @param transactionId ID giao dịch cần tìm
     * @return Đối tượng Transaction nếu tìm thấy, null nếu không
     */
    public Transaction findById(String transactionId) {
        return allTransactions.stream()
                .filter(txn -> txn.getTransactionId().equals(transactionId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Lấy số lượng giao dịch của một tài khoản.
     *
     * @param accountNumber Số tài khoản
     * @return Số lượng giao dịch của tài khoản
     */
    public int getTransactionCount(String accountNumber) {
        return accountTransactions.getOrDefault(accountNumber, new ArrayList<>()).size();
    }

    /**
     * Lấy tổng số lượng giao dịch trên tất cả tài khoản.
     *
     * @return Tổng số giao dịch
     */
    public int getTotalTransactionCount() {
        return allTransactions.size();
    }

    /**
     * Xóa toàn bộ giao dịch.
     */
    public void clear() {
        accountTransactions.clear();
        allTransactions.clear();
    }
}
