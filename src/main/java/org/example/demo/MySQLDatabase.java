package org.example.demo;

import java.sql.*;

public class MySQLDatabase implements DatabaseConnect {

    private static final String URL = "jdbc:mysql://localhost:3306/ThuVien";
    private static final String USER = "root";
    private static final String PASSWORD = "Binh2352005@";

    private static BookDatabase bookDatabase = new BookDatabase();
    private static UserDatabase userDatabase = new UserDatabase();
    private static BorrowDatabase borrowDatabase = new BorrowDatabase();

    public static BookDatabase getBookDatabase() {
        if (bookDatabase == null) {
            bookDatabase = new BookDatabase();
        }
        return bookDatabase;
    }

    public static UserDatabase getUserDatabase() {
        if (userDatabase == null) {
            userDatabase = new UserDatabase();
        }
        return userDatabase;
    }

    public static BorrowDatabase getBorrowDatabase() {
        if (borrowDatabase == null) {
            borrowDatabase = new BorrowDatabase();
        }
        return borrowDatabase;
    }

    private static void setBookDatabase(BookDatabase bookDatabase) {
        MySQLDatabase.bookDatabase = bookDatabase;
    }
    public static void setUserDatabase(UserDatabase userDatabase) {
        MySQLDatabase.userDatabase = userDatabase;
    }
    public static void setBorrowDatabase(BorrowDatabase borrowDatabase) {
        MySQLDatabase.borrowDatabase = borrowDatabase;
    }

    @Override
    public Connection connect() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println("Error connecting to database: " + e.getMessage());
            return null;
        }
    }
}
