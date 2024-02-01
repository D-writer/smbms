package com.dxx.servlet.login;

import com.dxx.pojo.User;
import com.dxx.service.user.UserServiceImpl;
import com.dxx.util.Constant;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("load logoutServlet...");
        //移除用户的session
        req.getSession().removeAttribute(Constant.USER_SESSION);
        resp.sendRedirect(req.getContextPath()+"/login.jsp");

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}
