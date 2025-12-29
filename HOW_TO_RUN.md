## 6. Cách chạy code

- Tải javafx về sau đó tìm folder lib. (Ví dụ: “C:\javafx-sdk-25.0.1\lib”)
- Tạo Folder lib/javafx
- Copy các file trong folder lib của javafx vào Folder lib/javafx
- Chạy lệnh như sau (Thay địa chỉ javafx mà mình đã lưu)

```
java --module-path "(thay địa chỉ đã lưu vào đây)" --add-modules javafx.controls,javafx.fxml -cp bin com.bankapp.fx.BankAppFX
```