package org.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BorrowDatabase implements BorrowRepository {
    private static MySQLDatabase mySQLDatabase = new MySQLDatabase();

    public void setMySQLDatabase(MySQLDatabase mySQLDatabase) {
        this.mySQLDatabase = mySQLDatabase;
    }
    public MySQLDatabase getMySQLDatabase() {
        return mySQLDatabase;
    }

    @Override
    public int countBooksBorrow() {
        String query = "SELECT COUNT(bb.borrow_book_id) FROM borrow_books bb JOIN borrow br ON bb.borrow_id = br.borrow_id WHERE br.tinh_trang = 'BORROWING';";
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

    @Override
    public boolean insertBorrowRecord(int userId, List<Integer> bookIds, Date borrowDate, Date returnDate) {
        String borrowQuery = "INSERT INTO borrow (user_id, ngay_muon, ngay_tra, tinh_trang) VALUES (?, ?, ?, ?)";
        String borrowBooksQuery = "INSERT INTO borrow_books (borrow_id, book_id) VALUES (?, ?)";
        String checkQuantityQuery = "SELECT so_luong FROM books WHERE id_sach = ?";
        String updateQuantityQuery = "UPDATE books SET so_luong = so_luong - 1 WHERE id_sach = ?";
        Connection connection = mySQLDatabase.connect();
        try {
            
            connection.setAutoCommit(false);

            
            PreparedStatement borrowStatement = connection.prepareStatement(borrowQuery, PreparedStatement.RETURN_GENERATED_KEYS);
            borrowStatement.setInt(1, userId);
            borrowStatement.setDate(2, borrowDate);
            borrowStatement.setDate(3, returnDate);
            borrowStatement.setString(4, "BORROWING");

            int rowsAffected = borrowStatement.executeUpdate();
            if (rowsAffected == 0) {
                connection.rollback();
                return false; 
            }

            
            int borrowId = -1;
            try (var generatedKeys = borrowStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    borrowId = generatedKeys.getInt(1);
                }
            }

            
            try (PreparedStatement checkStmt = connection.prepareStatement(checkQuantityQuery)) {
                for (int bookId : bookIds) {
                    checkStmt.setInt(1, bookId);
                    ResultSet rs = checkStmt.executeQuery();

                    if (rs.next()) {
                        int quantity = rs.getInt("so_luong");
                        if (quantity > 0) {
                            
                            try (PreparedStatement updateStmt = connection.prepareStatement(updateQuantityQuery)) {
                                updateStmt.setInt(1, bookId);
                                updateStmt.executeUpdate();
                            }
                        } else {
                            
                            connection.rollback();
                            return false; 
                        }
                    }
                }
            }

            
            try (PreparedStatement borrowBooksStatement = connection.prepareStatement(borrowBooksQuery)) {
                for (int bookId : bookIds) {
                    borrowBooksStatement.setInt(1, borrowId);
                    borrowBooksStatement.setInt(2, bookId);
                    borrowBooksStatement.addBatch(); 
                }
                borrowBooksStatement.executeBatch(); 
            }

            
            connection.commit();
            return true;

        } catch (SQLException e) {
            try {
                connection.rollback(); 
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                connection.setAutoCommit(true); 
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<Borrowed> getAllBorrowData() throws SQLException {
        List<Borrowed> borrowList = new ArrayList<>();
        String query = "SELECT borrow.borrow_id, borrow.user_id, customers.user_name, borrow.ngay_muon, borrow.ngay_tra, borrow.tinh_trang, " +
                "books.id_sach, books.ten_sach, books.tac_gia, books.nha_xuatban, books.ngay_xuatban, books.the_loai, " +
                "books.isbn, books.image, books.description " +
                "FROM borrow " +
                "JOIN borrow_books ON borrow.borrow_id = borrow_books.borrow_id " +
                "JOIN books ON borrow_books.book_id = books.id_sach " +
                "JOIN customers ON borrow.user_id = customers.user_id " +
                "ORDER BY borrow.borrow_id DESC";

        try (PreparedStatement statement = mySQLDatabase.connect().prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            Borrowed currentBorrow = null;
            int currentBorrowId = -1;

            while (resultSet.next()) {
                int borrowId = resultSet.getInt("borrow_id");
                int userId = resultSet.getInt("user_id");
                String userName = resultSet.getString("user_name");
                String borrowDate = resultSet.getString("ngay_muon");
                String returnDate = resultSet.getString("ngay_tra");
                String status = resultSet.getString("tinh_trang");

                
                int bookId = resultSet.getInt("id_sach");
                String title = resultSet.getString("ten_sach");
                String author = resultSet.getString("tac_gia");
                String publisher = resultSet.getString("nha_xuatban");
                String publishedDate = resultSet.getString("ngay_xuatban");
                String genre = resultSet.getString("the_loai");
                String isbn = resultSet.getString("isbn");
                byte[] image = resultSet.getBytes("image");
                String description = resultSet.getString("description");

                Book book = new Book(bookId, title, author, publisher, publishedDate, genre, 1, isbn, image, description);

                
                if (currentBorrowId != borrowId) {
                    currentBorrow = new Borrowed(borrowId, userId, userName, new ArrayList<>(), borrowDate, returnDate, status);
                    borrowList.add(currentBorrow);
                    currentBorrowId = borrowId;
                }

                
                if (currentBorrow != null) {
                    currentBorrow.getBooks().add(book);
                }
            }
        }
        return borrowList;
    }

    @Override
    public void updateOverdueStatus() throws SQLException {
        String query = "UPDATE borrow " +
                "SET tinh_trang = 'OVERDUE' " +
                "WHERE tinh_trang = 'BORROWING' AND ngay_tra < CURDATE()";
        try (PreparedStatement statement = mySQLDatabase.connect().prepareStatement(query)) {
            statement.executeUpdate();
        }
    }

    @Override
    public boolean returnBook(int borrowId, List<Book> books) {
        String updateBorrowStatusQuery = "UPDATE borrow SET tinh_trang = 'RETURNED' WHERE borrow_id = ?";
        String updateBookQuantityQuery = "UPDATE books SET so_luong = so_luong + 1 WHERE id_sach = ?";
        Connection connection = mySQLDatabase.connect();

        try {
            
            connection.setAutoCommit(false);

            
            try (PreparedStatement updateBorrowStmt = connection.prepareStatement(updateBorrowStatusQuery)) {
                updateBorrowStmt.setInt(1, borrowId);
                int rowsUpdated = updateBorrowStmt.executeUpdate();

                if (rowsUpdated == 0) {
                    connection.rollback();
                    return false; 
                }
            }

            
            try (PreparedStatement updateBookStmt = connection.prepareStatement(updateBookQuantityQuery)) {
                for (Book book : books) {
                    updateBookStmt.setInt(1, book.getId()); 
                    updateBookStmt.addBatch(); 
                }
                updateBookStmt.executeBatch(); 
            }

            
            connection.commit();
            return true;

        } catch (SQLException e) {
            try {
                connection.rollback(); 
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                connection.setAutoCommit(true); 
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public ObservableList<Borrowed> searchBorrowRecords(String borrowId, String userName, String loanDate, String status) throws SQLException {
        ObservableList<Borrowed> borrowList = FXCollections.observableArrayList();

        String sql = "SELECT borrow.borrow_id, borrow.user_id, customers.user_name, borrow.ngay_muon, borrow.ngay_tra, borrow.tinh_trang " +
                "FROM borrow " +
                "JOIN customers ON borrow.user_id = customers.user_id " +
                "WHERE 1=1"; 

        
        if (!borrowId.isEmpty()) {
            sql += " AND borrow.user_id LIKE ?";
        }
        if (!userName.isEmpty()) {
            sql += " AND customers.user_name LIKE ?";
        }
        if (!loanDate.isEmpty()) {
            sql += " AND borrow.ngay_muon LIKE ?";
        }
        if (!status.isEmpty()) {
            sql += " AND borrow.tinh_trang LIKE ?";
        }

        try (Connection conn = mySQLDatabase.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            
            int paramIndex = 1;
            if (!borrowId.isEmpty()) {
                stmt.setString(paramIndex++, "%" + borrowId + "%");
            }
            if (!userName.isEmpty()) {
                stmt.setString(paramIndex++, "%" + userName + "%");
            }
            if (!loanDate.isEmpty()) {
                stmt.setString(paramIndex++, "%" + loanDate + "%");
            }
            if (!status.isEmpty()) {
                stmt.setString(paramIndex++, "%" + status + "%");
            }

            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Borrowed borrow = new Borrowed(
                            rs.getInt("borrow_id"),
                            rs.getInt("user_id"),
                            rs.getString("user_name"),
                            new ArrayList<>(),
                            rs.getString("ngay_muon"),
                            rs.getString("ngay_tra"),
                            rs.getString("tinh_trang")
                    );
                    borrowList.add(borrow);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error searching borrow records: " + e.getMessage());
            throw e;
        }

        return borrowList;
    }
}