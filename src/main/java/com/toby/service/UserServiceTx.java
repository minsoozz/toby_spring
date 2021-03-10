package com.toby.service;

import com.toby.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@Service
public class UserServiceTx implements UserService {

  UserService userService;  // 타깃 오브젝트
  PlatformTransactionManager transactionManager;

  public void setTransactionManager(PlatformTransactionManager transactionManager) {
    this.transactionManager = transactionManager;
  }

  public void setUserService(UserService userService) {
    this.userService = userService;
  }

  @Override
  public void add(User user) {  // 메소드 구현과 위임
    userService.add(user);
  }

  @Override
  public void upgradeLevels() { // 메소드 구현
    TransactionStatus status = this.transactionManager  // 부가기능 수행
        .getTransaction(new DefaultTransactionDefinition());

    try {
      userService.upgradeLevels();
      this.transactionManager.commit(status); // 부가기능 수행
    } catch (RuntimeException e) {
      this.transactionManager.rollback(status);
      throw e;
    }

  }
}
