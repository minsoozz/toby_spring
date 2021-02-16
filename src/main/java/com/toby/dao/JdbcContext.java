package com.toby.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;

public class JdbcContext {

  private DataSource dataSource;

  public JdbcContext(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public void executeSql(final String query) throws SQLException {
    workWithStatementStrategy(c -> c.prepareStatement(query));

  }


  public void workWithStatementStrategy(StatementStrategy strategy) throws SQLException {
    try (Connection c = dataSource.getConnection();
        PreparedStatement ps = strategy.makePreparedStatement(c)) {
      ps.executeUpdate();
    } catch (SQLException e) {
      throw e;
    }
  }
}