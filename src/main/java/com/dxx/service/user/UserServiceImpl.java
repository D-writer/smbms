package com.dxx.service.user;

import com.dxx.dao.BaseDao;
import com.dxx.dao.user.UserDaoImpl;
import com.dxx.dao.user.UserDao;
import com.dxx.pojo.Role;
import com.dxx.pojo.User;
import com.mysql.cj.util.StringUtils;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserServiceImpl implements UserService{

    private UserDao userDao;
    public  UserServiceImpl(){
        userDao = new UserDaoImpl();
    }
    public User login(String usercode){
        Connection conn = null;
        User user = null;

        try {
            conn = BaseDao.getConnection();
            User loginUser = userDao.getLoginUser(conn, usercode);
            return loginUser;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeResource(conn,null,null);
        }
        return null;
    }

    //todo
    @Override
    public boolean updatePwd(int id, String password) {
        Boolean flag = false;


        UserDao user = new UserDaoImpl();

        try {
            Connection conn = BaseDao.getConnection();
            int i = user.updatePwd(conn, id, password);
            return i>0?true:flag;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public int getUserCount( String userName, int userRole) throws Exception {
        int count = 0;
        Connection connection = BaseDao.getConnection();
        UserDao dao = new UserDaoImpl();
        count = dao.getUserCount(connection, userName, userRole);
        return count;
    }

    @Override
    public List<User> getUserList(String queryUserName, int queryUserRole, int currentPageNo, int pageSize) {
        Connection connection = null;
        List<User> userList = null;
        try {
            connection = BaseDao.getConnection();
            userList = userDao.getUserList(connection, queryUserName, queryUserRole, currentPageNo, pageSize);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeResource(connection, null, null);
        }
        return userList;
    }



    @Test
    public void test(){
        UserServiceImpl userService = new UserServiceImpl();
       boolean b = userService.updatePwd(1, "234567");
       System.out.println("密码修改=="+b);
   }

    @Test
    public void testCount() throws Exception {

        UserServiceImpl userService = new UserServiceImpl();
        int userCount = userService.getUserCount("admin",1);
        System.out.println("总共有"+userCount+"条数据");
    }

    @Test
    public void testUserList(){
        UserService userService = new UserServiceImpl();
        List<User> userList = userService.getUserList(null, 1, 1, 5);
        for (User user:userList) {
            System.out.println(user);
        }
    }


}
