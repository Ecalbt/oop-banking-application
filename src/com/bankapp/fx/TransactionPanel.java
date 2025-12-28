package com.bankapp.fx;

import com.bankapp.model.Account;
import com.bankapp.model.Transaction;
import com.bankapp.model.User;
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
import java.util.List;

/**
 * TransactionPanel - Panel quản lý giao dịch với TableView.
 * Cho phép nạp tiền, rút tiền, chuyển khoản và xem lịch sử giao dịch.
 */
public class TransactionPanel {
    
    private BankAppFX app;
    private VBox content;
    
    // TableView giao dịch
    private TableView<TransactionTableRow> tableView;
    private ObservableList<TransactionTableRow> transactionData;
    
    // ComboBox chọn tài khoản
    private ComboBox<String> cmbAccount;
    
    // Form giao dịch
    private ComboBox<String> cmbTransactionType;
    private TextField txtAmount;
    private TextField txtTargetAccount;
    private TextField txtDescription;
    private Label lblTargetAccount;
    
    public TransactionPanel(BankAppFX app) {
        this.app = app;
        transactionData = FXCollections.observableArrayList();
        createUI();
    }
    
    /**
     * Tạo giao diện người dùng.
     */
    private void createUI() {
        content = new VBox(15);
        content.setPadding(new Insets(20));
        
        // Tiêu đề
        Label lblTitle = new Label("Quản Lý Giao Dịch");
        lblTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        
        // Phần chọn tài khoản
        HBox accountSelector = createAccountSelector();
        
        // Phần Form giao dịch
        VBox formSection = createFormSection();
        
        // Phần TableView lịch sử giao dịch
        VBox tableSection = createTableSection();
        
        content.getChildren().addAll(lblTitle, accountSelector, formSection, tableSection);
    }
    
    /**
     * Tạo phần chọn tài khoản.
     */
    private HBox createAccountSelector() {
        HBox selector = new HBox(15);
        selector.setAlignment(Pos.CENTER_LEFT);
        selector.setPadding(new Insets(10));
        selector.setStyle("-fx-background-color: #E3F2FD; -fx-background-radius: 10;");
        
        Label lblAccount = new Label("Chọn tài khoản:");
        lblAccount.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        
        cmbAccount = new ComboBox<>();
        cmbAccount.setPrefWidth(300);
        cmbAccount.setPromptText("-- Chọn tài khoản --");
        cmbAccount.setOnAction(e -> loadTransactionHistory());
        
        // Nút refresh
        Button btnRefreshAccounts = new Button("🔄");
        btnRefreshAccounts.setOnAction(e -> refreshAccountList());
        
        // Thông tin số dư
        Label lblBalance = new Label("Số dư: --");
        lblBalance.setStyle("-fx-font-weight: bold; -fx-text-fill: #1976D2;");
        cmbAccount.setUserData(lblBalance);
        
        selector.getChildren().addAll(lblAccount, cmbAccount, btnRefreshAccounts, lblBalance);
        
        return selector;
    }
    
    /**
     * Tạo phần Form giao dịch.
     */
    private VBox createFormSection() {
        VBox form = new VBox(10);
        form.setPadding(new Insets(15));
        form.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        
        Label lblFormTitle = new Label("Thực Hiện Giao Dịch");
        lblFormTitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        
        // Grid layout cho form
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER_LEFT);
        
        // Loại giao dịch
        Label lblType = new Label("Loại giao dịch:");
        cmbTransactionType = new ComboBox<>();
        cmbTransactionType.getItems().addAll("💰 Nạp tiền", "💸 Rút tiền", "🔄 Chuyển khoản");
        cmbTransactionType.setValue("💰 Nạp tiền");
        cmbTransactionType.setPrefWidth(200);
        cmbTransactionType.setOnAction(e -> updateFormForTransactionType());
        
        // Số tiền
        Label lblAmount = new Label("Số tiền (VNĐ):");
        txtAmount = new TextField();
        txtAmount.setPromptText("Nhập số tiền");
        txtAmount.setPrefWidth(200);
        
