package com.toby.dao;

import static org.assertj.core.api.Assertions.assertThat;

import com.toby.model.User;
import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class UserDaoJdbcTest {

  @Autowired
  private UserDao userDao;

  @Autowired
  private DataSource dataSource;

  @Test
  public void add(){
    User user = new User("1","1","1");
    userDao.add(user);
  assertThat(user.getId()).isEqualTo("1");
  }

}