package com.tonevella.btl;

public class Account {
    public boolean account(String username, String password) {
        return "admin".equals(username) && "1234".equals(password);
    }
}
