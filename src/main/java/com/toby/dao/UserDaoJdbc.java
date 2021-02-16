package toby.dao;

import com.toby.dao.UserDao;
import com.toby.exception.DuplicateUserIdException;
import com.toby.model.User;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

public class UserDaoJdbc implements UserDao {


  private JdbcTemplate jdbcTemplate;

  public UserDaoJdbc(DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
  }

  public void add(final User user) {
    this.jdbcTemplate.update("insert into users(id, name, password) values(?, ?, ?) "
        , user.getId(), user.getName(), user.getPassword());
  }

  @Override
  public void addWithDuplicateUserIdException(User user) throws DuplicateUserIdException {

  }

  @Override
  public User get(String id) {
    return null;
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

  }

  @Transactional
  public void addAll(List<User> userList) {
    userList.forEach(user -> add(user));
  }

}
