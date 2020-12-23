package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class RouteDaoImp implements RouteDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource() ) ;

    @Override
    public int findTotalCount(int cid, String rname) {
//        String sql = "select count(*) from tab_route where cid = ?" ;

        // 1.定义sql 模板
        String sql = "select count(*) from tab_route where 1=1 ";
        StringBuilder sb = new StringBuilder(sql) ;

        // 有各种各样的情况，此次构建一个list 集合
        List params = new ArrayList() ;   // 条件们

        // 2.判断参数是否有值
        if(cid != 0) {
            sb.append(" and cid = ? ") ;

            // 当有cid 的时候，加上一个条件
            params.add(cid) ; // 添加 ? 对应的cid 的值
        }

        if(rname != null && rname.length() > 0 && !"null".equals(rname) )  {
            sb.append(" and rname like ? ");
            params.add("%"+rname+"%") ;
        }

        // RouteDao
//        if("null".equals(rname)) {
//            sb.append(" and rname like ? ");
//            params.add("%") ;
//        }

        sql = sb.toString() ;

        return template.queryForObject(
                    sql,
                    Integer.class,

                    // 传参传入一个Object 的参数
                    params.toArray()
        );
    }


    /**
     *  根据uid 查询总收藏数
     * @param uid
     * @param rname
     * @return
     */
    @Override
    public int findTotalCount_MF(int uid, String rname) {

        // 1.定义sql 模板
        String sql = "select count(*) from tab_favorite where 1=1 ";
        StringBuilder sb = new StringBuilder(sql) ;

        // 有各种各样的情况，此次构建一个list 集合
        List params = new ArrayList() ;   // 条件们

        // 2.判断参数是否有值
        if(uid != 0) {
            sb.append(" and uid = ? ") ;

            // 当有uid 的时候，加上一个条件
            params.add(uid) ; // 添加 ? 对应的cid 的值
        }

        if(rname != null && rname.length() > 0 && !"null".equals(rname) )  {
            sb.append(" and rname like ? ");
            params.add("%"+rname+"%") ;
        }

        // RouteDao
//        if("null".equals(rname)) {
//            sb.append(" and rname like ? ");
//            params.add("%") ;
//        }

        sql = sb.toString() ;

        return template.queryForObject(
                sql,
                Integer.class,

                // 传参传入一个Object 的参数
                params.toArray()
        );

    }


    @Override
    public List<Route> findByPage(int cid, int start, int pageSize, String rname) {
//        String sql = "select * from tab_route where cid = ? and rname like ? limit ? , ?" ;

        String sql = "select * from tab_route where 1=1 ";
        StringBuilder sb = new StringBuilder(sql) ;
        List params = new ArrayList() ;   // 条件们

        // 2.判断参数是否有值
        if(cid != 0) {
            sb.append( " and cid = ? ") ;
            params.add(cid) ; // 添加 ? 对应的值
        }

        if(rname != null && rname.length() > 0 && !"null".equals(rname) )  {
            sb.append(" and rname like ? ")  ;

            // 下面注释部分是错误的写法，因为 这样等价于'%西安% ' ，多了一个空格，导致sql 语句失效
//            params.add("%"+rname+"% ") ;
            params.add("%"+rname+"%") ;
        }

//        if(rname.equals("null")) {
//            sb.append(" and rname like ? ")  ;
//            // 如果 rname 原本就是字符串 null , 则让 rname 匹配 like "%" .
//            params.add("%") ;
//        }

        // 分页条件
        sb.append(" limit ? , ? ") ;

        sql = sb.toString() ;

        // 额外添加params 中的 start 、 pageSize，对应 " limit ? , ? " 中的两个问号
        // 第一个问号：起始的位置， 第二个问号：每页需要显示的条目数。
        params.add(start) ;
        params.add(pageSize) ;

        return template.query(sql,
                            new BeanPropertyRowMapper<Route>(Route.class),
//                            cid ,
//                            start,
//                            pageSize

                            // 将 params 集合转为数组 toArray()
                            // 变为只需要传递一个params.toArray() 对象了
                            params.toArray()
        );
    }


    /**
     *  根据 uid, start, pageSize  查询当前收藏页的数据集合
     * @param uid
     * @param start
     * @param pageSize
     * @param rname
     * @return
     */
    @Override
    public List<Route> findByPage_MF(int uid, int start, int pageSize, String rname) {

        String sql = "select * from tab_route where 1=1 ";
        StringBuilder sb = new StringBuilder(sql) ;
        List params = new ArrayList() ;   // 条件们

        // 2.判断参数是否有值
        if(uid != 0) {
            sb.append( " and rid in (select rid from tab_favorite where uid = ?) ") ;
            params.add(uid) ; // 添加 ? 对应的值
        }

        if(rname != null && rname.length() > 0 && !"null".equals(rname) )  {
            sb.append(" and rname like ? ")  ;

            // 下面注释部分是错误的写法，因为 这样等价于'%西安% ' ，多了一个空格，导致sql 语句失效
//            params.add("%"+rname+"% ") ;
            params.add("%"+rname+"%") ;
        }

//        if(rname.equals("null")) {
//            sb.append(" and rname like ? ")  ;
//            // 如果 rname 原本就是字符串 null , 则让 rname 匹配 like "%" .
//            params.add("%") ;
//        }

        // 分页条件
        sb.append(" limit ? , ? ") ;

        sql = sb.toString() ;

        // 额外添加params 中的 start 、 pageSize，对应 " limit ? , ? " 中的两个问号
        // 第一个问号：起始的位置， 第二个问号：每页需要显示的条目数。
        params.add(start) ;
        params.add(pageSize) ;

        return template.query(sql,
                new BeanPropertyRowMapper<Route>(Route.class),
//                            cid ,
//                            start,
//                            pageSize

                // 将 params 集合转为数组 toArray()
                // 变为只需要传递一个params.toArray() 对象了
                params.toArray()
        );
    }


    /**
     *  根据id 查询
     * @param rid
     * @return
     */
    @Override
    public Route findOne(int rid) {
        String sql = "select * from tab_route where rid = ?";
        return template.queryForObject(sql,new BeanPropertyRowMapper<Route>(Route.class),rid);
    }
}
