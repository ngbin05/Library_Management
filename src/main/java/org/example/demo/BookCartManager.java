package org.example.demo;

import java.util.ArrayList;
import java.util.List;

public class BookCartManager {
    private static List<Book> cart = new ArrayList<>(); // Giỏ sách mượn

    // Phương thức để lấy giỏ sách chung
    public static List<Book> getCart() {
        return cart;
    }

    // Thêm sách vào giỏ nếu chưa có
    public static boolean addBookIfNotInCart(Book book) {
        if (!isBookInCart(book)) {
            cart.add(book);
            return true; // Thêm thành công
        }
        return false; // Sách đã tồn tại
    }

    // Xóa sách khỏi giỏ
    public static void removeBook(Book book) {
        cart.remove(book);
    }

    // Kiểm tra xem sách đã có trong giỏ chưa
    private static boolean isBookInCart(Book book) {
        return cart.stream().anyMatch(b -> b.getId() == book.getId());
    }
}
