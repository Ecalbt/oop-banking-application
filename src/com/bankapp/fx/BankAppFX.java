package com.bankapp.fx;

import com.bankapp.model.User;
import com.bankapp.services.AuthService;
import com.bankapp.services.AccountService;
import com.bankapp.services.TransactionService;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * BankAppFX - Điểm vào chính cho ứng dụng JavaFX.
 * Quản lý các màn hình và chuyển đổi giữa chúng.
 */
public class BankAppFX extends Application {
    
    private Stage primaryStage;
    private User currentUser;
    
    // Services
    private AuthService authService;
    private AccountService accountService;
    private TransactionService transactionService;
    
    // Views
    private LoginView loginView;
    private DashboardView dashboardView;
    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("BankApp - Ứng Dụng Ngân Hàng");
        
        // Khởi tạo services
        authService = new AuthService();
        accountService = new AccountService();
        transactionService = new TransactionService();
        
        // Khởi tạo views
        loginView = new LoginView(this);
        dashboardView = new DashboardView(this);
        
        // Hiển thị màn hình đăng nhập
        showLoginView();
        
        primaryStage.show();
    }
    
    /**
     * Hiển thị màn hình đăng nhập.
     */
    public void showLoginView() {
        primaryStage.setScene(loginView.getScene());
        primaryStage.setWidth(400);
        primaryStage.setHeight(500);
        primaryStage.centerOnScreen();
    }
    
    /**
     * Hiển thị màn hình Dashboard sau khi đăng nhập thành công.
     */
    public void showDashboardView() {
        dashboardView.refresh();
        primaryStage.setScene(dashboardView.getScene());
        primaryStage.setWidth(1000);
        primaryStage.setHeight(700);
        primaryStage.centerOnScreen();
    }
    
    /**
     * Đăng xuất người dùng.
     */
    public void logout() {
        currentUser = null;
        showLoginView();
    }
    
    // ============= Getters và Setters =============
    
    public Stage getPrimaryStage() {
        return primaryStage;
    }
    
    public User getCurrentUser() {
        return currentUser;
    }
    
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }
    
    public AuthService getAuthService() {
        return authService;
    }
    
    public AccountService getAccountService() {
        return accountService;
    }
    
    public TransactionService getTransactionService() {
        return transactionService;
    }
    
    /**
     * Main method - Điểm vào chương trình.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
