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
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/registUserServlet")
public class RegistUserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 验证码的校验
        String check = request.getParameter("check");

        // 从session 中获取验证码
        HttpSession session = request.getSession();
        // 强制转换成 String 类型
        String checkcode_server = (String) session.getAttribute("CHECKCODE_SERVER");

        // 使用验证码后，移除使用后的验证码
        session.removeAttribute("CHECKCODE_SERVER");

        // 比较
        if( checkcode_server == null || !checkcode_server.equalsIgnoreCase(check) ) {
            // 验证码错误
            ResultInfo info = new ResultInfo() ;
            info.setFlag(false);
            info.setErrorMsg("验证码错误");

            // 将info 对象序列化为 json ,
            ObjectMapper mapper = new ObjectMapper( ) ;
            String json = mapper.writeValueAsString(info) ;

            response.setContentType("application/json;charset=utf-8");
            // 用字符流和字节流都可以，使用字符流更方便些
            //  response.getOutputStream().write(json.get)
            response.getWriter().write(json) ;  // 字符流
            return ;
        }

        // 1.获取数据
        Map<String, String[]> map = request.getParameterMap();

        // 2. 封装对象
        User user = new User() ;
        try {
            BeanUtils.populate(user,map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        // 3. 调用service 完成注册
        UserService service = new UserServiceImp() ;
        boolean flag = service.regist(user) ;


        ResultInfo info = new ResultInfo() ;

        // 4.响应结果
        if(flag) {
            // 注册成功
            // 给info 一个true
            info.setFlag(true);
        } else {
            // 注册失败
            // 给info 一个 false
            info.setFlag(false);
            info.setErrorMsg("注册失败");
        }

        // 将info 对象序列化为 json ,
        ObjectMapper mapper = new ObjectMapper( ) ;
        String json = mapper.writeValueAsString(info) ;

        // 将json 数据写回客户端
        // 设置content-type
        response.setContentType("application/json;charset=utf-8");
        // 用字符流和字节流都可以，使用字符流更方便些
        //  response.getOutputStream().write(json.get)
        response.getWriter().write(json) ;  // 字符流


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
