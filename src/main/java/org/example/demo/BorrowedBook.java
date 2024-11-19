package org.example.demo;

import java.sql.Date;

public class BorrowedBook {
    private int id;
    private String title;
    private String author;
    private Date borrowDate;
    private Date returnDate;

    public BorrowedBook(int id, String title, String author, Date borrowDate, Date returnDate) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
    }

    // Getter và Setter
}
