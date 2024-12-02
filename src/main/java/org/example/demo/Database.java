package org.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class Database {
    private static final String URL = "jdbc:mysql://localhost:3306/ThuVien";
    private static final String USER = "root";

    private static final String PASSWORD = "danhhao2005";


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


    public static boolean isBookExistByIsbn(String isbn, String title) {
        if (isbn == null || isbn.equals("Unknown ISBN")) {
            return isBookExistByTitle(title);
        }

        String sql = "SELECT COUNT(*) FROM books WHERE isbn = ?";
        try (Connection conn = connect();
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

    private static boolean isBookExistByTitle(String title) {
        String sql = "SELECT COUNT(*) FROM books WHERE ten_sach = ?";
        try (Connection conn = connect();
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

    public static void insertBook(String title, String author, String nha_xuatban, String publish, String theloai, int quantity, String isbn, String description, byte[] image) {
        // Cập nhật câu lệnh SQL để bao gồm trường "image" (BLOB) và "description"
        String sql = "INSERT INTO BOOKS (ten_sach, tac_gia, nha_xuatban, ngay_xuatban, the_loai, so_luong, isbn, image, description) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, title);
            pstmt.setString(2, author);
            pstmt.setString(3, nha_xuatban);
            pstmt.setString(4, publish);
            pstmt.setString(5, theloai);
            pstmt.setInt(6, quantity);
            pstmt.setString(7, isbn);
            // Thêm mô tả sách vào cơ sở dữ liệu

            // Nếu có ảnh, lưu ảnh vào cơ sở dữ liệu dưới dạng BLOB
            if (image != null) {
                pstmt.setBytes(8, image);  // Lưu ảnh dưới dạng byte[]
            } else {
                pstmt.setNull(8, Types.BLOB);  // Nếu không có ảnh, lưu NULL
            }
            pstmt.setString(9, description);

            int rowsAffected = pstmt.executeUpdate();
            System.out.println(rowsAffected + " row(s) inserted.");
        } catch (SQLException e) {
            System.out.println("Error inserting document: " + e.getMessage());
        }
    }




    public static List<Book> getBooks() throws SQLException {
        List<Book> books = new ArrayList<>();

        String query = "SELECT * FROM books";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
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

                // Đọc mô tả từ cơ sở dữ liệu
                String description = resultSet.getString("description");

                // Đọc ảnh từ cơ sở dữ liệu
                byte[] image = resultSet.getBytes("image");

                // Tạo đối tượng Book và thêm vào danh sách
                books.add(new Book(id, name, author, publisher, publishedDate, genre, quantity, isbn, image,description));
            }
        }

        return books;
    }




    public static void deleteBook(int bookId) throws SQLException {
        String sql = "DELETE FROM books WHERE id_sach = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bookId);
            stmt.executeUpdate();
        }
    }

    public static ObservableList<Book> getBooksByNameOrAuthor(String searchTitle, String searchAuthor) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT * FROM books WHERE 1=1");

        if (!searchTitle.isEmpty()) {
            sql.append(" AND ten_sach LIKE ?");
        }
        if (!searchAuthor.isEmpty()) {
            sql.append(" AND tac_gia LIKE ?");
        }

        ObservableList<Book> bookList = FXCollections.observableArrayList();

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

            int paramIndex = 1;

            // Gán các tham số vào PreparedStatement
            if (!searchTitle.isEmpty()) {
                pstmt.setString(paramIndex++, "%" + searchTitle + "%");  // Thêm dấu % để tìm kiếm theo phần tử
            }
            if (!searchAuthor.isEmpty()) {
                pstmt.setString(paramIndex++, "%" + searchAuthor + "%");
            }

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                // Đọc ảnh và mô tả từ cơ sở dữ liệu
                byte[] image = rs.getBytes("image");
                String description = rs.getString("description");

                // Thêm đối tượng Book vào danh sách
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
                        description// Truyền ảnh vào constructor
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching books: " + e.getMessage());
            throw e;
        }

        return bookList;
    }

    public static boolean isUserExists(String cccd) {
        String checkQuery = "SELECT COUNT(*) FROM readers WHERE cccd = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(checkQuery)) {

            preparedStatement.setString(1, cccd);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error checking user: " + e.getMessage());
        }
        return false;
    }


    public static void insertUser(String name, String phone, String address, String cccd) {
        String sql = "INSERT INTO CUSTOMERS(user_name, phone_number,address, CCCD) VALUES(?, ?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setString(2, phone);
            pstmt.setString(3, address);
            pstmt.setString(4, cccd);


            int rowsAffected = pstmt.executeUpdate();
            System.out.println(rowsAffected + " user(s) inserted.");

        } catch (SQLException e) {
            System.out.println("Error inserting user: " + e.getMessage());
        }
    }

    public static List<Customer> getCustomers() throws SQLException {
        List<Customer> customers = new ArrayList<>();

        String query = "SELECT * FROM customers";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("user_id");
                String name = resultSet.getString("user_name");
                String phone = resultSet.getString("phone_number");
                String address = resultSet.getString("address");
                String cccd = resultSet.getString("CCCD");

                customers.add(new Customer(id, name, phone,address ,cccd));
            }
        }

        return customers;
    }


    public static ObservableList<Customer> getUsersByMultipleSearchTerms(String searchID, String searchName, String searchPhone, String searchCCCD) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT * FROM Customers WHERE 1=1");

        if (!searchID.isEmpty()) {
            sql.append(" AND user_id LIKE ?");
        }
        if (!searchName.isEmpty()) {
            sql.append(" AND user_name LIKE ?");
        }
        if (!searchPhone.isEmpty()) {
            sql.append(" AND phone_number LIKE ?");
        }
        if (!searchCCCD.isEmpty()) {
            sql.append(" AND CCCD LIKE ?");
        }

        ObservableList<Customer> customerList = FXCollections.observableArrayList();

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

            int paramIndex = 1;

            // Gán các tham số vào PreparedStatement
            if (!searchID.isEmpty()) {
                pstmt.setString(paramIndex++, "%" + searchID + "%");  // Thêm dấu % để tìm kiếm theo phần tử
            }
            if (!searchName.isEmpty()) {
                pstmt.setString(paramIndex++, "%" + searchName + "%");
            }
            if (!searchPhone.isEmpty()) {
                pstmt.setString(paramIndex++, "%" + searchPhone + "%");
            }
            if (!searchCCCD.isEmpty()) {
                pstmt.setString(paramIndex++, "%" + searchCCCD + "%");
            }

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                customerList.add(new Customer(
                        rs.getInt("user_id"),
                        rs.getString("user_name"),
                        rs.getString("phone_number"),
                        rs.getString("address"),
                        rs.getString("CCCD")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching users: " + e.getMessage());
            throw e;
        }

        return customerList;
    }

    public static void deleteCustomer(int customerId) throws SQLException {
        String sql = "DELETE FROM customers WHERE user_id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, customerId);
            stmt.executeUpdate();
        }
    }






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




    public static boolean changePassword(String username, String oldPassword, String newPassword) {
        String querySelect = "SELECT password FROM accounts WHERE username = ?";
        String queryUpdate = "UPDATE accounts SET password = ? WHERE username = ?";

        try (Connection conn = Database.connect();
             PreparedStatement selectStmt = conn.prepareStatement(querySelect)) {

            // Kiểm tra mật khẩu cũ
            selectStmt.setString(1, username);
            ResultSet rs = selectStmt.executeQuery();
            if (rs.next()) {
                String currentPassword = rs.getString("password");

                if (!currentPassword.equals(oldPassword)) {
                    System.out.println("Mật khẩu cũ không đúng!");
                    return false;
                }
            } else {
                System.out.println("Không tìm thấy tài khoản với username này!");
                return false;
            }

            // Cập nhật mật khẩu mới
            try (PreparedStatement updateStmt = conn.prepareStatement(queryUpdate)) {
                updateStmt.setString(1, newPassword);
                updateStmt.setString(2, username);

                int rowsUpdated = updateStmt.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("Đổi mật khẩu thành công!");
                    return true;
                } else {
                    System.out.println("Đổi mật khẩu thất bại!");
                    return false;
                }
            }

        } catch (SQLException e) {
            System.out.println("Lỗi khi thay đổi mật khẩu: " + e.getMessage());
            return false;
        }
    }

    public static String getFullName(String userName) {
        String fullName = null;
        String query = "SELECT full_name FROM accounts WHERE username = ?";

        try (PreparedStatement statement = connect().prepareStatement(query)) {
            statement.setString(1, userName); // Set the user_name parameter
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    fullName = resultSet.getString("full_name");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fullName;
    }

    public static String getPhoneNumber(String userName) {
        String phoneNumber = null;
        String query = "SELECT phone_number FROM accounts WHERE username = ?";

        try (PreparedStatement statement = connect().prepareStatement(query)) {
            statement.setString(1, userName); // Set the user_name parameter
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    phoneNumber = resultSet.getString("phone_number");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return phoneNumber;
    }

    public static String getEmail(String userName) {
        String email = null;
        String query = "SELECT email FROM accounts WHERE username = ?";

        try (PreparedStatement statement = connect().prepareStatement(query)) {
            statement.setString(1, userName); // Set the user_name parameter
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    email = resultSet.getString("email");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return email;
    }


//    public static String getAddress() {
//        String address = null;
//        String query = "SELECT address FROM accounts LIMIT 1";
//
//        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
//             Statement statement = connection.createStatement();
//             ResultSet resultSet = statement.executeQuery(query)) {
//
//            if (resultSet.next()) {
//                address = resultSet.getString("address");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return address;
//    }

    public static boolean updateUserInformation(String full_name, String phoneNumber, String email, String user_name) {
        String sql = "UPDATE accounts SET full_name = ?, phone_number = ?, email = ? WHERE username = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, full_name);
            pstmt.setString(2, phoneNumber);
            pstmt.setString(3, email);
//            pstmt.setString(4, address);
            pstmt.setString(4, user_name);

            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0; // Trả về true nếu cập nhật thành công
        } catch (SQLException e) {
            System.out.println("Error updating user information: " + e.getMessage());
            return false;
        }
    }
    public static Account getAccountByUsername(String username) {
        String query = "SELECT username, password, avatar FROM accounts WHERE username = ?";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Gán tham số cho câu truy vấn
            preparedStatement.setString(1, username);

            // Thực hiện truy vấn
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String userName = resultSet.getString("username");
                String password = resultSet.getString("password");
                byte[] avatar = resultSet.getBytes("avatar"); // Lấy dữ liệu ảnh dạng BLOB

                return new Account(userName, password, avatar);
            } else {
                System.out.println("Không tìm thấy tài khoản với username: " + username);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean insertBorrowRecord(int userId, List<Integer> bookIds, Date borrowDate, Date returnDate) {
        String borrowQuery = "INSERT INTO borrow (user_id, ngay_muon, ngay_tra, tinh_trang) VALUES (?, ?, ?, ?)";
        String borrowBooksQuery = "INSERT INTO borrow_books (borrow_id, book_id) VALUES (?, ?)";
        String checkQuantityQuery = "SELECT so_luong FROM books WHERE id_sach = ?";
        String updateQuantityQuery = "UPDATE books SET so_luong = so_luong - 1 WHERE id_sach = ?";
        Connection connection = connect();
        try {
            // Bắt đầu giao dịch để đảm bảo tính nhất quán
            connection.setAutoCommit(false);

            // Chèn bản ghi vào bảng borrow
            PreparedStatement borrowStatement = connection.prepareStatement(borrowQuery, PreparedStatement.RETURN_GENERATED_KEYS);
            borrowStatement.setInt(1, userId);
            borrowStatement.setDate(2, borrowDate);
            borrowStatement.setDate(3, returnDate);
            borrowStatement.setString(4, "BORROWING");

            int rowsAffected = borrowStatement.executeUpdate();
            if (rowsAffected == 0) {
                connection.rollback();
                return false; // Không thể chèn lượt mượn
            }

            // Lấy borrow_id vừa chèn vào
            int borrowId = -1;
            try (var generatedKeys = borrowStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    borrowId = generatedKeys.getInt(1);
                }
            }

            // Kiểm tra và cập nhật số lượng sách trong bảng books
            try (PreparedStatement checkStmt =connection.prepareStatement(checkQuantityQuery)) {
                for (int bookId : bookIds) {
                    checkStmt.setInt(1, bookId);
                    ResultSet rs = checkStmt.executeQuery();

                    if (rs.next()) {
                        int quantity = rs.getInt("so_luong");
                        if (quantity > 0) {
                            // Giảm số lượng sách đi 1
                            try (PreparedStatement updateStmt = connection.prepareStatement(updateQuantityQuery)) {
                                updateStmt.setInt(1, bookId);
                                updateStmt.executeUpdate();
                            }
                        } else {
                            // Nếu không còn sách để mượn, rollback giao dịch
                            connection.rollback();
                            return false; // Trả về false nếu không có sách để mượn
                        }
                    }
                }
            }

            // Chèn các cuốn sách mượn vào bảng borrow_books
            try (PreparedStatement borrowBooksStatement = connection.prepareStatement(borrowBooksQuery)) {
                for (int bookId : bookIds) {
                    borrowBooksStatement.setInt(1, borrowId);
                    borrowBooksStatement.setInt(2, bookId);
                    borrowBooksStatement.addBatch(); // Thêm vào batch
                }
                borrowBooksStatement.executeBatch(); // Thực thi batch
            }

            // Cam kết giao dịch
            connection.commit();
            return true;

        } catch (SQLException e) {
            try {
                connection.rollback(); // Quay lại nếu có lỗi
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                connection.setAutoCommit(true); // Đặt lại chế độ auto-commit
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static List<Borrowed> getAllBorrowData() throws SQLException {
        List<Borrowed> borrowList = new ArrayList<>();
        String query = "SELECT borrow.borrow_id, borrow.user_id, customers.user_name, borrow.ngay_muon, borrow.ngay_tra, borrow.tinh_trang, " +
                "GROUP_CONCAT(books.ten_sach SEPARATOR '\n') AS book_titles " +
                "FROM borrow " +
                "JOIN borrow_books ON borrow.borrow_id = borrow_books.borrow_id " +
                "JOIN books ON borrow_books.book_id = books.id_sach " +
                "JOIN customers ON borrow.user_id = customers.user_id " +
                "GROUP BY borrow.borrow_id";

        try (PreparedStatement statement = connect().prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int borrowId = resultSet.getInt("borrow_id");
                int userId = resultSet.getInt("user_id");
                String userName = resultSet.getString("user_name"); // Get the user_name from the result set
                String borrowDate = resultSet.getString("ngay_muon");
                String returnDate = resultSet.getString("ngay_tra");
                String status = resultSet.getString("tinh_trang");
                String bookTitlesString = resultSet.getString("book_titles");

                // Check and split the book_titles string into a list
                List<String> bookTitles = bookTitlesString != null && !bookTitlesString.isEmpty()
                        ? Arrays.asList(bookTitlesString.split("\n"))
                        : new ArrayList<>(); // Return empty list if book_titles is empty

                // Create a Borrowed object with the new user_name field
                Borrowed borrow = new Borrowed(borrowId, userId, userName, bookTitles, borrowDate, returnDate, status);

                // Add the object to the result list
                borrowList.add(borrow);
            }
        }
        return borrowList;
    }

    public static void updateOverdueStatus() throws SQLException {
        String query = "UPDATE borrow " +
                "SET tinh_trang = 'OVERDUE' " +
                "WHERE tinh_trang = 'BORROWING' AND ngay_tra < CURDATE()";
        try (PreparedStatement statement = connect().prepareStatement(query)) {
            statement.executeUpdate();
        }
    }


    public static boolean checkEmailExists(String email) {
        // Câu truy vấn kiểm tra sự tồn tại của username
        String sql = "SELECT 1 FROM accounts WHERE email = ?";

        // Kiểm tra nếu email là null hoặc rỗng
        if (email == null || email.trim().isEmpty()) {
            return false; // Tài khoản không hợp lệ
        }

        try (Connection conn = connect(); // Kết nối tới cơ sở dữ liệu
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Gán giá trị email vào câu truy vấn
            pstmt.setString(1, email.trim());

            // Thực thi truy vấn
            ResultSet rs = pstmt.executeQuery();

            // Nếu có dòng dữ liệu trả về, tài khoản tồn tại
            return rs.next();
        } catch (SQLException e) {
            System.out.println("Error checking email: " + e.getMessage());
            return false; // Nếu có lỗi xảy ra, mặc định trả về false
        }
    }

    public static boolean updatenewPass(String email, String newPassword) {
        // Câu lệnh SQL để cập nhật mật khẩu
        String queryUpdate = "UPDATE accounts SET password = ? WHERE email = ?";

        try (Connection conn = Database.connect(); // Kết nối đến database
             PreparedStatement updateStmt = conn.prepareStatement(queryUpdate)) {

            // Gán tham số vào câu lệnh SQL
            updateStmt.setString(1, newPassword);
            updateStmt.setString(2, email);

            // Thực thi câu lệnh
            int rowsUpdated = updateStmt.executeUpdate();
            // Kiểm tra xem có dòng nào được cập nhật hay không
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi khi thay đổi mật khẩu: " + e.getMessage());
            return false;
        }
    }

    public static int countUsers() {
        String query = "SELECT COUNT(user_id) FROM customers";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) { // Nếu có kết quả
                return rs.getInt(1); // Lấy giá trị COUNT
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0; // Trả về 0 nếu có lỗi
    }

    public static int countBooks() {
        String query = "SELECT COUNT(id_sach) FROM books";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) { // Nếu có kết quả
                return rs.getInt(1); // Lấy giá trị COUNT
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0; // Trả về 0 nếu có lỗi
    }

    public static int countBooksBorrow() {
        String query = "SELECT COUNT(Borrow_book_id) FROM borrow_books";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) { // Nếu có kết quả
                return rs.getInt(1); // Lấy giá trị COUNT
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0; // Trả về 0 nếu có lỗi
    }
}
