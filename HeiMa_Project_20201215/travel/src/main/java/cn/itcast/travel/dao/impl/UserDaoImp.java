package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

public class UserDaoImp implements UserDao {

    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource() ) ;

    @Override
    public User findByUsername(String username) {
        User user = null ;
        // 处理异常，防止保存失败。
        try {
            // 1.定义sql
            String sql = "select * from  tab_user where username = ?";

            // 2.执行sql
            user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), username);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        // 如果存储失败，则捕捉了异常，同时user 自动为null,也不会报错。
        return user;
    }

    @Override
    public void save(User user) {
        // 1.定义sql
        // values() user 对象里的数据,即键值
        String sql = "insert into tab_user(username,password,name,birthday,sex,telephone,email,status,code)" +
                "values(?,?,?,?,?,?,?,?,?)";

        // 2.执行sql
        template.update(sql,user.getUsername(),
                user.getPassword(),
                user.getName(),
                user.getBirthday(),
                user.getSex(),
                user.getTelephone(),
                user.getEmail(),
                user.getStatus(),
                user.getCode()
        ) ;
    }


    /**
     *  通过 Code 来寻找对应的用户名
     * @param code
     * @return
     */
    @Override
    public User findByCode(String code) {
        String sql = "select * from tab_user where  code = ?" ;
        User user = null ;
        // 如果传入为null ,可能有异常，因此这里 try catch 一下
        try  {
            // 知道传入的code对象，要么有一个，要么没有
            // 以下语句，导入 sql 的同时， 还声明 ？ 的具体代表含义
            user = template.queryForObject(sql,new BeanPropertyRowMapper<User>(User.class), code) ;
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return user;
    }


    /**
     *  更新user 中 status 的值，从而更改用户激活状态
     * @param user
     */
    @Override
    public void updateStatus(User user) {
        String sql = "update tab_user set status = 'Y' where uid = ?" ;
        template.update(sql,user.getUid() ) ;
    }


    /**
     *
     *  根据用户名和密码查询的方法
     * @param username
     * @param password
     * @return
     */
    @Override
    public User findByUsernameAndPassword(String username, String password) {
        User user = null ;
        // 处理异常，防止保存失败。
        try {
            // 1.定义sql
            String sql = "select * from  tab_user where username = ? and password = ?";

            // 2.执行sql
            // 对两个问号 ？ 分别匹配对应的 传入的实参。
            user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), username, password);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        // 如果存储失败，则捕捉了异常，同时user 自动为null,也不会报错。
        return user;
    }
}
