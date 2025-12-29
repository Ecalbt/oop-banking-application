package com.bankapp.utils;

import java.util.Random;

/**
 * IDGenerator - Lớp tiện ích dùng để sinh các mã định danh duy nhất.
 * Tạo ID cho người dùng, tài khoản và giao dịch.
 */
public class IDGenerator {
    private static final Random random = new Random();
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    /**
     * Sinh ID người dùng duy nhất.
     * Định dạng: USER_XXXXXXXX (ví dụ: USER_ABC12345).
     *
     * @return ID người dùng được sinh ra
     */
    public static String generateUserId() {
        return "USER_" + generateRandomString(8);
    }

    /**
     * Sinh số tài khoản duy nhất.
     * Định dạng: ACC_XXXXXXXX (ví dụ: ACC_12ABC345).
     *
     * @return Số tài khoản được sinh ra
     */
    public static String generateAccountNumber() {
        return "ACC" + System.currentTimeMillis() % 1000000;
    }

    /**
     * Sinh ID giao dịch duy nhất.
     * Định dạng: TXN_XXXXXXXXXXXX
     *
     * @return ID giao dịch được sinh ra
     */
    public static String generateTransactionId() {
        return "TXN_" + System.nanoTime();
    }

    /**
     * Sinh một chuỗi ngẫu nhiên với độ dài chỉ định.
     *
     * @param length Độ dài chuỗi cần sinh
     * @return Chuỗi ngẫu nhiên
     */
    private static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }
}
