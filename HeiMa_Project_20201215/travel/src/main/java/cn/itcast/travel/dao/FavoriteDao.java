package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Favorite;

public interface FavoriteDao {

    /**
     *  根据一个rid 和 uid 查询收藏信息
     *  返回一个Favorite 对象
     * @param rid
     * @param uid
     * @return
     */
    public Favorite findByRidAndUid(int rid, int uid)  ;


    /**
     *  根据线路 rid 查询收藏次数
     * @param rid
     * @return
     */
    public int findCountByRid(int rid);


    /**
     *  添加收藏
     * @param rid
     * @param uid
     */
    public void add(int rid, int uid);
}
