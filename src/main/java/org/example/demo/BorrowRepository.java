package org.example.demo;

import javafx.collections.ObservableList;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public interface BorrowRepository {
    public int countBooksBorrow();
    public boolean insertBorrowRecord(int userId, List<Integer> bookIds, Date borrowDate, Date returnDate);
    public List<Borrowed> getAllBorrowData() throws SQLException;
    public void updateOverdueStatus() throws SQLException;
    public boolean returnBook(int borrowId, List<Book> books);
    public ObservableList<Borrowed> searchBorrowRecords(String borrowId, String userName, String loanDate, String status) throws SQLException;
}