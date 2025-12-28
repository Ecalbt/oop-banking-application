package com.bankapp.fx;

import com.bankapp.model.Account;
import com.bankapp.model.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * DashboardView - Màn hình chính sau khi đăng nhập.
 * Chứa menu điều hướng và các tab quản lý.
 */
public class DashboardView {
    
    private BankAppFX app;
    private Scene scene;
    
    // Header labels
    private Label lblWelcome;
    private Label lblTotalBalance;
    
    // Tab panels
    private AccountManagementPanel accountPanel;
    private TransactionPanel transactionPanel;
    
    // Profile labels
    private Label lblUserId;
    private Label lblUsername;
    private Label lblFullName;
    private Label lblEmail;
    private Label lblAccountCount;
    private Label lblCreatedAt;
    
    public DashboardView(BankAppFX app) {
        this.app = app;
        createUI();
    }
    
    /**
     * Tạo giao diện người dùng.
     */
    private void createUI() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f5f5f5;");
        
        // Header
        root.setTop(createHeader());
        
        // Content - TabPane
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        
        // Tab Quản lý tài khoản
        accountPanel = new AccountManagementPanel(app);
        Tab accountTab = new Tab("📊 Quản Lý Tài Khoản");
        accountTab.setContent(accountPanel.getContent());
        
        // Tab Giao dịch
        transactionPanel = new TransactionPanel(app);
        Tab transactionTab = new Tab("💰 Giao Dịch");
        transactionTab.setContent(transactionPanel.getContent());
        
        // Tab Thông tin cá nhân
        Tab profileTab = new Tab("👤 Thông Tin Cá Nhân");
        profileTab.setContent(createProfilePanel());
        
        tabPane.getTabs().addAll(accountTab, transactionTab, profileTab);
        
        // Listener để refresh khi chuyển tab
        tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            if (newTab == accountTab) {
                accountPanel.refresh();
            } else if (newTab == transactionTab) {
                transactionPanel.refresh();
            } else if (newTab == profileTab) {
                refreshProfile();
            }
        });
        
        root.setCenter(tabPane);
        
        scene = new Scene(root, 1000, 700);
    }
    
    /**
     * Tạo header.
     */
    private HBox createHeader() {
        HBox header = new HBox(20);
        header.setPadding(new Insets(15, 25, 15, 25));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle("-fx-background-color: #2196F3;");
        
        // Logo và tên app
        Label lblLogo = new Label("🏦 BankApp");
        lblLogo.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        lblLogo.setStyle("-fx-text-fill: white;");
        
        // Spacer
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // Thông tin người dùng
        VBox userInfo = new VBox(2);
        userInfo.setAlignment(Pos.CENTER_RIGHT);
        
        lblWelcome = new Label("Xin chào, User");
        lblWelcome.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        
        lblTotalBalance = new Label("Tổng số dư: 0 VNĐ");
        lblTotalBalance.setStyle("-fx-text-fill: #E3F2FD; -fx-font-size: 12px;");
        
        userInfo.getChildren().addAll(lblWelcome, lblTotalBalance);
        
        // Nút đăng xuất
        Button btnLogout = new Button("Đăng Xuất");
        btnLogout.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        btnLogout.setOnAction(e -> handleLogout());
        
        header.getChildren().addAll(lblLogo, spacer, userInfo, btnLogout);
        
        return header;
    }
    
    /**
     * Tạo panel thông tin cá nhân.
     */
    private VBox createProfilePanel() {
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(30));
        panel.setAlignment(Pos.TOP_CENTER);
        
        Label lblTitle = new Label("Thông Tin Cá Nhân");
        lblTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        
        // Grid thông tin
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(20));
        grid.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        
        // Labels
        String[] labels = {"User ID:", "Tên đăng nhập:", "Họ và tên:", "Email:", "Số tài khoản:", "Ngày tạo:"};
        for (int i = 0; i < labels.length; i++) {
            Label lbl = new Label(labels[i]);
            lbl.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            grid.add(lbl, 0, i);
        }
        
        // Values - khởi tạo các label instance
        lblUserId = new Label("-");
        lblUsername = new Label("-");
        lblFullName = new Label("-");
        lblEmail = new Label("-");
        lblAccountCount = new Label("-");
        lblCreatedAt = new Label("-");
        
        // Style cho các value labels
        Label[] valueLabels = {lblUserId, lblUsername, lblFullName, lblEmail, lblAccountCount, lblCreatedAt};
        for (Label lbl : valueLabels) {
            lbl.setFont(Font.font("Arial", 14));
            lbl.setStyle("-fx-text-fill: #333;");
        }
        
        grid.add(lblUserId, 1, 0);
        grid.add(lblUsername, 1, 1);
        grid.add(lblFullName, 1, 2);
        grid.add(lblEmail, 1, 3);
        grid.add(lblAccountCount, 1, 4);
        grid.add(lblCreatedAt, 1, 5);
        
        panel.getChildren().addAll(lblTitle, grid);
        
        // Load dữ liệu ngay khi tạo panel
        refreshProfile();
        
        return panel;
    }
    
    /**
     * Cập nhật thông tin cá nhân.
     */
    private void refreshProfile() {
        User user = app.getCurrentUser();
        if (user != null) {
            lblUserId.setText(user.getUserId());
            lblUsername.setText(user.getUsername());
            lblFullName.setText(user.getFullName());
            lblEmail.setText(user.getEmail());
            lblAccountCount.setText(String.valueOf(user.getAccounts().size()) + " tài khoản");
            
            // Format ngày tạo
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            lblCreatedAt.setText(sdf.format(new Date(user.getCreatedAt())));
        }
    }
    
    /**
     * Xử lý đăng xuất.
     */
    private void handleLogout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận");
        alert.setHeaderText("Đăng xuất");
        alert.setContentText("Bạn có chắc chắn muốn đăng xuất?");
        
        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            app.logout();
        }
    }
    
    /**
     * Làm mới dữ liệu hiển thị.
     */
    public void refresh() {
        User user = app.getCurrentUser();
        if (user != null) {
            lblWelcome.setText("Xin chào, " + user.getFullName());
            lblTotalBalance.setText(String.format("Tổng số dư: %,.0f VNĐ", user.getTotalBalance()));
            
            // Refresh panels
            accountPanel.refresh();
            transactionPanel.refresh();
        }
    }
    
    /**
     * Lấy Scene.
     */
    public Scene getScene() {
        return scene;
    }
}
