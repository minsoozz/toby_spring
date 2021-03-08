package com.toby.dao;

import com.toby.exception.DuplicateUserIdException;
import com.toby.domain.User;
import java.util.List;

public interface UserDao {
  void add(final User user);
  void addWithDuplicateUserIdException(final User user) throws DuplicateUserIdException;
  User get(String id);
  User getUserByName(String name);
  void deleteAll();
  int getCount();
  List<User> getAll();
  void update(User user);
  void addAll(List<User> userList);
}
