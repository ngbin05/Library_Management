package org.example.btloop;

public class Account {
    public static boolean processLogin(String username, String password) {
        // Logic đăng nhập ở đây
        return username.equals("admin") && password.equals("admin123");
    }
}
