package com.toby;

import com.toby.dao.H2Connection;
import com.toby.dao.UserDao;
import java.sql.Connection;
import java.sql.DriverManager;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext
public class DiritesTest {

  @Autowired
  UserDao dao;

  @BeforeEach
  public void setUp() {


    DataSource dataSource =
    new SingleConnectionDataSource("jdbc:h2:tcp://localhost/~/test", "sa", "", true);

  }
}
