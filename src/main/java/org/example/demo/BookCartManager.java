package org.example.demo;

import java.util.ArrayList;
import java.util.List;

public class BookCartManager {
    private static final List<Book> cart = new ArrayList<>(); 

    
    public static List<Book> getCart() {
        return cart;
    }

    
    public static boolean addBookIfNotInCart(Book book) {
        if (!isBookInCart(book)) {
            cart.add(book);
            return true; 
        }
        return false; 
    }

    
    public static void removeBook(Book book) {
        cart.remove(book);
    }

    
    private static boolean isBookInCart(Book book) {
        return cart.stream().anyMatch(b -> b.getId() == book.getId());
    }
}
