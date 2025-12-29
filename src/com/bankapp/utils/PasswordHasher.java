package com.bankapp.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * PasswordHasher - Lớp tiện ích dùng để băm và kiểm tra mật khẩu.
 * Sử dụng thuật toán SHA-256 với mã hóa Base64 để bảo mật.
 * Lưu ý: Trong môi trường thực tế nên dùng bcrypt hoặc Argon2.
 */
public class PasswordHasher {

    /**
     * Băm mật khẩu sử dụng thuật toán SHA-256.
     *
     * @param password Mật khẩu dạng văn bản thuần cần băm
     * @return Mật khẩu đã băm (mã hóa Base64)
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = digest.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }

    /**
     * Kiểm tra mật khẩu văn bản thuần với mật khẩu đã băm.
     *
     * @param plainPassword Mật khẩu văn bản thuần cần kiểm tra
     * @param hashedPassword Mật khẩu đã băm dùng để so sánh
     * @return true nếu mật khẩu khớp, false nếu không
     */
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        String hashedInput = hashPassword(plainPassword);
        return hashedInput.equals(hashedPassword);
    }
}
