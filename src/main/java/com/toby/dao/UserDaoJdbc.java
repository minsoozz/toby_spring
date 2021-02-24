package com.toby.dao;

import com.toby.domain.Level;
import com.toby.exception.DuplicateUserIdException;
import com.toby.domain.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Transactional;

public class UserDaoJdbc implements UserDao {


  private JdbcTemplate jdbcTemplate;

  @Autowired
  DataSource dataSource;

  public UserDaoJdbc(DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
  }

  private RowMapper<User> userMapper() {
    return (resultSet, i) -> {
      User user = new User();
      user.setId(resultSet.getString("id"));
      user.setName(resultSet.getString("name"));
      user.setPassword(resultSet.getString("password"));
      user.setLevel(Level.valueOf(resultSet.getInt("level")));
      user.setLogin(resultSet.getInt("login"));
      user.setRecommend(resultSet.getInt("recommend"));
      return user;
    };
  }

  public void add(final User user) {
    this.jdbcTemplate.update(
        "insert into users(id, name, password, level, login, recommend) values(?, ?, ?, ?, ?, ?) "
        , user.getId(), user.getName(), user.getPassword(), user.getLevel().intValue(),
        user.getLogin(), user.getRecommend());
  }

  @Override
  public void addWithDuplicateUserIdException(User user) throws DuplicateUserIdException {

  }

  @Override
  public User get(String id) {
    User user = new User();
    try (Connection c = dataSource.getConnection();
        PreparedStatement ps = c.prepareStatement("select * from users where id = ?");) {
      ps.setString(1, id);
      ResultSet rs = ps.executeQuery();
      rs.next();
      user.setId(rs.getString("id"));
      user.setName(rs.getString("name"));
      user.setPassword(rs.getString("password"));
      user.setLevel(Level.valueOf(rs.getInt("level")));
      user.setLogin(rs.getInt("login"));
      user.setRecommend(rs.getInt("recommend"));
    } catch (Exception e) {
    }
    return user;
  }

  @Override
  public User getUserByName(String name) {
    return null;
  }

  @Override
  public void deleteAll() {
    this.jdbcTemplate.update("delete from users");
  }

  @Override
  public int getCount() {
    return 0;
  }

  @Override
  public List<User> getAll() {
    return jdbcTemplate.query("select * from users order by id", userMapper());
  }

  @Override
  public void update(User user) {
    this.jdbcTemplate.update("update users set name = ?, password = ?, level = ?, login = ?, "
            + "recommend = ? where id = ? ", user.getName(), user.getPassword(),
        user.getLevel().intValue(), user.getLogin(), user.getRecommend(), user.getId());

  }

  @Transactional
  public void addAll(List<User> userList) {
    userList.forEach(user -> add(user));
  }
}
