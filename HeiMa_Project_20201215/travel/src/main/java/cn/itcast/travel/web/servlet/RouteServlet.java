package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.Favorite;
import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.FavoriteService;
import cn.itcast.travel.service.RouteService;
import cn.itcast.travel.service.impl.FavoriteServiceImp;
import cn.itcast.travel.service.impl.RouteServiceImp;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/route/*")
public class RouteServlet extends BaseServlet {

    private RouteService routeService = new RouteServiceImp()  ;
    private FavoriteService favoriteService = new FavoriteServiceImp() ;

    /**
     *  分页查询
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void pageQuery(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1.接收参数
        String currentPageStr = request.getParameter("currentPage");
        String pageSizeStr = request.getParameter("pageSize");
        // 这里可能会获得cid 字符串 "null" ,也是个字符串
        String cidStr = request.getParameter("cid");

        // 接收rname 线路名称
        String rname = request.getParameter("rname") ;

        try {
            // 处理tomcat 7 的乱码问题
            rname = new String(rname.getBytes("iso-8859-1"), "utf-8");
        }  catch (Exception e) {

        }
        int cid = 0 ;  // 类别 id

        // 2.处理参数
        // 处理条件，还包含cidStr 字符串不能为  "null" 字符串，防止下面强制转换时异常
        if(cidStr != null  && cidStr.length() > 0 && !"null".equals(cidStr))  {
            cid = Integer.parseInt(cidStr) ;
        }
        /**
         * 如果cid 保持为0 ，之后会在拼条件时把他它排除
         * 相当于只搜索rname=西安，即route_list.html?rname=西安 ，除去了cid 的条件
         */


        int currentPage = 0 ;  // 当前页码，如果不传递当前页码，默认为第一页
        if(currentPageStr != null  && currentPageStr.length() > 0)  {
            currentPage = Integer.parseInt(currentPageStr) ;
        } else {
            currentPage = 1;
        }

        int pageSize = 0 ;  // 每页显示条数，如果不传递，默认每页显示5条记录
        if(pageSizeStr != null  && pageSizeStr.length() > 0)  {
            pageSize = Integer.parseInt(pageSizeStr) ;
        } else {
            pageSize = 5;
        }

        // 3.调用 service 查询 PageBean 对象
        PageBean<Route> pb = routeService.pageQuery(cid, currentPage, pageSize,rname);

        // 4.将PageBean 对象序列化为 Json 返回
        writeValue(pb,response);
        System.out.println("list 为:" + pb.getList());
    }

    /**
     * 用户收藏页面的分页查询
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void pageQuery_MF(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1.接收参数
        // request 来自客户端，这些参数的值都来自客户端
        String currentPageStr = request.getParameter("currentPage");
        String pageSizeStr = request.getParameter("pageSize");
        // 这里可能会获得uid 字符串 "null" ,也是个字符串

        /**
         *  要能够获取当前用户的uid
         */
//        String uidStr = request.getParameter("uid");

        // 获取当前登录用户的 user
        User user = (User) request.getSession().getAttribute("user");

        int uid ;  // 用户的id

        if(user == null)  {
            // 用户尚未登录
//            uid = 0 ;
            // 直接返回
            return ;
        } else {
            // 用户已 经登录
            uid = user.getUid() ;
        }


        // 接收rname 线路名称
        String rname = request.getParameter("rname") ;

        try {
            // 处理tomcat 7 的乱码问题
            rname = new String(rname.getBytes("iso-8859-1"), "utf-8");
        }  catch (Exception e) {

        }


//        int uid = 0 ;  // 类别 id
//
//        // 2.处理参数
//        // 处理条件，还包含cidStr 字符串不能为  "null" 字符串，防止下面强制转换时异常
//        if(uidStr != null  && uidStr.length() > 0 && !"null".equals(uidStr))  {
//            uid = Integer.parseInt(uidStr) ;
//        }
        /**
         * 如果uid 保持为0 ，之后会在拼条件时把他它排除
         * 相当于只搜索rname=西安，即route_list.html?rname=西安 ，除去了cid 的条件
         */


        int currentPage = 0 ;  // 当前页码，如果不传递当前页码，默认为第一页
        if(currentPageStr != null  && currentPageStr.length() > 0)  {
            currentPage = Integer.parseInt(currentPageStr) ;
        } else {
            currentPage = 1;
        }

        int pageSize = 0 ;  // 每页显示条数，如果不传递，默认每页显示5条记录
        if(pageSizeStr != null  && pageSizeStr.length() > 0)  {
            pageSize = Integer.parseInt(pageSizeStr) ;
        } else {
            pageSize = 5;
        }

        // 3.调用 service 查询 PageBean 对象
        PageBean<Route> pb = routeService.pageQuery_MF(uid, currentPage, pageSize,rname);

        // 4.将PageBean 对象序列化为 Json 返回
        writeValue(pb,response);
        System.out.println("list_MF 为:" + pb.getList());
    }




    /**
     *  根据id 查询一个旅游线路的详细信息
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1.接收id
        String rid = request.getParameter("rid") ;

        // 2. 调用service 查询route 对象
        Route route = routeService.findOne(rid) ;

        // 3. 转为 json 写回客户端
        writeValue(route, response) ;
    }

    /**
     *   判断当前登录用户是否收藏过该线路
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void isFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {
        // 1.获取线路 id
        String rid = request.getParameter("rid")  ;

        // 2.获取当前登录用户的 user
        User user = (User) request.getSession().getAttribute("user");

        int uid ;  // 用户的id

        if(user == null)  {
            // 用户尚未登录
//            uid = 0 ;
            // 直接返回
            return ;
        } else {
            // 用户已 经登录
            uid = user.getUid() ;
        }

        // 3.调用 FavoriteService 查询是否收藏
        boolean flag = favoriteService.isFavorite(rid, uid);

        // 4.写回客户端
        writeValue(flag,response);

    }

    /**
     *  添加收藏
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void addFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1.获取线路rid
        String rid = request.getParameter("rid");

        // 2.获取当前登录用户的用户
        User user = (User) request.getSession().getAttribute("user");

        int uid ;  // 用户的id

        if(user == null)  {
            // 用户尚未登录
            return ;
        } else {
            // 用户已 经登录
            uid = user.getUid() ;
        }

        // 3. 调用service 添加
        favoriteService.add(rid,uid);



    }


}













