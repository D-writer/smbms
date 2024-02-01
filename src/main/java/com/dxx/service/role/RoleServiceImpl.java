package com.dxx.service.role;

import com.dxx.dao.BaseDao;
import com.dxx.dao.role.RoleDao;
import com.dxx.dao.role.RoleDaoImpl;
import com.dxx.dao.user.UserDao;
import com.dxx.dao.user.UserDaoImpl;
import com.dxx.pojo.Role;
import com.dxx.service.user.UserService;
import com.dxx.service.user.UserServiceImpl;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class RoleServiceImpl implements RoleService{
    @Override
    public List<Role> getRoleList() throws SQLException {
        Connection connection = BaseDao.getConnection();
        RoleDao dao = new RoleDaoImpl();
        List<Role> roleList = dao.getRoleList(connection);
        BaseDao.closeResource(connection,null,null);
        return roleList;
    }

    @Test
    public void testRoleList() throws SQLException {
        RoleService service = new RoleServiceImpl();
        List<Role> roleList = service.getRoleList();
        for (Role role:roleList) {
            System.out.println(role.toString());
        }
    }
}
