package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImp;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/loginServlet")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1.获取用户名和密码数据
        Map<String, String[]> map = request.getParameterMap();

        // 2.封装User
        User user = new User()  ;

        try {
            BeanUtils.populate(user,map) ;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        // 3.调用Service 查询
        UserService service = new UserServiceImp() ;
        // 通过 service 对象调用 login()函数，把函数调用的结果报存在 对象 u 上
        // UserServiceImp类中的login方法的目的是   获取用户名和密码
        // u 中保存着用户名和密码
        User u =  service.login(user) ;

        // ResultInfo() ：包含各种 flag 标志位
        ResultInfo info = new ResultInfo();

        // 4.判断用户对象是否为null
        if(u == null) {
            //  用户名或密码错误
            // 设置 info 对象的信息
            info.setFlag(false) ;
            info.setErrorMsg("用户名或密码错误");
        }

        // 5.如果用户名和密码是正确的，判断用户是否激活
        if(u != null && !"Y".equals(u.getStatus() ) ) {
            // 用户尚未激活
            info.setFlag(false) ;
            info.setErrorMsg("您尚未激活，请激活");
        }

        // 6.判断登录成功
        if(u != null && "Y".equals(u.getStatus() ) ) {
            // 登录成功
            info.setFlag(true) ;


            /**
             *  XXXXXXXXXXXX
             *  视频教程里的错误，少了这句话
             */
            // 把用户user 和 u（包含着用户名和密码） 传入到 session 中 ，防止 “ name 为 null ” 的错误
            request.getSession().setAttribute("user", u);
        }

        // 响应数据
        ObjectMapper mapper = new ObjectMapper() ;

        response.setContentType("application/json;charset=utf-8");
        // 把对应的数据流、info 信息给写过去
        mapper.writeValue(response.getOutputStream(),info) ;

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
