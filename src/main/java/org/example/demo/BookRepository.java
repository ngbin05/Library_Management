package org.example.demo;

import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.List;

public interface BookRepository {
    public boolean isBookExistByIsbn(String isbn, String title);
    public boolean isBookExistByTitle(String title);
    public void insertBook(String title, String author, String nha_xuatban, String publish, String theloai, int quantity, String isbn, String description, byte[] image);
    public List<Book> getBooks() throws SQLException;
    public void deleteBook(int bookId) throws SQLException;
    public ObservableList<Book> getBooksByNameOrAuthor(String searchTitle, String searchAuthor) throws SQLException;
    public  int countBooks();
}
