package com.toby.service;

import com.toby.dao.UserDao;
import com.toby.domain.Level;
import com.toby.domain.User;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@Service
public class UserServiceImpl implements UserService {

  public void setUserDao(UserDao userDao) {
    this.userDao = userDao;
  }

  @Autowired
  UserDao userDao;

  @Autowired
  DataSource dataSource;

  public static final int MIN_LOGOUT_FOR_SILVER = 50;
  public static final int MIN_RECOMMEND_FOR_GOLD = 30;

  public void upgradeLevels() {
    List<User> users = userDao.getAll();
    for (User user : users) {
      if (canUpgradeLevel(user)) {
        upgradeLevel(user);
      }
    }
  }
  /**
   * 리스트 6-1 트랜잭션 경계설정과 비즈니스 로직이 공존하는 메소드
   */
/*  public void upgradeLevels() {
    TransactionStatus status =
        this.transactionManager.getTransaction(new DefaultTransactionDefinition());
    try {
      List<User> users = userDao.getAll();
      for (User user : users) {
        if (canUpgradeLevel(user)) {
          upgradeLevel(user);
        }
      }
      this.transactionManager.commit(status);
    } catch (RuntimeException e) {
      this.transactionManager.rollback(status);
      throw e;
    }
  }*/


/*  public void upgradeLevels() {
    try {
      upgradeLevelsInternal();
    } catch (Exception e) {
      throw e;
    }
  }*/

  /**
   * 분리된 비즈니스 로직 코드, 트랜잭션을 적용하기 전과 동일하다
   */
  private void upgradeLevelsInternal() {
    List<User> users = userDao.getAll();
    for (User user : users) {
      if (canUpgradeLevel(user)) {
        upgradeLevel(user);
      }
    }
  }

  private boolean canUpgradeLevel(User user) {
    Level currentLevel = user.getLevel();
    switch (currentLevel) {
      case BASIC:
        return (user.getLogin() >= MIN_LOGOUT_FOR_SILVER);
      case SILVER:
        return (user.getRecommend() >= MIN_RECOMMEND_FOR_GOLD);
      case GOLD:
        return false;

      default:
        throw new IllegalArgumentException("오류~  : " + currentLevel);
    }
  }

  protected void upgradeLevel(User user) {
/*    if (user.getLevel() == Level.BASIC) {
      user.setLevel(Level.SILVER);
    } else if (user.getLevel() == Level.SILVER) {
      user.setLevel(Level.GOLD);
    }*/
    user.upgradeLevel();
    userDao.update(user);
  }

  public void add(User user) {
    if (user.getLevel() == null) {
      user.setLevel(Level.BASIC);
    }
    userDao.add(user);
  }
}
