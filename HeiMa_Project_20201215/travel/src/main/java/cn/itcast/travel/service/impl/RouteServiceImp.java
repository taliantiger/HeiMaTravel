package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.dao.RouteImgDao;
import cn.itcast.travel.dao.SellerDao;
import cn.itcast.travel.dao.impl.FavoriteDaoImp;
import cn.itcast.travel.dao.impl.RouteDaoImp;
import cn.itcast.travel.dao.impl.RouteImgDaoImp;
import cn.itcast.travel.dao.impl.SellerDaoImp;
import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.RouteImg;
import cn.itcast.travel.domain.Seller;
import cn.itcast.travel.service.RouteService;

import java.util.List;

public class RouteServiceImp implements RouteService {
    private RouteDao routeDao = new RouteDaoImp() ;

    private RouteImgDao routeImgDao = new RouteImgDaoImp() ;

    private SellerDao sellerDao = new SellerDaoImp() ;

    private FavoriteDao favoriteDao = new FavoriteDaoImp() ;

    @Override
    public PageBean<Route> pageQuery(int cid, int currentPage, int pageSize,String rname) {
        // 封装PageBean
        PageBean<Route> pb = new PageBean<Route>()  ;

        // 设置当前页码
        pb.setCurrentPage(currentPage);

        // 设置每页显示条数
        pb.setPageSize(pageSize) ;

        // 设置总的记录数
        int totalCount = routeDao.findTotalCount(cid,rname) ;
        pb.setTotalCount(totalCount);

        // 设置当前页显示的数据集合
        int start = (currentPage - 1) * pageSize;  // 开始记录数
        List<Route> list = routeDao.findByPage(cid,start,pageSize,rname) ;
        pb.setList(list) ;

        // 设置总页数 = 总记录数 / 每页显示条数
        int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize : totalCount / pageSize + 1 ;
        pb.setTotalPage(totalPage) ;

        return pb;
    }

    /**
     *  根据用户进行分页查询
     * @param uid
     * @param currentPage
     * @param pageSize
     * @param rname
     * @return
     */
    // 利用Dao类中获得的数据，进行相应参数的设置
    @Override
    public PageBean<Route> pageQuery_MF(int uid, int currentPage, int pageSize, String rname) {
        // 封装PageBean
        PageBean<Route> pb = new PageBean<Route>()  ;

        // 设置当前页码
        pb.setCurrentPage(currentPage);

        // 设置每页显示条数
        pb.setPageSize(pageSize) ;

        // 设置总的记录数
        int totalCount = routeDao.findTotalCount_MF(uid,rname) ;
        pb.setTotalCount(totalCount);

        // 设置当前页显示的数据集合
        int start = (currentPage - 1) * pageSize;  // 开始记录数
        List<Route> list = routeDao.findByPage_MF(uid,start,pageSize,rname) ;
        pb.setList(list) ;

        // 设置总页数 = 总记录数 / 每页显示条数
        int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize : totalCount / pageSize + 1 ;
        pb.setTotalPage(totalPage) ;

        return pb;
    }

    @Override
    public Route findOne(String rid) {
        // 1.根据id 去route 表中查询route 对象
        Route route = routeDao.findOne(Integer.parseInt(rid)) ;

        // 2.根据 route 的id 查询图片的集合信息
        List<RouteImg> routeImgList= routeImgDao.findByRid(route.getRid() ) ;

        // 2.2将集合设置到route 对象
        route.setRouteImgList(routeImgList);

        // 3.根据route 的sid 查询卖家的信息，查询商家的对象
        Seller seller = sellerDao.findById(route.getSid());
        // 同时还把seller 对象放入 route对象中
        route.setSeller(seller) ;

        // 4.查询收藏次数
        int count = favoriteDao.findCountByRid(route.getRid() ) ;
        route.setCount(count) ;


        return route;
    }
}
