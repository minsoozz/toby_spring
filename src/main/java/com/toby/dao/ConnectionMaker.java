package com.toby.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionMaker {
  public Connection getConnection() throws ClassNotFoundException, SQLException {
    //Class.forName("org.h2.Driver");
    Class.forName("com.mysql.jdbc.Driver");
      //  return DriverManager.getConnection("jdbc:h2:tcp://localhost/~/test", "sa", "");
          return DriverManager.getConnection("jdbc:mysql://localhost:3306/soltsds?serverTimezone=UTC", "soltsds", "soltsds");

  }
}
