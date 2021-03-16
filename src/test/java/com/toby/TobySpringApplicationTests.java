
package com.toby;


import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.toby.dao.DaoFactory;
import com.toby.dao.UserDao;
import com.toby.domain.User;
import java.sql.SQLException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
class TobySpringApplicationTests {

  @Autowired
  private ApplicationContext context;

  @Autowired
  private UserDao dao;

  private User user1;
  private User user2;
  private User user3;

  @BeforeEach
  public void setUp() throws SQLException, ClassNotFoundException {
    System.out.println("this.context = " + this.context);
    System.out.println("this = " + this);
  }

  @Test
  public void addAndGet() throws SQLException, ClassNotFoundException {
/////////////
    dao.deleteAll();
    assertThat(dao.getCount(), is(0));

    User user = new User();
    user.setId("minsoo");
    user.setName("김민수");
    user.setPassword("1234");

    dao.add(user);
    User user2 = new User();
    user2 = dao.get("minsoo");

    assertThat(user.getId(), is(user2.getId()));
    assertThat(user.getName(), is(user2.getName()));
    assertThat(user.getPassword(), is(user2.getPassword()));
  }

  @Test
  public void getUserFailures() throws SQLException, ClassNotFoundException {
    ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
    this.dao = context.getBean("userDao", UserDao.class);
    assertThat(dao.getCount(), is(1));
    Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
      dao.get("unknown_id");
    });
  }
}