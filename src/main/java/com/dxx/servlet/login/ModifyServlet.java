package com.dxx.servlet.login;

import com.alibaba.fastjson.JSONArray;
import com.dxx.pojo.Role;
import com.dxx.pojo.User;
import com.dxx.service.role.RoleService;
import com.dxx.service.role.RoleServiceImpl;
import com.dxx.service.user.UserService;
import com.dxx.service.user.UserServiceImpl;
import com.dxx.util.Constant;
import com.dxx.util.PageSupport;
import com.mysql.cj.util.StringUtils;
import com.mysql.cj.xdevapi.JsonArray;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModifyServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if (method != null && method.equals("savepwd")) {
            try {
                this.updatePwd(req, resp);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }else if (method != null && method.equals("pwdmodify")) {
            this.getUserPassword(req, resp);
        }else if (method != null && method.equals("query")) {
            try {
                this.query(req, resp);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }

    //修改密码
    private void updatePwd(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
        Object o = request.getSession().getAttribute(Constant.USER_SESSION);
        String newpassword = request.getParameter("newpassword");
        boolean flag = false;
        if (o != null && !StringUtils.isNullOrEmpty(newpassword)) {
            UserService userService = new UserServiceImpl();
            flag = userService.updatePwd(((User) o).getId(), newpassword);
            if (flag) {
                request.setAttribute(Constant.SYS_MESSAGE, "修改密码成功,请退出并使用新密码重新登录！");
                request.getSession().removeAttribute(Constant.USER_SESSION);//session注销
            } else {
                request.setAttribute(Constant.SYS_MESSAGE, "修改密码失败！");
            }
        } else {
            request.setAttribute(Constant.SYS_MESSAGE, "修改密码失败！");
        }
        //修改成功后转发就行了
        request.getRequestDispatcher("/jsp/pwdmodify.jsp").forward(request, response);
    }

    //验证旧密码
    public void getUserPassword(HttpServletRequest req, HttpServletResponse resp){
        //1.判断用户旧密码
        Object o = req.getSession().getAttribute(Constant.USER_SESSION);
        String oldpassword = req.getParameter("oldpassword");
        Map<String, String> resultMap = new HashMap<String,String>();
        if(o == null){
            resultMap.put("result","sessionnull");
        }else if(StringUtils.isNullOrEmpty(oldpassword)){
            resultMap.put("result","error");
        }else{
            String userPassword = ((User) o).getUserPassword();
            if(userPassword.equals(oldpassword)){
                resultMap.put("result","true");
            }else{
                resultMap.put("result","error");
            }
        }
        //2.返回给ajax
        resp.setContentType("application/json");
        PrintWriter writer = null;
        try {
            writer = resp.getWriter();
            writer.write(JSONArray.toJSONString(resultMap));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    //查询用户信息
    public void query(HttpServletRequest req,HttpServletResponse resp) throws Exception {
        //查询用户列表
        //从前端获取数据
        String queryUserName = req.getParameter("queryname");
        String temp = req.getParameter("queryUserRole");
        String pageIndex = req.getParameter("pageIndex");
        int queryUserRole = 0;
        UserService userService = new UserServiceImpl();
        List<User> userList = null;
        //第一次走这个请求，一定是第一页，页面大小是固定的
        //设置页面容量
        int pageSize = Constant.pageSize;
        //默认当前页码
        int currentPageNo = 1;

        if (queryUserName == null) {
            queryUserName = "";
        }
        if (temp != null && !temp.equals("")) {
            queryUserRole = Integer.parseInt(temp);
        }

        if (pageIndex != null) {
            try {
                currentPageNo = Integer.valueOf(pageIndex);
            } catch (NumberFormatException e) {
                resp.sendRedirect("error.jsp");
            }
        }
        //总数量（表）
        int totalCount = userService.getUserCount(queryUserName, queryUserRole);
        //总页数
        PageSupport pages = new PageSupport();
        pages.setCurrentPageNo(currentPageNo);  //当前页
        pages.setPageSize(pageSize);            //页面大小
        pages.setTotalCount(totalCount);        //总数量

        int totalPageCount = pages.getTotalPageCount();  //总页面数量

        //控制首页和尾页
        //如果页面小于第一页，就显示第一页
        if (currentPageNo < 1) {
            currentPageNo = 1;
            //如果当前页面大于最后一页，当前页等于最后一页即可
        } else if (currentPageNo > totalPageCount) {
            currentPageNo = totalPageCount;
        }

        //获取用户列表展示
        userList = userService.getUserList(queryUserName, queryUserRole, currentPageNo, pageSize);
        req.setAttribute("userList", userList);
        //获取角色列表
        List<Role> roleList = null;
        RoleService roleService = new RoleServiceImpl();
        roleList = roleService.getRoleList();
        req.setAttribute("roleList", roleList);

        req.setAttribute("queryUserName", queryUserName);
        req.setAttribute("queryUserRole", queryUserRole);
        req.setAttribute("totalPageCount", totalPageCount);
        req.setAttribute("totalCount", totalCount);
        req.setAttribute("currentPageNo", currentPageNo);

        req.getRequestDispatcher("userlist.jsp").forward(req, resp);
    }
}
