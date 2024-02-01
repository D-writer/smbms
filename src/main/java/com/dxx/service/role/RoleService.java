package com.dxx.service.role;

import com.dxx.pojo.Role;

import java.sql.SQLException;
import java.util.List;

public interface RoleService {
    public List<Role> getRoleList() throws SQLException;
}
