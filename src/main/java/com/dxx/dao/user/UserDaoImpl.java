package com.dxx.dao.user;

import com.dxx.dao.BaseDao;
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

public class UserDaoImpl implements UserDao{
    @Override
    public User getLoginUser(Connection conn, String usercode) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        User user = null;
    if(conn!=null) {
        String sql = "select * from smbms_user where usercode=? ";
        Object[] params = {usercode};

        try {
            rs = BaseDao.execute(conn, sql, params, ps);
            if(rs.next()){
                user = new User();
                //将这些值丢给用户
                user.setId(rs.getInt("id"));
                user.setUserCode(rs.getString("userCode"));
                user.setUserName(rs.getString("userName"));
                user.setUserPassword(rs.getString("userPassword"));
                user.setGender(rs.getInt("gender"));
                user.setBirthday(rs.getDate("birthday"));
                user.setPhone(rs.getString("phone"));
                user.setAddress(rs.getString("address"));
                user.setUserRole(rs.getInt("userRole"));
                user.setCreatedBy(rs.getInt("createdBy"));
                user.setModifyBy(rs.getInt("modifyBy"));
                user.setModifyDate(rs.getDate("modifyDate"));
            }
            BaseDao.closeResource( null,rs,ps);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    return user;
    }

    @Override
    public int updatePwd(Connection conn, int id, String password) throws SQLException {
        int flag = 0;
        String sql = "update smbms_user set userpassword=? where id=? ";
        PreparedStatement ps = null;
        Object[] params = {password,id};
        if (conn != null) {
            flag = BaseDao.executeupdate(conn, sql, params);
            BaseDao.closeResource( null,null,ps);
        }

        return flag;
    }

    @Override
    public int getUserCount(Connection conn, String userName, int userRole) throws Exception {
        int count = 0;
        PreparedStatement pstm = null;
        ArrayList list = new ArrayList();
        String sql = " select count(1) as count from smbms_user u,smbms_role r where u.userRole = r.id ";
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(sql);
        if(userName!= null){
            stringBuffer.append(" and username like ?");
            list.add("%"+userName+"%");
        }
        if (userRole > 0) {
            stringBuffer.append(" and u.userRole = ?");
            list.add(userRole);
        }
        Object[] params =list.toArray();

        ResultSet rs = BaseDao.execute(conn, stringBuffer.toString(), params, pstm);
        while(rs.next()){
            count = rs.getInt("count");
        }
        //及时关闭，防止内存溢出
        BaseDao.closeResource(null,rs,pstm);

        return count;
    }
    @Override
    public List<User> getUserList(Connection connection, String userName, int userRole, int currentPageNo, int pageSize)
            throws Exception {
        PreparedStatement pstm = null;
        ResultSet rs = null;
        List<User> userList = new ArrayList<User>();
        if (connection != null) {
            StringBuffer sql = new StringBuffer();
            sql.append("select u.*,r.roleName as userRoleName from smbms_user u,smbms_role r where u.userRole = r.id");
            List<Object> list = new ArrayList<Object>();
            if (!StringUtils.isNullOrEmpty(userName)) {
                sql.append(" and u.userName like ?");
                list.add("%" + userName + "%");
            }
            if (userRole > 0) {
                sql.append(" and u.userRole = ?");
                list.add(userRole);
            }
            //在mysql数据库中，分页使用 limit startIndex，pageSize ; 总数
            sql.append(" order by creationDate DESC limit ?,?");
            currentPageNo = (currentPageNo - 1) * pageSize;
            list.add(currentPageNo);
            list.add(pageSize);

            Object[] params = list.toArray();
            System.out.println("sql ----> " + sql.toString());
            rs = BaseDao.execute(connection,sql.toString(),params,pstm);
            while (rs.next()) {
                User _user = new User();
                _user.setId(rs.getInt("id"));
                _user.setUserCode(rs.getString("userCode"));
                _user.setUserName(rs.getString("userName"));
                _user.setGender(rs.getInt("gender"));
                _user.setBirthday(rs.getDate("birthday"));
                _user.setPhone(rs.getString("phone"));
                _user.setUserRole(rs.getInt("userRole"));
                _user.setUserRoleName(rs.getString("userRoleName"));
                userList.add(_user);
            }
            BaseDao.closeResource(null, rs, pstm);
        }
        return userList;
    }



    @Test
    public void testpwd() throws SQLException {
        Connection conn = BaseDao.getConnection();
        int id = 1;
        String password = "123123";
        int i = updatePwd(conn, id, password);
        System.out.println(i+" rows be affected...");
    }

    @Test
    public void testCount() throws Exception {
        Connection conn = BaseDao.getConnection();
        int rows = getUserCount(conn, "admin", 1);
        System.out.println("总共有"+rows+"条数据");
    }


    @Test
    public void testUserList() throws Exception {
        Connection conn = BaseDao.getConnection();
        UserDao userDao = new UserDaoImpl();
        List<User> list = userDao.getUserList(conn, "admin", 1, 1, 5);
        for (User user:list) {
            System.out.println(user.toString());
        }
    }



}
