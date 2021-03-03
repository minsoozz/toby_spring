package com.toby.service;

import com.toby.dao.UserDao;
import com.toby.domain.Level;
import com.toby.domain.User;
import java.sql.Connection;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
public class UserService {

  private PlatformTransactionManager transactionManager;

  public void setTransactionManager(PlatformTransactionManager transactionManager) {
    this.transactionManager = transactionManager;
  }

  public void setUserDao(UserDao userDao) {
    this.userDao = userDao;
  }

  @Autowired
  UserDao userDao;

  @Autowired
  DataSource dataSource;

  public static final int MIN_LOGOUT_FOR_SILVER = 50;
  public static final int MIN_RECOMMEND_FOR_GOLD = 30;



/*  public void upgradeLevels() {
    List<User> users = userDao.getAll();
    for (User user : users) {
      Boolean changed = null;
      if(user.getLevel() == Level.BASIC && user.getLogin() >= 50){
        user.setLevel(Level.SILVER);
        changed = true;
      }
      else if(user.getLevel() == Level.SILVER && user.getRecommend() >= 30){
        user.setLevel(Level.GOLD);
        changed = true;
      }
      else if(user.getLevel() == Level.GOLD) {
        changed = false;
      } else {
        changed = false;
      }
      if(changed) {
        userDao.update(user);
      }
    }
  }*/

  /*public void upgradeLevels() {
    List<User> users = userDao.getAll();
    for (User user : users) {
      if (canUpgradeLevel(user)) {
        upgradeLevel(user);
      }
    }
  }*/

/*  public void upgradeLevels() throws Exception {
    TransactionSynchronizationManager.initSynchronization();
    Connection c = DataSourceUtils.getConnection(dataSource); // DB 커넥션을 생성하고 트랜잭션을 생성한다
    c.setAutoCommit(false);

    try{
      List<User> users = userDao.getAll();
      for(User user : users){
        if(canUpgradeLevel(user)){
          upgradeLevel(user);
        }
      }
      c.commit();
    } catch (Exception e){
      c.rollback();
      throw e;
    } finally {
      DataSourceUtils.releaseConnection(c, dataSource);
      TransactionSynchronizationManager.unbindResource(this.dataSource);
      TransactionSynchronizationManager.clearSynchronization(); // 동기화 작업 종료 및 정리
    }
  }

/*  public void upgradeLevels() {
    PlatformTransactionManager transactionManager =
        new DataSourceTransactionManager(dataSource); // JDBC 트랜잭션 추상 오브젝트 생성

    TransactionStatus status =
        transactionManager.getTransaction(new DefaultTransactionDefinition());
    try {
      List<User> users = userDao.getAll();
      for (User user : users) {
        if (canUpgradeLevel(user)) {
          upgradeLevel(user);
        }
      }
      transactionManager.commit(status);
    } catch (RuntimeException e){
      transactionManager.rollback(status);
      throw e;
    }
  }*/

  public void upgradeLevels() {

/*    TransactionStatus status =
        this.transactionManager.getTransaction(new DefaultTransactionDefinition());*/
    try {
      List<User> users = userDao.getAll();
      for (User user : users) {
        if (canUpgradeLevel(user)) {
          upgradeLevel(user);
        }
      }
      /*this.transactionManager.commit(status);*/
    } catch (RuntimeException e) {
      /*this.transactionManager.rollback(status);*/
      throw e;
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

    static class TestUserServiceException extends RuntimeException {

    }
  }



}
