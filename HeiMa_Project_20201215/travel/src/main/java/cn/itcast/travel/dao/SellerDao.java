package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Seller;

public interface SellerDao {

    /**
     *  根据 id 查询 卖家对象
     * @param id
     * @return
     */
    public Seller findById(int id) ;
}
