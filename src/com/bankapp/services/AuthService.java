package com.bankapp.services;

import com.bankapp.data.InMemoryDataStore;
import com.bankapp.data.UserRepository;
import com.bankapp.model.User;
import com.bankapp.utils.IDGenerator;
import com.bankapp.utils.InputValidator;
import com.bankapp.utils.PasswordHasher;

/**
 * AuthService - Xử lý đăng nhập và đăng ký người dùng.
 * Tuân theo nguyên lý Trách nhiệm đơn (SRP) - chỉ tập trung vào nghiệp vụ xác thực.
 */
public class AuthService {
    private final UserRepository userRepository;

    /**
     * Constructor - khởi tạo với kho dữ liệu.
     */
    public AuthService() {
        this.userRepository = InMemoryDataStore.getInstance().getUserRepository();
    }

    /**
     * Đăng ký người dùng mới.
     * Kiểm tra tính hợp lệ của dữ liệu và trùng lặp username.
     *
     * @param username Tên đăng nhập của người dùng
     * @param password Mật khẩu của người dùng
     * @param pin Mã PIN giao dịch của người dùng
     * @param fullName Họ tên đầy đủ
     * @param email Địa chỉ email
     * @return Đối tượng User nếu đăng ký thành công, null nếu thất bại
     */
    public User register(String username, String password, String pin, String fullName, String email) {
        // Validate inputs
        if (!InputValidator.isValidUsername(username)) {
            return null;
        }
        if (!InputValidator.isValidPassword(password)) {
            return null;
        }
        if (!InputValidator.isValidPin(pin)) {
            return null;
        }
        if (!InputValidator.isValidFullName(fullName)) {
            return null;
        }
        if (!InputValidator.isValidEmail(email)) {
            return null;
        }

        // Check for duplicate username
        if (userRepository.usernameExists(username)) {
            return null;
        }

        // Create new user with hashed password
        String userId = IDGenerator.generateUserId();
        String passwordHash = PasswordHasher.hashPassword(password);
    String pinHash = PasswordHasher.hashPassword(pin);
    User newUser = new User(userId, username, passwordHash, pinHash, fullName, email);

        // Save user
        if (userRepository.save(newUser)) {
            return newUser;
        }
        return null;
    }

    /**
     * Xác thực mã PIN của người dùng cho các giao dịch.
     *
     * @param user Người dùng cần xác thực PIN
     * @param pin  Mã PIN dạng plain text
     * @return true nếu PIN đúng và chưa bị khóa, false nếu sai hoặc đã khóa
     */
    public boolean verifyPin(User user, String pin) {
        if (user == null || !InputValidator.isValidPin(pin)) {
            return false;
        }
        if (user.isPinLocked()) {
            return false;
        }

        boolean ok = user.verifyPin(pin);
        userRepository.update(user); // Lưu số lần nhập sai
        return ok;
    }

    public boolean isPinLocked(User user) {
        return user != null && user.isPinLocked();
    }

    public int getRemainingPinAttempts(User user) {
        return user != null ? user.getRemainingPinAttempts() : 0;
    }

    /**
     * Xác thực người dùng (đăng nhập).
     *
     * @param username Tên đăng nhập
     * @param password Mật khẩu
     * @return Đối tượng User nếu đăng nhập thành công, null nếu thất bại
     */
    public User login(String username, String password) {
        if (InputValidator.isNullOrEmpty(username) || InputValidator.isNullOrEmpty(password)) {
            return null;
        }

        User user = userRepository.findByUsername(username);
        if (user == null) {
            return null;
        }

        // Verify password
        if (PasswordHasher.verifyPassword(password, user.getPasswordHash())) {
            return user;
        }
        return null;
    }

    /**
     * Tìm người dùng theo ID.
     *
     * @param userId ID người dùng cần tìm
     * @return Đối tượng User nếu tìm thấy, null nếu không
     */
    public User getUserById(String userId) {
        return userRepository.findById(userId);
    }

    /**
     * Tìm người dùng theo tên đăng nhập.
     *
     * @param username Tên đăng nhập cần tìm
     * @return Đối tượng User nếu tìm thấy, null nếu không
     */
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
