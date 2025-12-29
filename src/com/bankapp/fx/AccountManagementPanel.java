package com.bankapp.fx;

import com.bankapp.model.Account;
import com.bankapp.model.CheckingAccount;
import com.bankapp.model.SavingsAccount;
import com.bankapp.model.User;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * AccountManagementPanel - Panel quản lý tài khoản với TableView.
 * Cho phép xem, thêm, sửa, xóa tài khoản.
 */
public class AccountManagementPanel {
    
    private BankAppFX app;
    private VBox content;
    
    // TableView
    private TableView<AccountTableRow> tableView;
    private ObservableList<AccountTableRow> accountData;
    
    // Form nhập liệu
    private ComboBox<String> cmbAccountType;
    private TextField txtInitialBalance;
    private TextField txtExtraParam; // overdraftLimit hoặc interestRate
    private Label lblExtraParam;
    
    // Selected account
    private AccountTableRow selectedAccount;
    
    public AccountManagementPanel(BankAppFX app) {
        this.app = app;
        accountData = FXCollections.observableArrayList();
        createUI();
    }
    
    /**
     * Tạo giao diện người dùng.
     */
    private void createUI() {
        content = new VBox(15);
        content.setPadding(new Insets(20));
        
        // Tiêu đề
        Label lblTitle = new Label("Quản Lý Tài Khoản");
        lblTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        
        // Phần TableView
        VBox tableSection = createTableSection();
        
        // Phần Form
        HBox formSection = createFormSection();
        
        // Phần nút bấm
        HBox buttonSection = createButtonSection();
        
        content.getChildren().addAll(lblTitle, tableSection, formSection, buttonSection);
    }
    
