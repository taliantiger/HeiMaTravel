package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Route;

import java.util.List;

public interface RouteDao {

    /**
     *  根据cid 查询总记录数
     */
    public int findTotalCount(int cid, String rname) ;

    /**
     *  根据uid 查询收藏总记录数
     */
    public int findTotalCount_MF(int uid,String rname) ;


    /**
     *  根据cid,start,pageSize 查询当前页的数据集合
     */
//    public List<Route> findByPage(int cid, int start, int page, String rname)  ;
    public List<Route> findByPage(int cid, int start, int pageSize, String rname)  ;

    /**
     *  根据uid,start,pageSize 查询当前收藏页的数据集合
     */
    public List<Route> findByPage_MF(int uid, int start, int pageSize, String rname)  ;


    /**
     *  根据id 查询
     * @param rid
     * @return
     */
    public Route findOne(int rid) ;



}
