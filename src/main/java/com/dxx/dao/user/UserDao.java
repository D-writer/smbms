package com.dxx.dao.user;

import com.dxx.pojo.Role;
import com.dxx.pojo.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface UserDao {
    //得到登录的用户
    public User getLoginUser(Connection conn,String usercode) throws SQLException;

    //修改当前用户密码
    public int updatePwd(Connection conn,int id,String password) throws SQLException;

    //获取用户总数
    int getUserCount(Connection connection, String userName, int userRole) throws Exception;

    //获取角色列表
    List<User> getUserList(Connection connection, String userName, int userRole, int currentPageNo, int pageSize) throws Exception;


}
