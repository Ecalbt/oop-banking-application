package com.bankapp.data;

import com.bankapp.model.User;
import java.util.HashMap;
import java.util.Map;

/**
 * UserRepository - Đối tượng truy xuất dữ liệu cho thực thể User.
 * Xử lý việc lưu trữ và truy vấn người dùng.
 */
public class UserRepository {
    private Map<String, User> users; // userId -> User

    public UserRepository() {
        this.users = new HashMap<>();
    }

    /**
     * Lưu một người dùng vào repository.
     *
     * @param user Đối tượng User cần lưu
     * @return true nếu lưu thành công, false nếu người dùng đã tồn tại
     */
    public boolean save(User user) {
        if (user == null || users.containsKey(user.getUserId())) {
            return false;
        }
        users.put(user.getUserId(), user);
        return true;
    }

    /**
     * Tìm người dùng theo ID.
     *
     * @param userId ID người dùng cần tìm
     * @return Đối tượng User nếu tìm thấy, null nếu không
     */
    public User findById(String userId) {
        return users.get(userId);
    }

    /**
     * Tìm người dùng theo tên đăng nhập.
     *
     * @param username Tên đăng nhập cần tìm
     * @return Đối tượng User nếu tìm thấy, null nếu không
     */
    public User findByUsername(String username) {
        return users.values().stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    /**
     * Cập nhật một người dùng đã tồn tại.
     *
     * @param user Đối tượng User cần cập nhật
     * @return true nếu cập nhật thành công, false nếu không tìm thấy người dùng
     */
    public boolean update(User user) {
        if (user == null || !users.containsKey(user.getUserId())) {
            return false;
        }
        users.put(user.getUserId(), user);
        return true;
    }

    /**
     * Xóa một người dùng khỏi repository.
     *
     * @param userId ID người dùng cần xóa
     * @return true nếu xóa thành công, false nếu không tìm thấy
     */
    public boolean delete(String userId) {
        return users.remove(userId) != null;
    }

    /**
     * Kiểm tra người dùng có tồn tại theo ID hay không.
     *
     * @param userId ID người dùng cần kiểm tra
     * @return true nếu tồn tại, false nếu không
     */
    public boolean exists(String userId) {
        return users.containsKey(userId);
    }

    /**
     * Kiểm tra một tên đăng nhập đã được sử dụng hay chưa.
     *
     * @param username Tên đăng nhập cần kiểm tra
     * @return true nếu đã tồn tại, false nếu chưa
     */
    public boolean usernameExists(String username) {
        return users.values().stream()
                .anyMatch(user -> user.getUsername().equals(username));
    }

    /**
     * Lấy tổng số lượng người dùng.
     *
     * @return Số lượng người dùng
     */
    public int getUserCount() {
        return users.size();
    }

    /**
     * Xóa toàn bộ người dùng khỏi repository.
     */
    public void clear() {
        users.clear();
    }
}
