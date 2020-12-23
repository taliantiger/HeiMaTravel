package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.SellerDao;
import cn.itcast.travel.domain.RouteImg;
import cn.itcast.travel.domain.Seller;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class SellerDaoImp implements SellerDao {

    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource() ) ;

    // 这里传的是id
    @Override
    public Seller findById(int id) {
        String sql = "select * from tab_seller where sid = ?";
        // 查询的是一个，所以此时调用queryForObject()
        return template.queryForObject(sql,new BeanPropertyRowMapper<Seller>(Seller.class),id );
    }
}
