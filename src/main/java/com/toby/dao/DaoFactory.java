package com.toby.dao;

import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

@Configuration
public class DaoFactory {

  @Bean
  public UserDao userDao() {
    return new UserDao(dataSource());
  }

  @Bean
  public DataSource dataSource() {
    return new SingleConnectionDataSource("jdbc:h2:tcp://localhost/~/test", "sa", "", true);
  }
}
