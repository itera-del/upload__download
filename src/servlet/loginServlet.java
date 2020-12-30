package servlet;

import domain.User;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import util.JDBCUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * @author 刘芋池
 * @Description
 * @create 2020/12/24 21:26
 */
@WebServlet("/loginServlet")
public class loginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JdbcTemplate template=new JdbcTemplate(JDBCUtils.getDataSource());
        //1.设置编码
        request.setCharacterEncoding("utf-8");
        //2.获取数据
        //2.1获取验证码
        String verifycode = request.getParameter("verifycode");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
//        Map<String, String[]> map = request.getParameterMap();
        //3.校验验证码
        HttpSession session = request.getSession();
        String checkcode_server = (String) session.getAttribute("CHECKCODE_SERVER");
        //保证验证码一次性使用
        session.removeAttribute("CHECKCODE_SERVER");
        if(!checkcode_server.equalsIgnoreCase(verifycode)){
            //验证码不正确
            request.setAttribute("login_msg","验证码不正确！");
            request.getRequestDispatcher("/login.jsp");
        }
        /*//3.封装User对象
        User user=new User();
        try {
            BeanUtils.populate(user,map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }*/
        User user=new User();
        try {
            String sql = "select * from user where username =? and password=?";
            user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), username,password);
        }catch (Exception e){
            e.printStackTrace();
        }
        //判断是否登陆成功

        if(user!=null){
            //登录成功
            //将用户存到session,欢迎***
            session.setAttribute("user",user);
            //跳转页面
            response.sendRedirect(request.getContextPath()+"/index.jsp");


        }else{
            request.setAttribute("login_msg","用户名或密码错误！");
            request.getRequestDispatcher("/login.jsp");

        }

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