    /**
     * Tạo phần TableView.
     */
    private VBox createTableSection() {
        VBox section = new VBox(10);
        
        Label lblTable = new Label("Danh sách tài khoản:");
        lblTable.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        
        // Tạo TableView
        tableView = new TableView<>();
        tableView.setPlaceholder(new Label("Chưa có tài khoản nào"));
        tableView.setPrefHeight(250);
        
        // Cột số tài khoản
        TableColumn<AccountTableRow, String> colAccountNumber = new TableColumn<>("Số Tài Khoản");
        colAccountNumber.setCellValueFactory(new PropertyValueFactory<>("accountNumber"));
        colAccountNumber.setPrefWidth(150);
        
        // Cột loại tài khoản
        TableColumn<AccountTableRow, String> colType = new TableColumn<>("Loại TK");
        colType.setCellValueFactory(new PropertyValueFactory<>("accountType"));
        colType.setPrefWidth(120);
        
        // Cột số dư
        TableColumn<AccountTableRow, String> colBalance = new TableColumn<>("Số Dư (VNĐ)");
        colBalance.setCellValueFactory(new PropertyValueFactory<>("formattedBalance"));
        colBalance.setPrefWidth(150);
        colBalance.setStyle("-fx-alignment: CENTER-RIGHT;");
        
        // Cột thông tin thêm
        TableColumn<AccountTableRow, String> colExtra = new TableColumn<>("Thông Tin Thêm");
        colExtra.setCellValueFactory(new PropertyValueFactory<>("extraInfo"));
        colExtra.setPrefWidth(180);
        
        // Cột trạng thái
        TableColumn<AccountTableRow, String> colStatus = new TableColumn<>("Trạng Thái");
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colStatus.setPrefWidth(100);
        
        // Cột ngày tạo
        TableColumn<AccountTableRow, String> colCreatedAt = new TableColumn<>("Ngày Tạo");
        colCreatedAt.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        colCreatedAt.setPrefWidth(150);
        
        tableView.getColumns().addAll(colAccountNumber, colType, colBalance, colExtra, colStatus, colCreatedAt);
        tableView.setItems(accountData);
        
        // Listener khi chọn hàng
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            selectedAccount = newVal;
            if (newVal != null) {
                populateForm(newVal);
            }
        });
        
        section.getChildren().addAll(lblTable, tableView);
        return section;
    }
    
    /**
     * Tạo phần Form nhập liệu.
     */
    private HBox createFormSection() {
        HBox form = new HBox(15);
        form.setPadding(new Insets(15));
        form.setAlignment(Pos.CENTER_LEFT);
        form.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        
        // Loại tài khoản
        VBox typeBox = new VBox(5);
        Label lblType = new Label("Loại tài khoản:");
        cmbAccountType = new ComboBox<>();
        cmbAccountType.getItems().addAll("Tài khoản thanh toán (Checking)", "Tài khoản tiết kiệm (Savings)");
        cmbAccountType.setValue("Tài khoản thanh toán (Checking)");
        cmbAccountType.setPrefWidth(200);
        cmbAccountType.setOnAction(e -> updateExtraParamLabel());
        typeBox.getChildren().addAll(lblType, cmbAccountType);
        
        // Số dư ban đầu
        VBox balanceBox = new VBox(5);
        Label lblBalance = new Label("Số dư ban đầu (VNĐ):");
        txtInitialBalance = new TextField();
        txtInitialBalance.setPromptText("Nhập số tiền");
        txtInitialBalance.setPrefWidth(150);
        balanceBox.getChildren().addAll(lblBalance, txtInitialBalance);
        
        // Thông số bổ sung (Hạn mức thấu chi / Lãi suất)
        VBox extraBox = new VBox(5);
        lblExtraParam = new Label("Hạn mức thấu chi (VNĐ):");
        txtExtraParam = new TextField();
        txtExtraParam.setPromptText("0");
        txtExtraParam.setPrefWidth(150);
        extraBox.getChildren().addAll(lblExtraParam, txtExtraParam);
        
        form.getChildren().addAll(typeBox, balanceBox, extraBox);
        
        return form;
    }
    
    /**
     * Cập nhật label thông số bổ sung theo loại tài khoản.
     */
    private void updateExtraParamLabel() {
        String selected = cmbAccountType.getValue();
        if (selected != null && selected.contains("Savings")) {
            lblExtraParam.setText("Lãi suất (%/năm):");
            txtExtraParam.setPromptText("Ví dụ: 5.5");
        } else {
            lblExtraParam.setText("Hạn mức thấu chi (VNĐ):");
            txtExtraParam.setPromptText("0");
        }
    }
    
    /**
     * Tạo phần nút bấm.
     */
    private HBox createButtonSection() {
        HBox buttons = new HBox(15);
        buttons.setAlignment(Pos.CENTER);
        buttons.setPadding(new Insets(10));
        
        // Nút Thêm
        Button btnAdd = new Button("➕ Thêm Tài Khoản");
        btnAdd.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 20;");
        btnAdd.setOnAction(e -> handleAdd());
        
        // Nút Sửa (Nạp/Rút tiền)
        Button btnUpdate = new Button("✏️ Nạp Tiền Vào TK");
        btnUpdate.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 20;");
        btnUpdate.setOnAction(e -> handleDeposit());
        
        // Nút Xóa (Đóng tài khoản)
        Button btnDelete = new Button("🗑️ Đóng Tài Khoản");
        btnDelete.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 20;");
        btnDelete.setOnAction(e -> handleDelete());
        
        // Nút Làm mới
        Button btnRefresh = new Button("🔄 Làm Mới");
        btnRefresh.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 20;");
        btnRefresh.setOnAction(e -> refresh());
        
        // Nút Xóa form
        Button btnClear = new Button("🧹 Xóa Form");
        btnClear.setStyle("-fx-background-color: #9E9E9E; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 20;");
        btnClear.setOnAction(e -> clearForm());
        
        buttons.getChildren().addAll(btnAdd, btnUpdate, btnDelete, btnRefresh, btnClear);
        
        return buttons;
    }
    
    /**
     * Xử lý thêm tài khoản.
     */
    private void handleAdd() {
        User user = app.getCurrentUser();
        if (user == null) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Vui lòng đăng nhập trước!");
            return;
        }
        
        // Validate số dư
        double initialBalance;
        try {
            String balanceText = txtInitialBalance.getText().trim().replace(",", "");
            initialBalance = balanceText.isEmpty() ? 0 : Double.parseDouble(balanceText);
            if (initialBalance < 0) {
                showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Số dư không được âm!");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Số dư không hợp lệ!");
            txtInitialBalance.requestFocus();
            return;
        }
        
        // Validate thông số bổ sung
        double extraParam;
        try {
            String extraText = txtExtraParam.getText().trim().replace(",", "");
            extraParam = extraText.isEmpty() ? 0 : Double.parseDouble(extraText);
            if (extraParam < 0) {
                showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Thông số không được âm!");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Thông số bổ sung không hợp lệ!");
            txtExtraParam.requestFocus();
            return;
        }
        
        // Tạo tài khoản
        String selected = cmbAccountType.getValue();
        Account newAccount;
        
        if (selected != null && selected.contains("Savings")) {
            // Tài khoản tiết kiệm - extraParam là lãi suất (%)
            newAccount = app.getAccountService().createSavingsAccount(user, initialBalance, extraParam);
        } else {
            // Tài khoản thanh toán - extraParam là hạn mức thấu chi
            newAccount = app.getAccountService().createCheckingAccount(user, initialBalance, extraParam);
        }
        
        if (newAccount != null) {
            showAlert(Alert.AlertType.INFORMATION, "Thành công", 
                "Tạo tài khoản thành công!\nSố tài khoản: " + newAccount.getAccountNumber());
            clearForm();
            refresh();
            app.refreshDashboard(); // Cập nhật tổng số dư trên header
        } else {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tạo tài khoản!");
        }
    }
    
    /**
     * Xử lý nạp tiền vào tài khoản đã chọn.
     */
    private void handleDeposit() {
        if (selectedAccount == null) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Vui lòng chọn tài khoản từ bảng!");
            return;
        }
        
        // Hiển thị dialog nhập số tiền
        TextInputDialog dialog = new TextInputDialog("0");
        dialog.setTitle("Nạp Tiền");
        dialog.setHeaderText("Nạp tiền vào tài khoản: " + selectedAccount.getAccountNumber());
        dialog.setContentText("Nhập số tiền (VNĐ):");
        
        dialog.showAndWait().ifPresent(amountStr -> {
            try {
                double amount = Double.parseDouble(amountStr.replace(",", ""));
                if (amount <= 0) {
                    showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Số tiền phải lớn hơn 0!");
                    return;
                }
                
                boolean success = app.getTransactionService().deposit(
                    selectedAccount.getAccountNumber(), amount, "Nạp tiền qua giao diện");
                
                if (success) {
                    showAlert(Alert.AlertType.INFORMATION, "Thành công", 
                        String.format("Nạp tiền thành công!\nSố tiền: %,.0f VNĐ", amount));
                    refresh();
                    app.refreshDashboard(); // Cập nhật tổng số dư trên header
                } else {
                    showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể nạp tiền!");
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Số tiền không hợp lệ!");
            }
        });
    }
    
    /**
     * Xử lý đóng tài khoản.
     */
    private void handleDelete() {
        if (selectedAccount == null) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Vui lòng chọn tài khoản từ bảng!");
            return;
        }
        
        // Xác nhận
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Xác nhận");
        confirm.setHeaderText("Đóng tài khoản");
        confirm.setContentText("Bạn có chắc chắn muốn đóng tài khoản " + 
            selectedAccount.getAccountNumber() + "?\nHành động này không thể hoàn tác!");
        
        if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            boolean success = app.getAccountService().closeAccount(selectedAccount.getAccountNumber());
            
            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Thành công", "Đã đóng tài khoản!");
                refresh();
                app.refreshDashboard(); // Cập nhật tổng số dư trên header
            } else {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể đóng tài khoản!");
            }
        }
    }
    
    /**
     * Điền thông tin vào form từ hàng đã chọn.
     */
    private void populateForm(AccountTableRow row) {
        if (row.getAccountType().contains("Tiết kiệm")) {
            cmbAccountType.setValue("Tài khoản tiết kiệm (Savings)");
        } else {
            cmbAccountType.setValue("Tài khoản thanh toán (Checking)");
        }
        txtInitialBalance.setText(String.format("%.0f", row.getBalance()));
        updateExtraParamLabel();
    }
    
    /**
     * Xóa form.
     */
    private void clearForm() {
        cmbAccountType.setValue("Tài khoản thanh toán (Checking)");
        txtInitialBalance.clear();
        txtExtraParam.clear();
        tableView.getSelectionModel().clearSelection();
        selectedAccount = null;
        updateExtraParamLabel();
    }
    
    /**
     * Làm mới dữ liệu.
     */
    public void refresh() {
        accountData.clear();
        User user = app.getCurrentUser();
        
        if (user != null) {
            for (Account account : user.getAccounts()) {
                accountData.add(new AccountTableRow(account));
            }
        }
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
     * Lấy content.
     */
    public VBox getContent() {
        return content;
    }
    
    // ============= Inner Class cho TableView =============
    
    /**
     * Class đại diện một hàng trong TableView.
     */
    public static class AccountTableRow {
        private String accountNumber;
        private String accountType;
        private double balance;
        private String formattedBalance;
        private String extraInfo;
        private String status;
        private String createdAt;
        
        public AccountTableRow(Account account) {
            this.accountNumber = account.getAccountNumber();
            this.accountType = account.getAccountType().equals("CHECKING") ? "Thanh toán" : "Tiết kiệm";
            this.balance = account.getBalance();
            this.formattedBalance = String.format("%,.0f", account.getBalance());
            this.status = account.isActive() ? "✅ Hoạt động" : "❌ Đã đóng";
            
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            this.createdAt = sdf.format(new Date(account.getCreatedAt()));
            
            // Thông tin thêm
            if (account instanceof CheckingAccount) {
                CheckingAccount ca = (CheckingAccount) account;
                this.extraInfo = String.format("Thấu chi: %,.0f VNĐ", ca.getOverdraftLimit());
            } else if (account instanceof SavingsAccount) {
                SavingsAccount sa = (SavingsAccount) account;
                this.extraInfo = String.format("Lãi suất: %.2f%%/năm", sa.getInterestRate());
            } else {
                this.extraInfo = "-";
            }
        }
        
        // Getters
        public String getAccountNumber() { return accountNumber; }
        public String getAccountType() { return accountType; }
        public double getBalance() { return balance; }
        public String getFormattedBalance() { return formattedBalance; }
        public String getExtraInfo() { return extraInfo; }
        public String getStatus() { return status; }
        public String getCreatedAt() { return createdAt; }
    }
}
