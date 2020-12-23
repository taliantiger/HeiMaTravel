package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.CategoryDao;
import cn.itcast.travel.dao.impl.CategoryDaoImp;
import cn.itcast.travel.domain.Category;
import cn.itcast.travel.service.CategoryService;
import cn.itcast.travel.util.JedisUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CategoryServiceImp implements CategoryService {

    private CategoryDao categoryDao = new CategoryDaoImp() ;

    public List<Category> findAll() {
        // 缓存的优化
        // 1.从redis 中查询
        // 1.1 获取jedis 客户端
        Jedis jedis = JedisUtil.getJedis() ;

        // 1.2 可使用 sortedset 排序查询
        // 结果返回一个set 集合
        // Set<String> categorys = jedis.zrange("category", 0, -1);

        // 1.3 查询sortedset 中的分数（cid） 和 值 （cname）
        Set<Tuple> categorys = jedis.zrangeWithScores("category", 0, -1);


        List<Category> cs = null ;

        // 2.判断查询的集合是否为空
        if(categorys == null || categorys.size() == 0 ) {
            System.out.println("从数据库查询.....");

            // 3. 如果为空，需要从数据库查询，再将数据存入 redis
            // 3.1 从数据库查询
            cs = categoryDao.findAll();

            // 3.2将集合数据存储到 redis 中的 category 的  key 中
            for (int i = 0 ; i < cs.size() ; i++)  {
                jedis.zadd("category",
                        cs.get(i).getCid(),
                        cs.get(i).getCname()
                )  ;
            }
        }  else {
            System.out.println("从redis 中查询");

            // 数据结构的统一
            /**
             *  需要的是 list 集合，查询的是set 的集合，此时进行转换
             */
            // 如果不为空，将set 的数据存入list
            cs = new ArrayList<Category>() ;
            for (Tuple tuple : categorys ) {
                Category category = new Category();
                // 把名字放入 category 对象中
                category.setCname(tuple.getElement() ) ;
                category.setCid((int)tuple.getScore() );
                cs.add(category) ;
            }
        }
        return cs ;
    }

}
