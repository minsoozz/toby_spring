package com.toby.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;

@Configuration
public class DaoFactory {

  @Bean
  public UserDao userDao() {
    return new toby.dao.UserDaoJdbc(dataSource());
  }

  @Bean
  public ConnectionMaker connectionMaker() {
    return new ConnectionMaker();
  }

  @Bean
  public DataSource dataSource() {
    SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
    dataSource.setDriverClass(org.h2.Driver.class);
    dataSource.setUrl("jdbc:h2:tcp://localhost/~/test");
    dataSource.setUsername("sa");
    dataSource.setPassword("");
    return dataSource;
  }

  @Bean
  public DataSourceTransactionManager transactionManager() {
    return new DataSourceTransactionManager(dataSource());
  }

//  @Bean
//  public JdbcContext jdbcContext() {
//    return new JdbcContext(dataSource());
//  }

}