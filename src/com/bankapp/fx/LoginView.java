package com.bankapp.fx;

import com.bankapp.model.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * LoginView - Màn hình đăng nhập và đăng ký.
 * Sử dụng Java thuần để tạo giao diện (không FXML).
 */
public class LoginView {
    
    private BankAppFX app;
    private Scene scene;
    
    // Form đăng nhập
    private TextField txtLoginUsername;
    private PasswordField txtLoginPassword;
    
    // Form đăng ký
    private TextField txtRegUsername;
    private PasswordField txtRegPassword;
    private PasswordField txtRegConfirmPassword;
    private TextField txtRegFullName;
    private TextField txtRegEmail;
    
    // TabPane để chuyển giữa đăng nhập và đăng ký
    private TabPane tabPane;
    
    public LoginView(BankAppFX app) {
        this.app = app;
        createUI();
    }
    
    /**
     * Tạo giao diện người dùng.
     */
    private void createUI() {
        // Root container
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: #f5f5f5;");
        
        // Tiêu đề
        Label lblTitle = new Label("🏦 BankApp");
        lblTitle.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        lblTitle.setStyle("-fx-text-fill: #2196F3;");
        
        Label lblSubtitle = new Label("Ứng Dụng Ngân Hàng");
        lblSubtitle.setFont(Font.font("Arial", 14));
        lblSubtitle.setStyle("-fx-text-fill: #666;");
        
        // TabPane
        tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        
        // Tab Đăng nhập
        Tab loginTab = new Tab("Đăng Nhập");
        loginTab.setContent(createLoginForm());
        
        // Tab Đăng ký
        Tab registerTab = new Tab("Đăng Ký");
        registerTab.setContent(createRegisterForm());
        
        tabPane.getTabs().addAll(loginTab, registerTab);
        
        root.getChildren().addAll(lblTitle, lblSubtitle, tabPane);
        
        scene = new Scene(root, 400, 500);
    }
    
    /**
     * Tạo form đăng nhập.
     */
    private VBox createLoginForm() {
        VBox form = new VBox(15);
        form.setPadding(new Insets(20));
        form.setAlignment(Pos.CENTER);
        
        // Username
        Label lblUsername = new Label("Tên đăng nhập:");
        txtLoginUsername = new TextField();
        txtLoginUsername.setPromptText("Nhập tên đăng nhập");
        txtLoginUsername.setMaxWidth(250);
        
        // Password
        Label lblPassword = new Label("Mật khẩu:");
        txtLoginPassword = new PasswordField();
        txtLoginPassword.setPromptText("Nhập mật khẩu");
        txtLoginPassword.setMaxWidth(250);
        
        // Nút đăng nhập
        Button btnLogin = new Button("Đăng Nhập");
        btnLogin.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 30;");
        btnLogin.setOnAction(e -> handleLogin());
        
        // Enter key để đăng nhập
        txtLoginPassword.setOnAction(e -> handleLogin());
        
        form.getChildren().addAll(
            lblUsername, txtLoginUsername,
            lblPassword, txtLoginPassword,
            new Region() {{ setMinHeight(10); }},
            btnLogin
        );
        
        return form;
    }
    
    /**
     * Tạo form đăng ký.
     */
    private VBox createRegisterForm() {
        VBox form = new VBox(10);
        form.setPadding(new Insets(15));
        form.setAlignment(Pos.CENTER);
        
        // Username
        Label lblUsername = new Label("Tên đăng nhập:");
        txtRegUsername = new TextField();
        txtRegUsername.setPromptText("4-20 ký tự, chữ và số");
        txtRegUsername.setMaxWidth(250);
        
        // Password
        Label lblPassword = new Label("Mật khẩu:");
        txtRegPassword = new PasswordField();
        txtRegPassword.setPromptText("Tối thiểu 6 ký tự");
        txtRegPassword.setMaxWidth(250);
        
        // Confirm Password
        Label lblConfirmPassword = new Label("Xác nhận mật khẩu:");
        txtRegConfirmPassword = new PasswordField();
        txtRegConfirmPassword.setPromptText("Nhập lại mật khẩu");
        txtRegConfirmPassword.setMaxWidth(250);
        
        // Full Name
        Label lblFullName = new Label("Họ và tên:");
        txtRegFullName = new TextField();
        txtRegFullName.setPromptText("Nhập họ tên đầy đủ");
        txtRegFullName.setMaxWidth(250);
        
        // Email
        Label lblEmail = new Label("Email:");
        txtRegEmail = new TextField();
        txtRegEmail.setPromptText("example@email.com");
        txtRegEmail.setMaxWidth(250);
        
        // Nút đăng ký
        Button btnRegister = new Button("Đăng Ký");
        btnRegister.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 30;");
        btnRegister.setOnAction(e -> handleRegister());
        
        form.getChildren().addAll(
            lblUsername, txtRegUsername,
            lblPassword, txtRegPassword,
            lblConfirmPassword, txtRegConfirmPassword,
            lblFullName, txtRegFullName,
            lblEmail, txtRegEmail,
            new Region() {{ setMinHeight(5); }},
            btnRegister
        );
        
        return form;
    }
    
