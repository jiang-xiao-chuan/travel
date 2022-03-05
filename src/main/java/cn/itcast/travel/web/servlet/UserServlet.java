package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/user/*")
public class UserServlet extends BaseServlet {

    //声明UserServlet业务对象
    private UserService service = new UserServiceImpl();

    /**
     * 注册用户功能
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void register(HttpServletRequest request , HttpServletResponse response) throws ServletException, IOException {

        //验证校验
        String check = request.getParameter("check");

        //从session中获取数据
        HttpSession session = request.getSession();
        String checkcode_server = (String) session.getAttribute("CHECKCODE_SERVER");
        //保证验证码的一次性
        session.removeAttribute(checkcode_server);

        //比较
        if (checkcode_server == null || !checkcode_server.equalsIgnoreCase(check)){
            //验证码错误
            ResultInfo info = new ResultInfo();
            //注册失败
            info.setFlag(false);
            info.setErrorMsg("验证码错误！");

            //将info对象 序列化成json
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(info);
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(json);
            return;

        }


        //1.获取数据
        Map<String, String[]> map = request.getParameterMap();

        //2.封装对象
        User user = new User();

        try {
            BeanUtils.populate(user,map);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        //3.调用service完成注册
        boolean flag =  service.register(user);
        ResultInfo info = new ResultInfo();

        //4.响应结果
        if (flag){
            //注册成功
            info.setFlag(true);
        }else {
            //注册失败
            info.setFlag(false);
            info.setErrorMsg("注册失败！");
        }


        //将info对象 序列化成json
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(info);


        //将json数据写回客户端
        //设置content-type
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(json);
    }

    /**
     * 用户登录功能
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */

    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.获取用户信息
        Map<String, String[]> map = request.getParameterMap();

        //2.封装用户对象
        User user = new User();
        try {
            BeanUtils.populate(user,map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        //3.调用service查询user
        User login_user =  service.login(user);
        ResultInfo info = new ResultInfo();

        //4.判断用户对象是否为空
        if (login_user == null){
            //用户名或者密码错误
            info.setFlag(false);
            info.setErrorMsg("用户名或者密码错误！");
        }

        //5.判断用户是否激活
        if (login_user != null && !"Y".equals(login_user.getStatus())){
            //用户尚未激活
            info.setFlag(false);
            info.setErrorMsg("您尚未激活，请激活...");
        }

        //6.判断登录成功
        if (login_user != null && "Y".equals(login_user.getStatus())){
            //登录成功
            info.setFlag(true);
        }

        //将拿到的login_user对象存入session中
        request.getSession().setAttribute("user",login_user.getName());


        //响应数据
        ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/json;charset=utf-8");
        mapper.writeValue(response.getOutputStream(),info);

    }

    /**
     * 查询单个用户
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */

    public void findOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //从session中获取登录用户
        Object user = request.getSession().getAttribute("user");

        //将user写回客户端
        ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/json,charset=utf-8");
        mapper.writeValue(response.getOutputStream(),user);
    }

    /**
     * 退出登录方法
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void exit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //1.销毁session
        request.getSession().invalidate();

        //2.跳转至登录页面(注意加上虚拟目录)
        response.sendRedirect(request.getContextPath()+"/login.html");

    }

    /**
     * 用户激活方法
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */

    public void Active(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.获取激活码
        String code = request.getParameter("code");
        //判断是否有激活码(判空)
        if (code != null){
            //2.调用service完成激活
            UserService service = new UserServiceImpl();
            boolean flag = service.active(code);

            //3.判断标记
            String msg = null;
            if (flag){
                //成功激活
                msg = "成功激活，请前往<a href='login.html'>登录</a>";
            }else {
                //激活失败
                msg = "激活失败，请稍后重试！";
            }
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write(msg);
        }

    }

}
