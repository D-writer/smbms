package com.dxx.service.user;

import com.dxx.pojo.Role;
import com.dxx.pojo.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface UserService {
    public User login(String usercod);

    public boolean updatePwd(int id,String password) throws SQLException;

    int getUserCount(String userName, int userRole) throws Exception;

    public List<User> getUserList(String queryUserName, int queryUserRole, int currentPageNo, int pageSize);


}