        // Tài khoản đích (cho chuyển khoản)
        lblTargetAccount = new Label("TK nhận:");
        txtTargetAccount = new TextField();
        txtTargetAccount.setPromptText("Số tài khoản nhận");
        txtTargetAccount.setPrefWidth(200);
        txtTargetAccount.setVisible(false);
        lblTargetAccount.setVisible(false);
        
        // Mô tả
        Label lblDescription = new Label("Mô tả:");
        txtDescription = new TextField();
        txtDescription.setPromptText("Mô tả giao dịch (tùy chọn)");
        txtDescription.setPrefWidth(200);
        
        grid.add(lblType, 0, 0);
        grid.add(cmbTransactionType, 1, 0);
        grid.add(lblAmount, 2, 0);
        grid.add(txtAmount, 3, 0);
        grid.add(lblTargetAccount, 0, 1);
        grid.add(txtTargetAccount, 1, 1);
        grid.add(lblDescription, 2, 1);
        grid.add(txtDescription, 3, 1);
        
        // Nút thực hiện
        HBox buttons = new HBox(15);
        buttons.setAlignment(Pos.CENTER);
        buttons.setPadding(new Insets(10, 0, 0, 0));
        
        Button btnExecute = new Button("✅ Thực Hiện Giao Dịch");
        btnExecute.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 25;");
        btnExecute.setOnAction(e -> executeTransaction());
        
        Button btnClear = new Button("🧹 Xóa Form");
        btnClear.setStyle("-fx-background-color: #9E9E9E; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 25;");
        btnClear.setOnAction(e -> clearForm());
        
        buttons.getChildren().addAll(btnExecute, btnClear);
        
        form.getChildren().addAll(lblFormTitle, grid, buttons);
        
