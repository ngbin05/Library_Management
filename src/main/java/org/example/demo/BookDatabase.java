package org.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;



public class BookDatabase implements BookRepository{
    private static MySQLDatabase mysqlDatabase = new MySQLDatabase();

    public void setMysqlDatabase(MySQLDatabase mysqlDatabase) {
        this.mysqlDatabase = mysqlDatabase;
    }
    public MySQLDatabase getMysqlDatabase() {
        return mysqlDatabase;
    }

    @Override
    public boolean isBookExistByIsbn(String isbn, String title) {
        if (isbn == null || isbn.equals("Unknown ISBN")) {
            return isBookExistByTitle(title);
        }

        String sql = "SELECT COUNT(*) FROM books WHERE isbn = ?";
        try (Connection conn = mysqlDatabase.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, isbn);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error checking ISBN: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean isBookExistByTitle(String title) {
        String sql = "SELECT COUNT(*) FROM books WHERE ten_sach = ?";
        try (Connection conn = mysqlDatabase.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error checking book title: " + e.getMessage());
        }
        return false;
    }

    @Override
    public void insertBook(String title, String author, String nha_xuatban, String publish, String theloai, int quantity, String isbn, String description, byte[] image) {
        
        String sql = "INSERT INTO BOOKS (ten_sach, tac_gia, nha_xuatban, ngay_xuatban, the_loai, so_luong, isbn, image, description) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = mysqlDatabase.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, title);
            pstmt.setString(2, author);
            pstmt.setString(3, nha_xuatban);
            pstmt.setString(4, publish);
            pstmt.setString(5, theloai);
            pstmt.setInt(6, quantity);
            pstmt.setString(7, isbn);
            

            
            if (image != null) {
                pstmt.setBytes(8, image);  
            } else {
                pstmt.setNull(8, Types.BLOB);  
            }
            pstmt.setString(9, description);

            int rowsAffected = pstmt.executeUpdate();
            System.out.println(rowsAffected + " row(s) inserted.");
        } catch (SQLException e) {
            System.out.println("Error inserting document: " + e.getMessage());
        }
    }

    @Override
    public List<Book> getBooks() throws SQLException {
        List<Book> books = new ArrayList<>();

        String query = "SELECT * FROM books";

        try (Connection connection = mysqlDatabase.connect();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id_sach");
                String name = resultSet.getString("ten_sach");
                String author = resultSet.getString("tac_gia");
                String publisher = resultSet.getString("nha_xuatban");
                String publishedDate = resultSet.getString("ngay_xuatban");
                String genre = resultSet.getString("the_loai");
                int quantity = resultSet.getInt("so_luong");
                String isbn = resultSet.getString("isbn");

                
                String description = resultSet.getString("description");

                
                byte[] image = resultSet.getBytes("image");

                
                books.add(new Book(id, name, author, publisher, publishedDate, genre, quantity, isbn, image, description));
            }
        }

        return books;
    }

    @Override
    public void deleteBook(int bookId) throws SQLException {
        String sql = "DELETE FROM books WHERE id_sach = ?";
        try (Connection conn = mysqlDatabase.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bookId);
            stmt.executeUpdate();
        }
    }

    @Override
    public ObservableList<Book> getBooksByNameOrAuthor(String searchTitle, String searchAuthor) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT * FROM books WHERE 1=1");

        if (!searchTitle.isEmpty()) {
            sql.append(" AND ten_sach LIKE ?");
        }
        if (!searchAuthor.isEmpty()) {
            sql.append(" AND tac_gia LIKE ?");
        }

        ObservableList<Book> bookList = FXCollections.observableArrayList();

        try (Connection conn = mysqlDatabase.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

            int paramIndex = 1;

            
            if (!searchTitle.isEmpty()) {
                pstmt.setString(paramIndex++, "%" + searchTitle + "%");  
            }
            if (!searchAuthor.isEmpty()) {
                pstmt.setString(paramIndex++, "%" + searchAuthor + "%");
            }

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                
                byte[] image = rs.getBytes("image");
                String description = rs.getString("description");

                
                bookList.add(new Book(
                        rs.getInt("id_sach"),
                        rs.getString("ten_sach"),
                        rs.getString("tac_gia"),
                        rs.getString("nha_xuatban"),
                        rs.getString("ngay_xuatban"),
                        rs.getString("the_loai"),
                        rs.getInt("so_luong"),
                        rs.getString("isbn"),
                        image,
                        description
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching books: " + e.getMessage());
            throw e;
        }

        return bookList;
    }

    @Override
    public  int countBooks() {
        String query = "SELECT COUNT(id_sach) FROM books";
        try (Connection conn = mysqlDatabase.connect();
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
}
