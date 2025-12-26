package com.bankapp.utils;

import java.util.Scanner;

/**
 * ConsoleUtils - Lớp tiện ích cho các thao tác nhập/xuất trên console.
 * Cung cấp các hàm hỗ trợ hiển thị thông báo và đọc dữ liệu từ người dùng.
 */
public class ConsoleUtils {
    private static final Scanner scanner = new Scanner(System.in);
    private static final String SEPARATOR = "=".repeat(60);
    private static final String DASH_SEPARATOR = "-".repeat(60);

    /**
     * In một tiêu đề (header).
     *
     * @param message Thông điệp cần hiển thị làm tiêu đề
     */
    public static void printHeader(String message) {
        System.out.println("\n" + SEPARATOR);
        System.out.println("  " + message);
        System.out.println(SEPARATOR);
    }

    /**
     * In một tiêu đề phụ (subheader).
     *
     * @param message Thông điệp cần hiển thị làm tiêu đề phụ
     */
    public static void printSubHeader(String message) {
        System.out.println("\n" + DASH_SEPARATOR);
        System.out.println("  " + message);
        System.out.println(DASH_SEPARATOR);
    }

    /**
     * In thông báo thành công.
     *
     * @param message Thông điệp thành công
     */
    public static void printSuccess(String message) {
        System.out.println("✓ " + message);
    }

    /**
     * In thông báo lỗi.
     *
     * @param message Thông điệp lỗi
     */
    public static void printError(String message) {
        System.out.println("✗ Error: " + message);
    }

    /**
     * In thông báo thông tin.
     *
     * @param message Thông điệp thông tin
     */
    public static void printInfo(String message) {
        System.out.println("ℹ " + message);
    }

    /**
     * In thông báo cảnh báo.
     *
     * @param message Thông điệp cảnh báo
     */
    public static void printWarning(String message) {
        System.out.println("⚠ " + message);
    }

    /**
     * Đọc chuỗi ký tự người dùng nhập từ console.
     *
     * @param prompt Thông điệp gợi ý cần hiển thị
     * @return Chuỗi người dùng nhập
     */
    public static String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    /**
     * Đọc số thực người dùng nhập vào, có kiểm tra hợp lệ.
     *
     * @param prompt Thông điệp gợi ý cần hiển thị
     * @return Giá trị double hoặc -1 nếu không hợp lệ
     */
    public static double readDouble(String prompt) {
        try {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            return Double.parseDouble(input);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Đọc số nguyên người dùng nhập vào, có kiểm tra hợp lệ.
     *
     * @param prompt Thông điệp gợi ý cần hiển thị
     * @return Giá trị int hoặc -1 nếu không hợp lệ
     */
    public static int readInt(String prompt) {
        try {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Đọc mật khẩu người dùng nhập (không hiển thị lại trên console).
     *
     * @param prompt Thông điệp gợi ý cần hiển thị
     * @return Chuỗi mật khẩu người dùng nhập
     */
    public static String readPassword(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    /**
     * Hiển thị một menu và lấy lựa chọn từ người dùng.
     *
     * @param options Các lựa chọn trong menu
     * @return Chỉ số lựa chọn (bắt đầu từ 0) hoặc -1 nếu không hợp lệ
     */
    public static int readMenuChoice(String... options) {
        for (int i = 0; i < options.length; i++) {
            System.out.println((i + 1) + ". " + options[i]);
        }
        int choice = readInt("Enter your choice: ");
        return (choice >= 1 && choice <= options.length) ? choice - 1 : -1;
    }

    /**
     * Tạm dừng chương trình và chờ người dùng nhấn Enter.
     */
    public static void pause() {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }

    /**
     * Xóa màn hình console (hoạt động trên hầu hết terminal).
     */
    public static void clearScreen() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            // If clear fails, just print new lines
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }

    /**
     * Định dạng số tiền kèm ký hiệu tiền tệ.
     *
     * @param amount Số tiền cần định dạng
     * @return Chuỗi số tiền đã định dạng
     */
    public static String formatAmount(double amount) {
        return String.format("$%.2f", amount);
    }

    /**
     * Đóng resource Scanner.
     */
    public static void closeScanner() {
        scanner.close();
    }
}
