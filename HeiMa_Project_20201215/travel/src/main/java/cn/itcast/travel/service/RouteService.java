package cn.itcast.travel.service;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;

/**
 *  线路 service
 */
public interface RouteService {
    /**
     *  根据类别进行分页查询
     * @param cid
     * @param current
     * @param pageSize
     * @return
     */
    public PageBean<Route> pageQuery(int cid, int current, int pageSize,String rname) ;

    /**
     *  根据用户的不同进行分页查询
     */
    public PageBean<Route> pageQuery_MF(int uid, int current, int pageSize,String rname) ;


    /**
     *  根据id 查询
     * @param rid
     * @return
     */
    public Route findOne(String rid);
}
