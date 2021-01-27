package com.toby.dao;

import com.toby.model.User;
import java.sql.*;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

public class UserDao {

  private JdbcTemplate jdbcTemplate;

  public void setDataSource(DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
    this.dataSource = dataSource;
  }

  private DataSource dataSource;

  public UserDao(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public void add(User user) throws SQLException, ClassNotFoundException {
//        Class.forName("com.mysql.cj.jdbc.Driver");
    Connection c = dataSource.getConnection();
    PreparedStatement ps = c
        .prepareStatement("insert into users(id, password, name) values(?, ?, ?)");
    ps.setString(1, user.getId());
    ps.setString(2, user.getPassword());
    ps.setString(3, user.getName());
    ps.executeUpdate();
    ps.close();
    c.close();
  }

  public User get(String id) throws SQLException, ClassNotFoundException {
    Connection c = dataSource.getConnection();
    PreparedStatement ps = c.prepareStatement(
        "select * from users where id = ?");
    ps.setString(1, id);
    ResultSet rs = ps.executeQuery();

    User user = null;
    if (rs.next()) {
      user = new User();
      user.setId(rs.getString("id"));
      user.setName(rs.getString("name"));
      user.setPassword(rs.getString("password"));
    }
    rs.close();
    ps.close();
    c.close();

    if (user == null) {
      throw new EmptyResultDataAccessException(1);
    }

    return user;
  }

  public void deleteAll() {

    this.jdbcTemplate.update("delete from users");
  }

  public int getCount() throws SQLException, ClassNotFoundException {
    Connection c = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    try {
      c = dataSource.getConnection();
      ps = c.prepareStatement("select count(*) from users");

      ps.executeQuery();
      rs.next();
      int count = rs.getInt(1);

      return count;
    } catch (SQLException e) {
      throw e;
    } finally {
      if (rs != null) {
        try {
          rs.close();
        } catch (SQLException e) {
        }
      }
      if (ps != null) {
        try {
          ps.close();
        } catch (SQLException e) {
        }
      }
      if (c != null) {
        try {
          c.close();
        } catch (SQLException e) {
        }
      }
    }
  }

  private PreparedStatement makeStatement(Connection c) throws SQLException {
    PreparedStatement ps;
    ps = c.prepareStatement("delete from users");
    return ps;
  }

  public void jdbcContextWithStatementStrategy(StatementStrategy stmt)
      throws SQLException, ClassNotFoundException {
    Connection c = null;
    PreparedStatement ps = null;

    try {
      c = dataSource.getConnection();
      ps = stmt.makePreparedStatement(c);
    } catch (SQLException e) {
      throw e;
    } finally {
      if (ps != null) {
        try {
          ps.close();
        } catch (SQLException e) {
        }
      }
      if (c != null) {
        try {
          c.close();
        } catch (SQLException e) {
        }
      }
    }
  }
}
