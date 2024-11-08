package org.example.demo;
import java.sql.*;
import java.sql.Date;
import java.util.*;


public class Database {
    private static final String URL = "jdbc:mysql://localhost:3306/ThuVien";
    private static final String USER = "root";
    private static final String PASSWORD = "Binh2352005@";

    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database connection established.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
//    public static void insertBook(String title, String author, String nha_xuatban, Date publish, String theloai, int quantity, String isbn) {
//        String sql = "INSERT INTO BOOK (ten_sach, tac_gia, nha_xuatban, ngay_xuatban, the_loai, so_luong, isbn) VALUES (?, ?, ?, ?, ?, ?, ?) ";
//        try (Connection conn = connect();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//
//            // Thiết lập các tham số cho PreparedStatement
//            pstmt.setString(1, title);
//            pstmt.setString(2, author);
//            pstmt.setString(3, nha_xuatban);
//            pstmt.setDate(4, publish);
//            pstmt.setString(5, theloai);
//            pstmt.setInt(6, quantity);
//            pstmt.setString(7, isbn);
//
//            int rowsAffected = pstmt.executeUpdate();
//            System.out.println(rowsAffected + " row(s) inserted.");
//
//        } catch (SQLException e) {
//            System.out.println("Error inserting document: " + e.getMessage());
//        }
//    }
//    public static void insertUser(String id, String name, String phone, String address) {
//        String sql = "INSERT INTO USERS(user_id, user_name, phone_number,address) VALUES(?, ?, ?, ?)";
//        try (Connection conn = connect();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//
//            pstmt.setString(1, id);
//            pstmt.setString(2, name);
//            pstmt.setString(3, phone);
//            pstmt.setString(4, address);
//
//
//            int rowsAffected = pstmt.executeUpdate();
//            System.out.println(rowsAffected + " user(s) inserted.");
//
//        } catch (SQLException e) {
//            System.out.println("Error inserting user: " + e.getMessage());
//        }
//    }
//
//    public static Customer getUserById(int userId) {
//        String sql = "SELECT * FROM Users WHERE id = ?";
//        Customer user = null;
//
//        try (Connection conn = connect();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//
//            pstmt.setInt(1, userId);
//            ResultSet rs = pstmt.executeQuery();
//
//            if (rs.next()) {
//                user = new Customer(rs.getInt("id"), rs.getString("name"), rs.getString("email"),
//                        rs.getString("phone"), rs.getString("address"));
//            }
//
//        } catch (SQLException e) {
//            System.out.println("Error fetching user: " + e.getMessage());
//        }
//
//        return user;
//    }
//
//    public static List<BorrowedBook> getBorrowedBooksByUserId(int userId) {
//        String sql = "SELECT b.id, d.title, d.author, b.borrow_date, b.return_date " +
//                "FROM BorrowedBooks b " +
//                "JOIN Documents d ON b.document_id = d.id " +
//                "WHERE b.user_id = ?";
//        List<BorrowedBook> borrowedBooks = new ArrayList<>();
//
//        try (Connection conn = connect();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//
//            pstmt.setInt(1, userId);
//            ResultSet rs = pstmt.executeQuery();
//
//            while (rs.next()) {
//                BorrowedBook borrowedBook = new BorrowedBook(
//                        rs.getInt("id"),
//                        rs.getString("title"),
//                        rs.getString("author"),
//                        rs.getDate("borrow_date"),
//                        rs.getDate("return_date")
//                );
//                borrowedBooks.add(borrowedBook);
//            }
//
//        } catch (SQLException e) {
//            System.out.println("Error fetching borrowed books: " + e.getMessage());
//        }
//
//        return borrowedBooks;
//    }
//
//    public static void returnBook(int borrowedBookId) {
//        String sql = "UPDATE BorrowedBooks SET return_date = ? WHERE id = ?";
//
//        try (Connection conn = connect();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//
//            pstmt.setDate(1, new java.sql.Date(System.currentTimeMillis())); // Ngày hiện tại
//            pstmt.setInt(2, borrowedBookId);
//
//            int rowsAffected = pstmt.executeUpdate();
//            System.out.println(rowsAffected + " book(s) returned.");
//
//        } catch (SQLException e) {
//            System.out.println("Error returning book: " + e.getMessage());
//        }
//    }

    public static boolean validAccount(String username, String password) {
        String query = "SELECT * FROM Accounts WHERE username = ? AND password = ?";
        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Account account = new Account(username, password);
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean isUsernameTaken(String username) {
        String query = "SELECT * FROM Accounts WHERE username = ?";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void registerAccount(String fullname,
                                          String email,
                                          String phone,
                                          String username,
                                          String password
    ) {
        String query = "INSERT INTO Accounts (username, full_name, email, phone_number, password) VALUES (?, ?, ?, ?, ?)";


        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, fullname);
            stmt.setString(3, email);
            stmt.setString(4, phone);
            stmt.setString(5, password);
            //hực hiện câu lệnh insert
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

