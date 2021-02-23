package com.toby.dao;

import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

@Configuration
public class DaoFactory {

  @Bean
  public UserDao userDao() {
    return new UserDaoJdbc(dataSource());
  }

  @Bean
  public ConnectionMaker connectionMaker() {
    return new ConnectionMaker();
  }

  @Bean
  public DataSource dataSource() {
    SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
    // dataSource.setDriverClass(org.h2.Driver.class);
    dataSource.setDriverClass(com.mysql.cj.jdbc.Driver.class);
    //dataSource.setUrl("jdbc:h2:tcp://localhost/~/test");
    dataSource.setUrl("jdbc:mysql://localhost:3306/soltsds?serverTimezone=UTC");
    dataSource.setUsername("soltsds");
    dataSource.setPassword("soltsds");
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