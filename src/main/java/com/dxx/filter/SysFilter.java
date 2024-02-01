package com.dxx.filter;

import com.dxx.pojo.User;
import com.dxx.util.Constant;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
//权限过滤
public class SysFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        User user = (User) request.getSession().getAttribute(Constant.USER_SESSION);
        if(user==null){
            response.sendRedirect("/error.jsp");
        }else{
            filterChain.doFilter(req,resp);
        }
    }

    @Override
    public void destroy() {

    }
}
