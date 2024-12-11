package org.example.demo;

import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.List;

public interface UserRepository {
    public boolean setImageToDatabase(String username, byte[] avatarBytes);
    public boolean isUserExists(String cccd);

    public void insertUser(String name, String phone, String address, String cccd);

    public List<Customer> getCustomers() throws SQLException;

    public ObservableList<Customer> getUsersByMultipleSearchTerms(String searchID, String searchName, String searchPhone, String searchCCCD) throws SQLException;

    public void deleteCustomer(int customerId) throws SQLException;

    public boolean validAccount(String username, String password);

    public boolean isUsernameTaken(String username);

    public void registerAccount(String fullname,
                                String email,
                                String phone,
                                String username,
                                String password
    );

    public boolean changePassword(String username, String oldPassword, String newPassword);

    public String getFullName(String userName);

    public String getPhoneNumber(String userName);

    public String getEmail(String userName);

    public boolean updateUserInformation(String full_name, String phoneNumber, String email, String user_name);

    public Account getAccountByUsername(String username);

    public boolean checkEmailExists(String email);

    public boolean updatenewPass(String email, String newPassword);

    public int countUsers();
}