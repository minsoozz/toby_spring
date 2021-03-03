package com.toby.service;

import com.toby.dao.UserDao;
import com.toby.domain.Level;
import com.toby.domain.User;
import com.toby.service.UserService.TestUserService;
import com.toby.service.UserService.TestUserService.TestUserServiceException;
import java.util.Arrays;
import java.util.List;
import jdk.nashorn.internal.objects.annotations.Setter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class UserServiceTest {

  @Autowired
  UserService userService;

  @Autowired
  UserDao userDao;

  List<User> users;

  @BeforeEach
  public void setUp() {
    users = Arrays.asList(
        new User("a1", "일민수", "1", Level.BASIC, 49, 0, ""),
        new User("a2", "이민수", "2", Level.BASIC, 50, 0, ""),
        new User("a3", "삼민수", "3", Level.SILVER, 60, 29, ""),
        new User("a4", "사민수", "4", Level.SILVER, 60, 30, ""),
        new User("a5", "오민수", "5", Level.GOLD, 100, 100, "")
    );
  }

  @Test
  public void bean() {
    assertThat(this.userService).isNotNull();
  }

  @Test
  public void upgradeLevels() {
    userDao.deleteAll();
    for (User user : users) {
      userDao.add(user);
    }

    userService.upgradeLevels();

    checkLevel(users.get(0), Level.BASIC);
    checkLevel(users.get(1), Level.SILVER);
    checkLevel(users.get(2), Level.SILVER);
    checkLevel(users.get(3), Level.GOLD);
    checkLevel(users.get(4), Level.GOLD);
  }

  @Test
  public void add() {
    userDao.deleteAll();

    User userWithLevel = users.get(4);
    User userWithoutLevel = users.get(0);
    userWithoutLevel.setLevel(null);

    userService.add(userWithLevel);
    userService.add(userWithoutLevel);

    User userWithLevelRead = userDao.get(userWithLevel.getId());
    User userWithoutLevelRead = userDao.get(userWithoutLevel.getId());

    assertThat(userWithLevelRead.getLevel()).isEqualTo(userWithLevel.getLevel());
    assertThat(userWithoutLevelRead.getLevel()).isEqualTo(Level.BASIC);

  }

  private void checkLevel(User user, Level expectedLevel) {
    User userUpdate = userDao.get(user.getId());
    assertThat(userUpdate.getLevel()).isEqualTo(expectedLevel);
  }


  static class TestUserService extends UserService {

    private String id;

    private TestUserService(String id) {
      this.id = id;
    }

    @Override
    protected void upgradeLevel(User user) {
      if (user.getId().equals(this.id)) {
        throw new TestUserServiceException();
      }
      super.upgradeLevel(user);
    }
  }

  @Test
  public void upgradeAllOrNothing() {
    UserService testUserService = new TestUserService(users.get(3).getId());
    testUserService.setUserDao(userDao);
    userDao.deleteAll();
    for (User user : users) {
      userDao.add(user);
    }

    try {
      testUserService.upgradeLevels();
      fail("TestUserServiceException expected");
    } catch (TestUserServiceException e) {
      checkLevelUpgraded(users.get(1), false);
    }
  }

  private void checkLevelUpgraded(User user, boolean upgraded) {
    User userUpdate = userDao.get(user.getId());
    if (upgraded) {
      assertThat(userUpdate.getLevel()).isEqualTo(user.getLevel().nextLevel());
    } else {
      assertThat(userUpdate.getLevel()).isEqualTo(user.getLevel());
    }
  }
}