    /**
     * Xử lý đăng nhập.
     */
    private void handleLogin() {
        String username = txtLoginUsername.getText().trim();
        String password = txtLoginPassword.getText();
        
        // Kiểm tra dữ liệu nhập
        if (username.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Vui lòng nhập tên đăng nhập!");
            txtLoginUsername.requestFocus();
            return;
        }
        
        if (password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Vui lòng nhập mật khẩu!");
            txtLoginPassword.requestFocus();
            return;
        }
        
        // Thực hiện đăng nhập
        User user = app.getAuthService().login(username, password);
        
        if (user != null) {
            app.setCurrentUser(user);
            showAlert(Alert.AlertType.INFORMATION, "Thành công", 
                "Đăng nhập thành công!\nXin chào, " + user.getFullName());
            clearLoginForm();
            app.showDashboardView();
        } else {
            showAlert(Alert.AlertType.ERROR, "Lỗi", 
                "Đăng nhập thất bại!\nTên đăng nhập hoặc mật khẩu không đúng.");
            txtLoginPassword.clear();
            txtLoginPassword.requestFocus();
        }
    }
    
    /**
     * Xử lý đăng ký.
     */
    private void handleRegister() {
        String username = txtRegUsername.getText().trim();
        String password = txtRegPassword.getText();
        String confirmPassword = txtRegConfirmPassword.getText();
        String fullName = txtRegFullName.getText().trim();
        String email = txtRegEmail.getText().trim();
        
        // Kiểm tra dữ liệu nhập
        if (username.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Vui lòng nhập tên đăng nhập!");
            txtRegUsername.requestFocus();
            return;
        }
        
        if (username.length() < 4 || username.length() > 20) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Tên đăng nhập phải từ 4-20 ký tự!");
            txtRegUsername.requestFocus();
            return;
        }
        
        if (password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Vui lòng nhập mật khẩu!");
            txtRegPassword.requestFocus();
            return;
        }
        
        if (password.length() < 6) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Mật khẩu phải có tối thiểu 6 ký tự!");
            txtRegPassword.requestFocus();
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Mật khẩu xác nhận không khớp!");
            txtRegConfirmPassword.requestFocus();
            return;
        }
        
        if (fullName.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Vui lòng nhập họ và tên!");
            txtRegFullName.requestFocus();
            return;
        }
        
        if (email.isEmpty() || !email.contains("@")) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Vui lòng nhập email hợp lệ!");
            txtRegEmail.requestFocus();
            return;
        }
        
        // Thực hiện đăng ký
        User newUser = app.getAuthService().register(username, password, fullName, email);
        
        if (newUser != null) {
            showAlert(Alert.AlertType.INFORMATION, "Thành công", 
                "Đăng ký thành công!\nVui lòng đăng nhập với tài khoản vừa tạo.");
            clearRegisterForm();
            tabPane.getSelectionModel().select(0); // Chuyển sang tab đăng nhập
            txtLoginUsername.setText(username);
            txtLoginPassword.requestFocus();
        } else {
            showAlert(Alert.AlertType.ERROR, "Lỗi", 
                "Đăng ký thất bại!\nTên đăng nhập có thể đã tồn tại hoặc dữ liệu không hợp lệ.");
        }
    }
    
    /**
     * Xóa form đăng nhập.
     */
    private void clearLoginForm() {
        txtLoginUsername.clear();
        txtLoginPassword.clear();
    }
    
    /**
     * Xóa form đăng ký.
     */
    private void clearRegisterForm() {
        txtRegUsername.clear();
        txtRegPassword.clear();
        txtRegConfirmPassword.clear();
        txtRegFullName.clear();
        txtRegEmail.clear();
    }
    
    /**
     * Hiển thị Alert.
     */
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Lấy Scene.
     */
    public Scene getScene() {
        return scene;
    }
}
