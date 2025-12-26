package com.bankapp.data;

/**
 * InMemoryDataStore - Kho lưu trữ dữ liệu trung tâm cho ứng dụng ngân hàng.
 * Áp dụng Repository Pattern để trừu tượng hóa tầng truy xuất dữ liệu.
 * Tất cả dữ liệu được lưu trong bộ nhớ và sẽ mất khi ứng dụng tắt
 * (trừ khi được mở rộng thêm cơ chế lưu file / cơ sở dữ liệu).
 */
public class InMemoryDataStore {
    private static InMemoryDataStore instance;
    private UserRepository userRepository;
    private AccountRepository accountRepository;
    private TransactionRepository transactionRepository;

    /**
     * Constructor private dùng cho mẫu thiết kế Singleton.
     */
    private InMemoryDataStore() {
        this.userRepository = new UserRepository();
        this.accountRepository = new AccountRepository();
        this.transactionRepository = new TransactionRepository();
    }

    /**
     * Lấy thể hiện duy nhất (singleton) của InMemoryDataStore.
     *
     * @return Đối tượng InMemoryDataStore
     */
    public static synchronized InMemoryDataStore getInstance() {
        if (instance == null) {
            instance = new InMemoryDataStore();
        }
        return instance;
    }

    /**
     * Lấy repository quản lý người dùng.
     *
     * @return Đối tượng UserRepository
     */
    public UserRepository getUserRepository() {
        return userRepository;
    }

    /**
     * Lấy repository quản lý tài khoản.
     *
     * @return Đối tượng AccountRepository
     */
    public AccountRepository getAccountRepository() {
        return accountRepository;
    }

    /**
     * Lấy repository quản lý giao dịch.
     *
     * @return Đối tượng TransactionRepository
     */
    public TransactionRepository getTransactionRepository() {
        return transactionRepository;
    }

    /**
     * Xóa toàn bộ dữ liệu trong kho dữ liệu.
     * Hữu ích cho việc kiểm thử hoặc đặt lại ứng dụng.
     */
    public void clearAll() {
        userRepository.clear();
        accountRepository.clear();
        transactionRepository.clear();
    }
}
