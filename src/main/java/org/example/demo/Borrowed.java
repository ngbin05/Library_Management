package org.example.demo;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Borrowed {
    private int borrowId;
    private int userId;
    private String user_name;
    private List<Book> books = new ArrayList<>();
    private String borrowDate;
    private String returnDate;
    private String status;


    public Borrowed(int borrowId, int userId, String user_name, List<Book> books, String borrowDate, String returnDate, String status) {
        this.borrowId = borrowId;
        this.userId = userId;
        this.user_name = user_name;
        this.books = books;

        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.status = status;
    }

    public Borrowed(int borrowId, int userId, Date ngayMuon, Date ngayTra, String tinhTrang) {
        this.borrowId = borrowId;
        this.userId = userId;
        this.user_name = user_name;
        this.borrowDate = ngayMuon.toString();
        this.returnDate = ngayTra.toString();
    }

    public Borrowed(String uid, String userName, int soLuong, Date ngayMuon, Date ngayTra, String status) {
        this.userId = userId;
        this.user_name = userName;
        this.borrowDate = ngayMuon.toString();
        this.returnDate = ngayTra.toString();
        this.status = status;
    }

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

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
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