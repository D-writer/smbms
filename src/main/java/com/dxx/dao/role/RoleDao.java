package com.dxx.dao.role;

import com.dxx.pojo.Role;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface RoleDao {
    List<Role> getRoleList(Connection connection) throws SQLException;
}
