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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
        MySQLDatabase.getUserDatabase().setImageToDatabase(username, image);
    }
}
