package org.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDatabase implements UserRepository {
    private static MySQLDatabase mySQLDatabase = new MySQLDatabase();

    public void setMySQLDatabase(MySQLDatabase mySQLDatabase) {
        this.mySQLDatabase = mySQLDatabase;
    }
    public MySQLDatabase getMySQLDatabase() {
        return mySQLDatabase;
    }
    @Override
    public boolean setImageToDatabase(String username, byte[] avatarBytes) {
        String sql = "UPDATE accounts SET avatar = ? WHERE username = ?";
        try (PreparedStatement preparedStatement = mySQLDatabase.connect().prepareStatement(sql)) {
            preparedStatement.setBytes(1, avatarBytes);
            preparedStatement.setString(2, username);

            int rowsUpdated = preparedStatement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean isUserExists(String cccd) {
        String checkQuery = "SELECT COUNT(*) FROM readers WHERE cccd = ?";
        try (Connection connection = mySQLDatabase.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(checkQuery)) {

            preparedStatement.setString(1, cccd);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error checking user: " + e.getMessage());
        }
        return false;
    }

    @Override
    public void insertUser(String name, String phone, String address, String cccd) {
        String sql = "INSERT INTO CUSTOMERS(user_name, phone_number,address, CCCD) VALUES(?, ?, ?, ?)";
        try (Connection conn = mySQLDatabase.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setString(2, phone);
            pstmt.setString(3, address);
            pstmt.setString(4, cccd);


            int rowsAffected = pstmt.executeUpdate();
            System.out.println(rowsAffected + " user(s) inserted.");

        } catch (SQLException e) {
            System.out.println("Error inserting user: " + e.getMessage());
        }
    }

    @Override
    public List<Customer> getCustomers() throws SQLException {
        List<Customer> customers = new ArrayList<>();

        String query = "SELECT * FROM customers";

        try (Connection connection = mySQLDatabase.connect();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("user_id");
                String name = resultSet.getString("user_name");
                String phone = resultSet.getString("phone_number");
                String address = resultSet.getString("address");
                String cccd = resultSet.getString("CCCD");

                customers.add(new Customer(id, name, phone, address, cccd));
            }
        }

        return customers;
    }

    @Override
    public ObservableList<Customer> getUsersByMultipleSearchTerms(String searchID, String searchName, String searchPhone, String searchCCCD) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT * FROM Customers WHERE 1=1");

        if (!searchID.isEmpty()) {
            sql.append(" AND user_id LIKE ?");
        }
        if (!searchName.isEmpty()) {
            sql.append(" AND user_name LIKE ?");
        }
        if (!searchPhone.isEmpty()) {
            sql.append(" AND phone_number LIKE ?");
        }
        if (!searchCCCD.isEmpty()) {
            sql.append(" AND CCCD LIKE ?");
        }

        ObservableList<Customer> customerList = FXCollections.observableArrayList();

        try (Connection conn = mySQLDatabase.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

            int paramIndex = 1;


            if (!searchID.isEmpty()) {
                pstmt.setString(paramIndex++, "%" + searchID + "%");
            }
            if (!searchName.isEmpty()) {
                pstmt.setString(paramIndex++, "%" + searchName + "%");
            }
            if (!searchPhone.isEmpty()) {
                pstmt.setString(paramIndex++, "%" + searchPhone + "%");
            }
            if (!searchCCCD.isEmpty()) {
                pstmt.setString(paramIndex++, "%" + searchCCCD + "%");
            }

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                customerList.add(new Customer(
                        rs.getInt("user_id"),
                        rs.getString("user_name"),
                        rs.getString("phone_number"),
                        rs.getString("address"),
                        rs.getString("CCCD")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching users: " + e.getMessage());
            throw e;
        }

        return customerList;
    }

    @Override
    public void deleteCustomer(int customerId) throws SQLException {
        String sql = "DELETE FROM customers WHERE user_id = ?";
        try (Connection conn = mySQLDatabase.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, customerId);
            stmt.executeUpdate();
        }
    }

    @Override
    public boolean validAccount(String username, String password) {
        String query = "SELECT * FROM Accounts WHERE username = ? AND password = ?";
        try (Connection conn = mySQLDatabase.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean isUsernameTaken(String username) {
        String query = "SELECT * FROM Accounts WHERE username = ?";

        try (Connection conn = mySQLDatabase.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void registerAccount(String fullname,
                                       String email,
                                       String phone,
                                       String username,
                                       String password
    ) {
        String query = "INSERT INTO Accounts (username, full_name, email, phone_number, password) VALUES (?, ?, ?, ?, ?)";


        try (Connection conn = mySQLDatabase.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, fullname);
            stmt.setString(3, email);
            stmt.setString(4, phone);
            stmt.setString(5, password);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean changePassword(String username, String oldPassword, String newPassword) {
        String querySelect = "SELECT password FROM accounts WHERE username = ?";
        String queryUpdate = "UPDATE accounts SET password = ? WHERE username = ?";

        try (Connection conn = mySQLDatabase.connect();
             PreparedStatement selectStmt = conn.prepareStatement(querySelect)) {

            
            selectStmt.setString(1, username);
            ResultSet rs = selectStmt.executeQuery();
            if (rs.next()) {
                String currentPassword = rs.getString("password");

                if (!currentPassword.equals(oldPassword)) {
                    System.out.println("Mật khẩu cũ không đúng!");
                    return false;
                }
            } else {
                System.out.println("Không tìm thấy tài khoản với username này!");
                return false;
            }


            try (PreparedStatement updateStmt = conn.prepareStatement(queryUpdate)) {
                updateStmt.setString(1, newPassword);
                updateStmt.setString(2, username);

                int rowsUpdated = updateStmt.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("Đổi mật khẩu thành công!");
                    return true;
                } else {
                    System.out.println("Đổi mật khẩu thất bại!");
                    return false;
                }
            }

        } catch (SQLException e) {
            System.out.println("Lỗi khi thay đổi mật khẩu: " + e.getMessage());
            return false;
        }
    }

    @Override
    public String getFullName(String userName) {
        String fullName = null;
        String query = "SELECT full_name FROM accounts WHERE username = ?";

        try (PreparedStatement statement = mySQLDatabase.connect().prepareStatement(query)) {
            statement.setString(1, userName); 
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    fullName = resultSet.getString("full_name");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fullName;
    }

    @Override
    public String getPhoneNumber(String userName) {
        String phoneNumber = null;
        String query = "SELECT phone_number FROM accounts WHERE username = ?";

        try (PreparedStatement statement = mySQLDatabase.connect().prepareStatement(query)) {
            statement.setString(1, userName); 
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    phoneNumber = resultSet.getString("phone_number");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return phoneNumber;
    }

    @Override
    public String getEmail(String userName) {
        String email = null;
        String query = "SELECT email FROM accounts WHERE username = ?";

        try (PreparedStatement statement = mySQLDatabase.connect().prepareStatement(query)) {
            statement.setString(1, userName); 
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    email = resultSet.getString("email");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return email;
    }

    @Override
    public boolean updateUserInformation(String full_name, String phoneNumber, String email, String user_name) {
        String sql = "UPDATE accounts SET full_name = ?, phone_number = ?, email = ? WHERE username = ?";

        try (Connection conn = mySQLDatabase.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, full_name);
            pstmt.setString(2, phoneNumber);
            pstmt.setString(3, email);
            pstmt.setString(4, user_name);

            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.out.println("Error updating user information: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Account getAccountByUsername(String username) {
        String query = "SELECT username, password, avatar FROM accounts WHERE username = ?";

        try (Connection connection = mySQLDatabase.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, username);


            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String userName = resultSet.getString("username");
                String password = resultSet.getString("password");
                byte[] avatar = resultSet.getBytes("avatar"); 

                return new Account(userName, password, avatar);
            } else {
                System.out.println("Không tìm thấy tài khoản với username: " + username);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean checkEmailExists(String email) {

        String sql = "SELECT 1 FROM accounts WHERE email = ?";


        if (email == null || email.trim().isEmpty()) {
            return false;
        }

        try (Connection conn = mySQLDatabase.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {


            pstmt.setString(1, email.trim());

            ResultSet rs = pstmt.executeQuery();

            return rs.next();
        } catch (SQLException e) {
            System.out.println("Error checking email: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updatenewPass(String email, String newPassword) {

        String queryUpdate = "UPDATE accounts SET password = ? WHERE email = ?";

        try (Connection conn = mySQLDatabase.connect();
             PreparedStatement updateStmt = conn.prepareStatement(queryUpdate)) {


            updateStmt.setString(1, newPassword);
            updateStmt.setString(2, email);

            
            int rowsUpdated = updateStmt.executeUpdate();
            
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi khi thay đổi mật khẩu: " + e.getMessage());
            return false;
        }
    }

    @Override
    public int countUsers() {
        String query = "SELECT COUNT(user_id) FROM customers";
        try (Connection conn = mySQLDatabase.connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) { 
                return rs.getInt(1); 
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0; 
    }
}