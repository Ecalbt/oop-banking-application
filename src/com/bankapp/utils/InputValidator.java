package com.bankapp.utils;

/**
 * InputValidator - Lớp tiện ích để kiểm tra tính hợp lệ của dữ liệu đầu vào.
 * Cung cấp các phương thức xác thực input của người dùng nhằm đảm bảo an toàn và đúng định dạng.
 */
public class InputValidator {

    /**
     * Kiểm tra tính hợp lệ của tên đăng nhập.
     * Yêu cầu: 4-20 ký tự, chỉ gồm chữ, số và dấu gạch dưới.
     *
     * @param username Tên đăng nhập cần kiểm tra
     * @return true nếu hợp lệ, false nếu không
     */
    public static boolean isValidUsername(String username) {
        if (username == null || username.isEmpty()) {
            return false;
        }
        if (username.length() < 4 || username.length() > 20) {
            return false;
        }
        return username.matches("^[a-zA-Z0-9_]+$");
    }

    /**
     * Kiểm tra tính hợp lệ của mật khẩu.
     * Yêu cầu: Ít nhất 6 ký tự.
     *
     * @param password Mật khẩu cần kiểm tra
     * @return true nếu hợp lệ, false nếu không
     */
    public static boolean isValidPassword(String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }
        return password.length() >= 6;
    }

    /**
     * Kiểm tra tính hợp lệ của địa chỉ email.
     * Sử dụng kiểm tra đơn giản bằng regex.
     *
     * @param email Email cần kiểm tra
     * @return true nếu hợp lệ, false nếu không
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    /**
     * Kiểm tra tính hợp lệ của họ tên.
     * Yêu cầu: 2-50 ký tự, chỉ gồm chữ cái và khoảng trắng.
     *
     * @param fullName Họ tên cần kiểm tra
     * @return true nếu hợp lệ, false nếu không
     */
    public static boolean isValidFullName(String fullName) {
        if (fullName == null || fullName.isEmpty()) {
            return false;
        }
        if (fullName.length() < 2 || fullName.length() > 50) {
            return false;
        }
        return fullName.matches("^[a-zA-Z\\s]+$");
    }

    /**
     * Kiểm tra tính hợp lệ của số tiền.
     * Yêu cầu: Số dương, tối đa 2 chữ số thập phân.
     *
     * @param amount Số tiền cần kiểm tra
     * @return true nếu hợp lệ, false nếu không
     */
    public static boolean isValidAmount(double amount) {
        return amount > 0 && amount <= 999999999.99;
    }

    /**
     * Kiểm tra chuỗi số tiền và chuyển sang kiểu double.
     *
     * @param amountStr Chuỗi số tiền cần kiểm tra
     * @return Giá trị double nếu hợp lệ, hoặc -1 nếu không
     */
    public static double parseAmount(String amountStr) {
        try {
            double amount = Double.parseDouble(amountStr);
            return isValidAmount(amount) ? amount : -1;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Kiểm tra tính hợp lệ của số tài khoản.
     * Yêu cầu: 8-16 ký tự, chỉ gồm chữ và số.
     *
     * @param accountNumber Số tài khoản cần kiểm tra
     * @return true nếu hợp lệ, false nếu không
     */
    public static boolean isValidAccountNumber(String accountNumber) {
        if (accountNumber == null || accountNumber.isEmpty()) {
            return false;
        }
        if (accountNumber.length() < 8 || accountNumber.length() > 16) {
            return false;
        }
        return accountNumber.matches("^[a-zA-Z0-9]+$");
    }

    /**
     * Kiểm tra một chuỗi có null hoặc rỗng hay không.
     *
     * @param str Chuỗi cần kiểm tra
     * @return true nếu null hoặc rỗng, false nếu không
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
}
