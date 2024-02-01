package com.dxx.dao;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class BaseDao {
    private static String driver;
    private static String url;
    private static String username;
    private static String password;
    //静态代码块，类加载的时候就初始化
    static {
        InputStream is = BaseDao.class.getClassLoader().getResourceAsStream("db.properties");
        Properties properties = new Properties();

        try {
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        driver = properties.getProperty("driver");
        url = properties.getProperty("url");
        username = properties.getProperty("username");
        password = properties.getProperty("password");

    }
    //获取数据库的连接
    public static Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url,username,password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    //编写查询公共类
    public static ResultSet execute(Connection connection,String sql,Object[] params,PreparedStatement preparedStatement) throws SQLException {
        preparedStatement = connection.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            preparedStatement.setObject(i+1,params[i]);
        }
         //resultSet = preparedStatement.executeQuery(sql);
        ResultSet resultSet1 = preparedStatement.executeQuery();
        return resultSet1;
    }

    //编写增删改工具类
    public static int executeupdate(Connection connection,String sql,Object[] params) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            preparedStatement.setObject(i+1,params[i]);
        }
//        System.out.println(preparedStatement.toString());
        int rows = preparedStatement.executeUpdate();
        return rows;
    }
    //释放资源
    public static boolean closeResource(Connection connection, ResultSet resultSet,PreparedStatement preparedStatement)  {
        boolean flag = true;
        if(resultSet!=null){
            try {
                resultSet.close();
                resultSet=null;
            } catch (SQLException e) {
                e.printStackTrace();
                flag=false;
            }

        }
        if(connection!=null){
            try {
                connection.close();
                connection=null;
            } catch (SQLException e) {
                e.printStackTrace();
                flag=false;
            }

        }if(preparedStatement!=null){
            try {
                preparedStatement.close();
                preparedStatement=null;
            } catch (SQLException e) {
                e.printStackTrace();
                flag=false;
            }

        }

        return flag;

    }

    @Test
    public void test() throws SQLException {
        Connection connection = BaseDao.getConnection();
        String sql = "select * from smbms_user where usercode=?";
        Object[] params = {"admin"};
        ResultSet rs = null;
        PreparedStatement ps = null;

        ResultSet resultSet = execute(connection, sql, params, ps);
        while(resultSet.next()){
            System.out.println(resultSet.getString("userpassword"));
        }


    }
}
