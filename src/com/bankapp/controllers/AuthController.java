package com.bankapp.controllers;

import com.bankapp.model.User;
import com.bankapp.services.AuthService;
import com.bankapp.utils.ConsoleUtils;
import com.bankapp.utils.InputValidator;

/**
 * AuthController - Xử lý các thao tác xác thực người dùng.
 * Áp dụng mẫu Controller trong MVC - kiểm tra dữ liệu và ủy quyền cho tầng service.
 */
public class AuthController {
    private final AuthService authService;

    /**
     * Constructor - khởi tạo với AuthService.
     *
     * @param authService Đối tượng AuthService
     */
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Xử lý luồng đăng ký tài khoản người dùng.
     *
     * @return Đối tượng User nếu đăng ký thành công, null nếu thất bại
     */
    public User handleRegistration() {
        ConsoleUtils.printHeader("USER REGISTRATION");

        // Get username
        String username = ConsoleUtils.readString("Enter username (4-20 chars, alphanumeric): ");
        if (!InputValidator.isValidUsername(username)) {
            ConsoleUtils.printError("Invalid username format");
            return null;
        }

        // Get password
        String password = ConsoleUtils.readString("Enter password (min 6 chars): ");
        if (!InputValidator.isValidPassword(password)) {
            ConsoleUtils.printError("Password must be at least 6 characters");
            return null;
        }

        // Get full name
        String fullName = ConsoleUtils.readString("Enter full name: ");
        if (!InputValidator.isValidFullName(fullName)) {
            ConsoleUtils.printError("Invalid full name (use letters and spaces only)");
            return null;
        }

        // Get email
        String email = ConsoleUtils.readString("Enter email: ");
        if (!InputValidator.isValidEmail(email)) {
            ConsoleUtils.printError("Invalid email format");
            return null;
        }

        // Attempt registration
        User newUser = authService.register(username, password, fullName, email);
        if (newUser != null) {
            ConsoleUtils.printSuccess("Registration successful!");
            ConsoleUtils.printInfo("Your User ID: " + newUser.getUserId());
            return newUser;
        } else {
            ConsoleUtils.printError("Registration failed. Username may already exist.");
            return null;
        }
    }

    /**
     * Xử lý luồng đăng nhập của người dùng.
     *
     * @return Đối tượng User nếu đăng nhập thành công, null nếu thất bại
     */
    public User handleLogin() {
        ConsoleUtils.printHeader("USER LOGIN");

        // Get username
        String username = ConsoleUtils.readString("Enter username: ");
        if (InputValidator.isNullOrEmpty(username)) {
            ConsoleUtils.printError("Username cannot be empty");
            return null;
        }

        // Get password
        String password = ConsoleUtils.readString("Enter password: ");
        if (InputValidator.isNullOrEmpty(password)) {
            ConsoleUtils.printError("Password cannot be empty");
            return null;
        }

        // Attempt login
        User user = authService.login(username, password);
        if (user != null) {
            ConsoleUtils.printSuccess("Login successful! Welcome, " + user.getFullName());
            return user;
        } else {
            ConsoleUtils.printError("Invalid username or password");
            return null;
        }
    }
}
