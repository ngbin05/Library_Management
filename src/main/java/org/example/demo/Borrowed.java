package org.example.demo;
import java.util.List;

public class Borrowed {
    private int borrowId;
    private int userId;
    private String user_name;
    private List<String> bookTitles; // List chứa tên các cuốn sách
    private String borrowDate;
    private String returnDate;
    private String status;

    public Borrowed(int borrowId, int userId,String user_name, List<String> bookTitles, String borrowDate, String returnDate, String status) {
        this.borrowId = borrowId;
        this.userId = userId;
        this.user_name = user_name;
        this.bookTitles = bookTitles;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.status = status;
    }

    // Getter và Setter
    public int getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(int borrowId) {
        this.borrowId = borrowId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public List<String> getBookTitles() {
        return bookTitles;
    }

    public void setBookTitles(List<String> bookTitles) {
        this.bookTitles = bookTitles;
    }

    public String getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(String borrowDate) {
        this.borrowDate = borrowDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}