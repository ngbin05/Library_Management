package org.example.demo;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Account {
    private String username;
    private String password;
    private byte[] image;
    public Account() {
    }

    public Account(String username, String password, byte[] image) {
        this.username = username;
        this.password = password;
        this.image = image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public byte[] getImage() { return image; }

    public void setImage(byte[] image) {
        this.image = image;
        setImageToDatabase(username, image);
    }

    public static boolean setImageToDatabase(String username, byte[] avatarBytes) {
        String sql = "UPDATE accounts SET avatar = ? WHERE username = ?";
        try (PreparedStatement preparedStatement = Database.connect().prepareStatement(sql)) {
            preparedStatement.setBytes(1, avatarBytes); // Gán byte[] vào cột avatar
            preparedStatement.setString(2, username);   // Gán username vào điều kiện WHERE

            int rowsUpdated = preparedStatement.executeUpdate();
            return rowsUpdated > 0; // Trả về true nếu cập nhật thành công
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Trả về false nếu có lỗi
    }
}
