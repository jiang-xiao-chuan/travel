package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/loginServlet")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
        UserService service = new UserServiceImpl();
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
}
