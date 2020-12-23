package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImp;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Random;

@WebServlet("/user/*")  // user/add    user/find
public class UserServlet extends BaseServlet {

    /**
     *  声明一个UserService 的业务对象
     */
    private UserService service = new UserServiceImp() ;


    /**
     *  注册功能
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void regist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

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
        // UserService service = new UserServiceImp() ;
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


//        /**
//         *  把user对象的Value 值， 保存到session
//         */
//        request.getSession().setAttribute("user", user);
//
//        System.out.println(session);

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


    /**
     *  登录功能
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        /**
         *  登录的验证码的校验，直接采用注册时验证码校验的代码
         */
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


//        this.doPost(request, response);
//        System.out.println("userServlet的find方法....");

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
        // UserService service = new UserServiceImp() ;

        // 通过 service 对象调用 login()函数，把函数调用的结果报存在 对象 u 上
        User u =  service.login(user) ;

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
            request.getSession().setAttribute("user", u);
        }

        // 响应数据
//        ObjectMapper mapper = new ObjectMapper() ;
//
//        response.setContentType("application/json;charset=utf-8");
//        // 把对应的数据流、info 信息给写过去
//        mapper.writeValue(response.getOutputStream(),info) ;

        writeValue(info,response);
    }


    /**
     *  查找单个对象
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 从session 中获取登录用户user
        Object user = request.getSession().getAttribute("user");

        // 将user 写回客户端
//        ObjectMapper mapper = new ObjectMapper() ;
//        response.setContentType("application/json;charset=utf-8");
//        mapper.writeValue(response.getOutputStream(), user) ;

        writeValue(user,response);


    }


    /**
     *  退出功能
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void exit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1.销毁session
        request.getSession().invalidate() ;

        // 2.跳转到登录页面
        response.sendRedirect(request.getContextPath()+"/login.html");
    }


    /**
     * 激活功能
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void active(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1.获取激活码
        // 邮箱网页发送的地址中，后缀自动携带着code
        // 此时只要读取到code 范围内容就可以了
        String code = request.getParameter("code");

        if(code != null ) {
            // 2.调用service 完成激活
            // UserService service = new UserServiceImp() ;

            // active() 方法的结果，结果返回一个 boolean 类型的值
            boolean flag = service.active(code) ;

            // 3.判断标记
            String msg = null ;
            // 浏览器直接输出一句话
            if(flag) {
                // 激活成功
//                msg = "激活成功，请登录<a href='login.html'>登录</a>" ;
                // 当前目录处于user 下，要先返回上一层，变为localhost/travel/login.html
                msg = "激活成功，请登录<a href='../login.html'>登录</a>" ;
            }  else {
                // 激活失败
                msg = "激活失败，请联系管理员" ;
            }

            // 把上述信息写回到客户端
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write(msg) ;
        }
    }


    /**
     *  获取验证码功能
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void code(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //服务器通知浏览器不要缓存
        response.setHeader("pragma","no-cache");
        response.setHeader("cache-control","no-cache");
        response.setHeader("expires","0");

        //在内存中创建一个长80，宽30的图片，默认黑色背景
        //参数一：长
        //参数二：宽
        //参数三：颜色
        int width = 80;
        int height = 30;
        BufferedImage image = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);

        //获取画笔
        Graphics g = image.getGraphics();
        //设置画笔颜色为灰色
        g.setColor(Color.GRAY);
        //填充图片
        g.fillRect(0,0, width,height);

        //产生4个随机验证码，12Ey
        String checkCode = getCheckCode();
        //将验证码放入HttpSession中
        request.getSession().setAttribute("CHECKCODE_SERVER",checkCode);

        //设置画笔颜色为黄色
        g.setColor(Color.YELLOW);
        //设置字体的小大
        g.setFont(new Font("黑体",Font.BOLD,24));
        //向图片上写入验证码
        g.drawString(checkCode,15,25);

        //将内存中的图片输出到浏览器
        //参数一：图片对象
        //参数二：图片的格式，如PNG,JPG,GIF
        //参数三：图片输出到哪里去
        ImageIO.write(image,"PNG",response.getOutputStream());
    }
    /**
     * 产生4位随机字符串
     */
    private String getCheckCode() {
        String base = "0123456789ABCDEFGabcdefg";
        int size = base.length();
        Random r = new Random();
        StringBuffer sb = new StringBuffer();
        for(int i=1;i<=4;i++){
            //产生0到size-1的随机值
            int index = r.nextInt(size);
            //在base字符串中获取下标为index的字符
            char c = base.charAt(index);
            //将c放入到StringBuffer中去
            sb.append(c);
        }
        return sb.toString();

    }

}





