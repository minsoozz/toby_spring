package com.toby.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class UserDaoAbstract {

  abstract protected PreparedStatement makeStatement(Connection c) throws SQLException;
}
