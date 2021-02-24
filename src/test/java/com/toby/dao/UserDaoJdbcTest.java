package com.toby.dao;

import static org.assertj.core.api.Assertions.assertThat;

import com.toby.domain.Level;
import com.toby.domain.User;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
class UserDaoJdbcTest {

  @Autowired
  private UserDao dao;

  @Autowired
  private DataSource dataSource;

  User user1;
  User user2;
  User user3;

  @BeforeEach
  public void setUp() {
    this.user1 = new User("minsoo1", "일민수", "spring1", Level.BASIC, 49, 0);
    this.user2 = new User("minsoo2", "이민수", "spring2", Level.SILVER, 50, 0);
    this.user3 = new User("minsoo3", "삼민수", "spring3", Level.GOLD, 60, 29);
  }

  @Test
  public void add() {
    User user = new User("minsoo", "1234", "김민수", Level.GOLD, 1000, 500);
    dao.add(user);
    assertThat(user.getId()).isEqualTo("minsoo");
  }

  @Test
  public void update() {
    dao.deleteAll();

    dao.add(user1);
    dao.add(user2);

    user1.setName("오민규");
    user1.setPassword("springno06");
    user1.setLevel(Level.GOLD);
    user1.setLogin(1000);
    user1.setRecommend(999);
    dao.update(user1);

    User user1update = dao.get(user1.getId());
    checkSameUser(user1,user1update);

    User user2same = dao.get(user2.getId());
    checkSameUser(user2,user2same);
  }

  @Test
  private void checkSameUser(User user1, User user2) {
    assertThat(user1.getId()).isEqualTo(user2.getId());
    assertThat(user1.getLevel()).isEqualTo(user2.getLevel());
  }
}