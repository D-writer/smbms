package com.dxx.servlet.login;

import com.dxx.pojo.User;
import com.dxx.service.user.UserServiceImpl;
import com.dxx.util.Constant;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userCode = req.getParameter("userCode");
        String userPassword = req.getParameter("userPassword");

        UserServiceImpl userService = new UserServiceImpl();
        User user = userService.login(userCode);
        if(user!= null && user.getUserPassword().equals(userPassword)){
            req.getSession().setAttribute(Constant.USER_SESSION,user);
            resp.sendRedirect("jsp/frame.jsp");
        }else {
            req.setAttribute("error","用户名或密码不正确");
            req.getRequestDispatcher("login.jsp").forward(req,resp);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}