        return form;
    }
    
    /**
     * Tạo phần TableView lịch sử giao dịch.
     */
    private VBox createTableSection() {
        VBox section = new VBox(10);
        section.setPadding(new Insets(10, 0, 0, 0));
        
        Label lblHistory = new Label("Lịch Sử Giao Dịch");
        lblHistory.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        
        // Tạo TableView
        tableView = new TableView<>();
        tableView.setPlaceholder(new Label("Chưa có giao dịch nào"));
        tableView.setPrefHeight(200);
        
        // Cột ID giao dịch
        TableColumn<TransactionTableRow, String> colId = new TableColumn<>("Mã GD");
        colId.setCellValueFactory(new PropertyValueFactory<>("transactionId"));
        colId.setPrefWidth(150);
        
        // Cột loại giao dịch
        TableColumn<TransactionTableRow, String> colType = new TableColumn<>("Loại");
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colType.setPrefWidth(120);
        
        // Cột số tiền
        TableColumn<TransactionTableRow, String> colAmount = new TableColumn<>("Số Tiền (VNĐ)");
        colAmount.setCellValueFactory(new PropertyValueFactory<>("formattedAmount"));
        colAmount.setPrefWidth(130);
        colAmount.setStyle("-fx-alignment: CENTER-RIGHT;");
        
        // Cột tài khoản liên quan
        TableColumn<TransactionTableRow, String> colRelatedAccount = new TableColumn<>("TK Liên Quan");
        colRelatedAccount.setCellValueFactory(new PropertyValueFactory<>("relatedAccount"));
        colRelatedAccount.setPrefWidth(130);
        
        // Cột mô tả
        TableColumn<TransactionTableRow, String> colDescription = new TableColumn<>("Mô Tả");
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colDescription.setPrefWidth(180);
        
        // Cột thời gian
        TableColumn<TransactionTableRow, String> colTime = new TableColumn<>("Thời Gian");
        colTime.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        colTime.setPrefWidth(150);
        
        // Cột trạng thái
        TableColumn<TransactionTableRow, String> colStatus = new TableColumn<>("Trạng Thái");
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colStatus.setPrefWidth(90);
        
        tableView.getColumns().addAll(colId, colType, colAmount, colRelatedAccount, colDescription, colTime, colStatus);
        tableView.setItems(transactionData);
        
        section.getChildren().addAll(lblHistory, tableView);
        
        return section;
    }
    
    /**
     * Cập nhật form theo loại giao dịch.
     */
    private void updateFormForTransactionType() {
        String selected = cmbTransactionType.getValue();
        boolean isTransfer = selected != null && selected.contains("Chuyển khoản");
        
        lblTargetAccount.setVisible(isTransfer);
        txtTargetAccount.setVisible(isTransfer);
    }
    
    /**
     * Thực hiện giao dịch.
     */
    private void executeTransaction() {
        // Validate tài khoản
        String selectedAccount = cmbAccount.getValue();
        if (selectedAccount == null || selectedAccount.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Vui lòng chọn tài khoản!");
            return;
        }
        
        // Lấy số tài khoản từ chuỗi hiển thị (format: "ACC-xxx | Loại | Số dư")
        String accountNumber = selectedAccount.split(" \\| ")[0].trim();
        
        // Validate số tiền
        double amount;
        try {
            String amountText = txtAmount.getText().trim().replace(",", "");
            if (amountText.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Vui lòng nhập số tiền!");
                txtAmount.requestFocus();
                return;
            }
            amount = Double.parseDouble(amountText);
            if (amount <= 0) {
                showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Số tiền phải lớn hơn 0!");
                txtAmount.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Số tiền không hợp lệ!");
            txtAmount.requestFocus();
            return;
        }
        
        String description = txtDescription.getText().trim();
        if (description.isEmpty()) {
            description = "Giao dịch qua giao diện";
        }
        
        String transactionType = cmbTransactionType.getValue();
        boolean success = false;
        String resultMessage = "";
        
        if (transactionType.contains("Nạp tiền")) {
            success = app.getTransactionService().deposit(accountNumber, amount, description);
            resultMessage = success ? 
                String.format("Nạp tiền thành công!\nSố tiền: %,.0f VNĐ", amount) :
                "Nạp tiền thất bại!";
                
        } else if (transactionType.contains("Rút tiền")) {
            success = app.getTransactionService().withdraw(accountNumber, amount, description);
            if (success) {
                resultMessage = String.format("Rút tiền thành công!\nSố tiền: %,.0f VNĐ", amount);
            } else {
                resultMessage = "Rút tiền thất bại!\nCó thể do:\n- Số dư không đủ\n- Vượt quá hạn mức rút\n- Tài khoản không hoạt động";
            }
            
        } else if (transactionType.contains("Chuyển khoản")) {
            String targetAccount = txtTargetAccount.getText().trim();
            if (targetAccount.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Vui lòng nhập số tài khoản nhận!");
                txtTargetAccount.requestFocus();
                return;
            }
            
            success = app.getTransactionService().transfer(accountNumber, targetAccount, amount, description);
            if (success) {
                resultMessage = String.format("Chuyển khoản thành công!\nSố tiền: %,.0f VNĐ\nTK nhận: %s", amount, targetAccount);
            } else {
                resultMessage = "Chuyển khoản thất bại!\nCó thể do:\n- Số dư không đủ\n- Tài khoản nhận không tồn tại\n- Tài khoản không hoạt động";
            }
        }
        
        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Thành công", resultMessage);
            clearForm();
            refreshAccountList();
            loadTransactionHistory();
        } else {
            showAlert(Alert.AlertType.ERROR, "Lỗi", resultMessage);
        }
    }
    
    /**
     * Làm mới danh sách tài khoản.
     */
    private void refreshAccountList() {
        String currentSelection = cmbAccount.getValue();
        cmbAccount.getItems().clear();
        
        User user = app.getCurrentUser();
        if (user != null) {
            for (Account account : user.getAccounts()) {
                if (account.isActive()) {
                    String display = String.format("%s | %s | %,.0f VNĐ",
                        account.getAccountNumber(),
                        account.getAccountType().equals("CHECKING") ? "Thanh toán" : "Tiết kiệm",
                        account.getBalance());
                    cmbAccount.getItems().add(display);
                }
            }
        }
        
        // Restore selection if possible
        if (currentSelection != null && cmbAccount.getItems().contains(currentSelection)) {
            cmbAccount.setValue(currentSelection);
        } else if (!cmbAccount.getItems().isEmpty()) {
            cmbAccount.getSelectionModel().selectFirst();
        }
        
        updateBalanceLabel();
    }
    
    /**
     * Cập nhật label số dư.
     */
    private void updateBalanceLabel() {
        Label lblBalance = (Label) cmbAccount.getUserData();
        String selected = cmbAccount.getValue();
        
        if (selected != null && !selected.isEmpty()) {
            String accountNumber = selected.split(" \\| ")[0].trim();
            Account account = app.getAccountService().getAccount(accountNumber);
            if (account != null) {
                lblBalance.setText(String.format("Số dư: %,.0f VNĐ", account.getBalance()));
            }
        } else {
            lblBalance.setText("Số dư: --");
        }
    }
    
    /**
     * Tải lịch sử giao dịch.
     */
    private void loadTransactionHistory() {
        transactionData.clear();
        updateBalanceLabel();
        
        String selected = cmbAccount.getValue();
        if (selected == null || selected.isEmpty()) {
            return;
        }
        
        String accountNumber = selected.split(" \\| ")[0].trim();
        List<Transaction> transactions = app.getTransactionService().getTransactionHistory(accountNumber);
        
        // Hiển thị giao dịch mới nhất trước
        for (int i = transactions.size() - 1; i >= 0; i--) {
            transactionData.add(new TransactionTableRow(transactions.get(i)));
        }
    }
    
    /**
     * Xóa form.
     */
    private void clearForm() {
        cmbTransactionType.setValue("💰 Nạp tiền");
        txtAmount.clear();
        txtTargetAccount.clear();
        txtDescription.clear();
        updateFormForTransactionType();
    }
    
    /**
     * Làm mới dữ liệu.
     */
    public void refresh() {
        refreshAccountList();
        loadTransactionHistory();
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
    public static class TransactionTableRow {
        private String transactionId;
        private String type;
        private double amount;
        private String formattedAmount;
        private String relatedAccount;
        private String description;
        private String timestamp;
        private String status;
        
        public TransactionTableRow(Transaction transaction) {
            this.transactionId = transaction.getTransactionId();
            this.amount = transaction.getAmount();
            this.formattedAmount = String.format("%,.0f", transaction.getAmount());
            this.description = transaction.getDescription();
            this.status = transaction.getStatus().equals("SUCCESS") ? "✅" : "❌";
            
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            this.timestamp = sdf.format(new Date(transaction.getTimestamp()));
            
            // Loại giao dịch
            switch (transaction.getType()) {
                case "DEPOSIT":
                    this.type = "💰 Nạp tiền";
                    this.relatedAccount = "-";
                    break;
                case "WITHDRAWAL":
                    this.type = "💸 Rút tiền";
                    this.relatedAccount = "-";
                    break;
                case "TRANSFER_OUT":
                    this.type = "🔄 Chuyển đi";
                    this.relatedAccount = transaction.getToAccountNumber();
                    break;
                case "TRANSFER_IN":
                    this.type = "📥 Nhận tiền";
                    this.relatedAccount = transaction.getToAccountNumber();
                    break;
                case "INTEREST":
                    this.type = "📈 Lãi suất";
                    this.relatedAccount = "-";
                    break;
                case "WITHDRAWAL_PENALTY":
                    this.type = "⚠️ Phí phạt";
                    this.relatedAccount = "-";
                    break;
                default:
                    this.type = transaction.getType();
                    this.relatedAccount = "-";
            }
        }
        
        // Getters
        public String getTransactionId() { return transactionId; }
        public String getType() { return type; }
        public double getAmount() { return amount; }
        public String getFormattedAmount() { return formattedAmount; }
        public String getRelatedAccount() { return relatedAccount; }
        public String getDescription() { return description; }
        public String getTimestamp() { return timestamp; }
        public String getStatus() { return status; }
    }
}
