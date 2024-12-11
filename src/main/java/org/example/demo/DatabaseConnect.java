package org.example.demo;

import java.sql.Connection;
import java.sql.SQLException;

public interface DatabaseConnect {
    Connection connect();
}