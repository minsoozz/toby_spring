package com.toby.dao;

import com.toby.domain.Level;
import com.toby.exception.DuplicateUserIdException;
import com.toby.domain.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

  private RowMapper<User> userMapper =
      new RowMapper<User>() {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
          User user = new User();
          user.setId(rs.getString("id"));
          user.setName(rs.getString("name"));
          user.setPassword(rs.getString("password"));
          user.setLevel(Level.valueOf(rs.getInt("level")));
          user.setLogin(rs.getInt("login"));
          user.setRecommend(rs.getInt("recommend"));
          return user;
        }
      };

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

  }

  @Override
  public int getCount() {
    return 0;
  }

  @Override
  public List<User> getAll() {
    return null;
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
