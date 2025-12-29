# Cách chạy code

## Bước 1: Tải JavaFX SDK

1. Truy cập: https://gluonhq.com/products/javafx/
2. Tải phiên bản JavaFX SDK phù hợp với hệ điều hành của bạn
3. Giải nén vào thư mục, ví dụ:
   - Windows: `C:\javafx-sdk-25.0.1`
   - Mac/Linux: `/opt/javafx-sdk-25.0.1`

- Tải javafx về sau đó tìm folder lib. (Ví dụ: “C:\javafx-sdk-25.0.1\lib”)
- Tạo Folder lib/javafx
- Copy các file trong folder lib của javafx vào Folder lib/javafx
- Chạy lệnh như sau (Thay địa chỉ javafx mà mình đã lưu)

## Bước 2: Thiết Lập Biến Môi Trường

### Windows (Command Prompt)
```cmd
set PATH_TO_FX=C:\javafx-sdk-25.0.1\lib
```

### Windows (PowerShell)
```powershell
$env:PATH_TO_FX = "C:\javafx-sdk-25.0.1\lib"
```

### Mac/Linux
```bash
export PATH_TO_FX=/opt/javafx-sdk-21/lib
```

## Bước 3: Chạy Ứng Dụng

**Windows (Command Prompt):**

```cmd
java --module-path "%PATH_TO_FX%" --add-modules javafx.controls,javafx.fxml -cp bin com.bankapp.fx.BankAppFX
```

**Windows (PowerShell):**

```powershell
java --module-path $env:PATH_TO_FX --add-modules javafx.controls,javafx.fxml -cp bin com.bankapp.fx.BankAppFX
```

**Mac/Linux:**

```bash
java --module-path "$PATH_TO_FX" --add-modules javafx.controls,javafx.fxml -cp bin com.bankapp.fx.BankAppFX
```