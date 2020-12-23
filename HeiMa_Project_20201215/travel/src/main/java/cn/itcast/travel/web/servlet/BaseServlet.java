package cn.itcast.travel.web.servlet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

// BaseServlet 不需要被我们访问到的
public class BaseServlet extends HttpServlet {


    /**
     *  展现出被请求的方法
     *  展现出调用了 UserServlet 中的 方法名称
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 完成方法的分发
        // !!!!
        // 1.获取请求路径
        String uri = req.getRequestURI() ;
        System.out.println("请求uri:"+uri);  // /travel/user/add

        // 2.获取方法名称
        // !!!!!!!!!!
        // 从后面往前找斜杠，位置 + 1 为 add 方法的位置
        String methodName = uri.substring(uri.lastIndexOf('/') + 1);
        System.out.println("方法名称:"+methodName);

        // 3.获取方法对象Method
        // 谁调用我，我代表谁
        System.out.println(this);   // 代表UserServlet 对象

        try {
            // 对应的方法对象获取出来了

//            // 使用getDeclaredMethod，忽略访问权限修饰符，获取方法
//            Method method = this.getClass().getDeclaredMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);

              // 使用getDeclaredMethod，忽略访问权限修饰符，获取方法
            Method method = this.getClass().getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);

            // 4.执行方法
            // 暴力反射
            // method.setAccessible(true);  // 强制获取非public 的方法

            method.invoke(this,req,resp) ;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }



    /**
     *  简化书写，防止每次都创建 mapper 对象
     *  直接将传入的对象序列化为json ,并且写回客户端
     * @param obj
     */
    public void writeValue(Object obj,HttpServletResponse response) throws IOException {

        // 2.序列化json返回
        ObjectMapper mapper = new ObjectMapper() ;

        response.setContentType("application/json;charset=utf-8");
        mapper.writeValue(response.getOutputStream(),obj);
    }


    /**
     *  简化书写，防止每次都常见mapper 对象
     *  将传入的对象序列化为json,返回给调用者
     * @param obj
     * @return
     */
    public String writeValueAsStream(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper() ;

        return mapper.writeValueAsString(obj) ;
    }
}





















