package cn.itcast.travel.web.servlet;

import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImp;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// have been moved :become useless

@WebServlet("/activeUserServlet")
public class ActiveUserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1.获取激活码
        // 邮箱网页发送的地址中，后缀自动携带着code
        // 此时只要读取到code 范围内容就可以了
        String code = request.getParameter("code");

        if(code != null ) {
            // 2.调用service 完成激活
            UserService service = new UserServiceImp() ;

            // active() 方法的结果，结果返回一个 boolean 类型的值
            boolean flag = service.active(code) ;

            // 3.判断标记
            String msg = null ;
            // 浏览器直接输出一句话
            if(flag) {
                // 激活成功
                msg = "激活成功，请登录...<a href='login.html'>登录</a>" ;
//                msg = "激活成功，请登录<a href='../login.html'>登录</a>" ;
            }  else {
                // 激活失败
                msg = "激活失败，请联系管理员" ;
            }

            // 把上述信息写回到客户端
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write(msg) ;

        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
