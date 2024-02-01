package com.dxx.dao.role;

import com.dxx.dao.BaseDao;
import com.dxx.dao.user.UserDao;
import com.dxx.dao.user.UserDaoImpl;
import com.dxx.pojo.Role;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoleDaoImpl implements RoleDao{
    public List<Role> getRoleList(Connection connection) throws SQLException {
        ArrayList<Role> roles = new ArrayList<>();
        PreparedStatement ps = null;
        Object[] params = {};
        String sql = "select * from smbms_role";
        if(connection!=null){
            ResultSet rs = BaseDao.execute(connection, sql, params, ps);
            while(rs.next()){
                Role role = new Role();
                role.setId(rs.getInt("id"));
                role.setRoleCode(rs.getString("rolecode"));
                role.setRoleName(rs.getString("rolename"));
                role.setModifyBy(rs.getInt("modifyby"));
                role.setModifyDate(rs.getDate("modifydate"));
                role.setCreatedBy(rs.getInt("createdby"));
                role.setCreationDate(rs.getDate("creationdate"));
                roles.add(role);
            }
            BaseDao.closeResource(null,rs,ps);
        }
        return roles;

    }

    @Test
    public void testRoleList() throws SQLException {
        Connection conn = BaseDao.getConnection();
        RoleDao roleDao = new RoleDaoImpl();
        List<Role> roleList = roleDao.getRoleList(conn);
        for (Role role: roleList) {
            System.out.println(role.toString());
        }
    }
}
