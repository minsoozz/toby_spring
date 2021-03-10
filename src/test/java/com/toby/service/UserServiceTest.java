package com.toby.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.toby.dao.UserDao;
import com.toby.domain.Level;
import com.toby.domain.User;
import com.toby.exception.DuplicateUserIdException;
import com.toby.proxy.TransactionHandler;
import com.toby.service.UserServiceImpl.TestUserService.TestUserServiceException;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.PlatformTransactionManager;

@RunWith(SpringRunner.class)
@SpringBootTest
class UserServiceTest {

  @Autowired
  UserDao userDao;

  @Autowired
  PlatformTransactionManager transactionManager;

  List<User> users;

  @BeforeEach
  public void setUp() {
    users = Arrays.asList(
        new User("일민수", "1234", "1", Level.SILVER, 49, 0, ""),
        new User("이민수", "1234", "2", Level.BASIC, 50, 0, ""),
        new User("삼민수", "1234", "3", Level.SILVER, 60, 29, ""),
        new User("사민수", "1234", "4", Level.SILVER, 60, 30, ""),
        new User("오민수", "1234", "5", Level.GOLD, 100, 100, "")
    );
  }

  static class TestUserService extends UserServiceImpl {

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
  public void upgradeAllOrNothing() throws Exception {
    TestUserService testUserService = new TestUserService(users.get(3).getId());
    testUserService.setUserDao(userDao);

    TransactionHandler txHandler = new TransactionHandler();
    txHandler.setTarget(testUserService);
    txHandler.setTransactionManager(transactionManager);
    txHandler.setPattern("upgradeLevels");

    UserService txUserService = (UserService) Proxy.newProxyInstance(
        getClass().getClassLoader(), new Class[] {UserService.class}, txHandler);


/*    UserServiceTx txUserService = new UserServiceTx();
    txUserService.setTransactionManager(transactionManager);
    txUserService.setUserService(testUserService);*/

    userDao.deleteAll();

    for (User user : users) {
      userDao.add(user);
    }

    try {
      txUserService.upgradeLevels();
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

  @Test
  public void mockUpgradeLevels() throws Exception {
    UserServiceImpl userServiceImpl = new UserServiceImpl();

    UserDao mockUserDao = mock(UserDao.class);
    when(mockUserDao.getAll()).thenReturn(this.users);
    userServiceImpl.setUserDao(mockUserDao);

    userServiceImpl.upgradeLevels();

    verify(mockUserDao,times(2)).update(any(User.class));
    verify(mockUserDao,times(2)).update(any(User.class));
    verify(mockUserDao).update(users.get(1));
    assertThat(users.get(1).getLevel()).isEqualTo(Level.SILVER);
    verify(mockUserDao).update(users.get(3));
    assertThat(users.get(3).getLevel()).isEqualTo(Level.GOLD);
  }

  @Test
  public void upgradeLevels() throws Exception {
    UserServiceImpl userServiceImpl = new UserServiceImpl(); // 고립된 테스트 에서는 테스트 대상 오브젝트를 직접 생성하면 된다.

    MockUserDao mockUserDao = new MockUserDao(this.users); // 목 오브젝트로 만든 UserDao 를 직접 DI 해준다
    userServiceImpl.setUserDao(mockUserDao);
    userServiceImpl.upgradeLevels();

    List<User> updated = mockUserDao.getUpdated(); // MockUserDao 로 부터 업데이트 결과를 가져온다.

    assertThat(updated.size()).isEqualTo(2);
    checkUserAndLevel(updated.get(0), "이민수", Level.SILVER);
    checkUserAndLevel(updated.get(1), "사민수", Level.GOLD);
  }

  private void checkUserAndLevel(User updated, String expectedId, Level expectedLevel) {
    assertThat(updated.getId()).isEqualTo(expectedId);
    assertThat(updated.getLevel()).isEqualTo(expectedLevel);
  }

  static class MockUserDao implements UserDao {

    private List<User> users;
    private List<User> updated = new ArrayList<>();

    private MockUserDao(List<User> users) {
      this.users = users;
    }

    public List<User> getUpdated() {
      return this.updated;
    }

    @Override
    public List<User> getAll() {
      return this.users;
    }

    @Override
    public void update(User user) {
      updated.add(user);
    }

    @Override
    public void add(User user) {

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
    public void addAll(List<User> userList) {

    }
  }
}