package com.toby.service;

import com.toby.domain.User;

public interface UserService {
  void add(User user);
  void upgradeLevels();
}
