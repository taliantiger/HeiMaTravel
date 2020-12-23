package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.dao.impl.UserDaoImp;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.util.MailUtils;
import cn.itcast.travel.util.UuidUtil;

public class UserServiceImp implements UserService {

    private UserDao userDao = new UserDaoImp() ;

    /**
     *  注册用户
     * @param user
     * @return
     */
    @Override
    public boolean regist(User user) {
        // 1.根据用户名查询用户对象
        User u = userDao.findByUsername(user.getUsername());

        // 判断u是否为null
        if(u != null ) {
            // u != null
            // 用户名存在，注册失败
            return  false ;
        }

        // u == null
        // 用户名不存在，可以注册
        // 2.保存用户信息
        // 2.1 设置激活码，唯一字符串
        user.setCode(UuidUtil.getUuid() ) ;

        // 2.2 设置激活状态
        user.setStatus("N");

        userDao.save(user) ;

        // 3.激活邮件发送
        String content = "<a href='http://localhost/travel/user/active?code="+user.getCode()+"'>点击激活【黑马旅游网】</a>" ;

        MailUtils.sendMail(user.getEmail(),content,"激活邮件") ;
        return  true ;
    }


    /**
     *  点击邮箱链接后，正式激活用户
     * @param code
     * @return
     */
    @Override
    public boolean active(String code) {
        // 1.根据传入的激活码查询用户对象
        User user = userDao.findByCode(code)  ;
        if(user != null) {
            userDao.updateStatus(user) ;
            return true ;
        } else {
            // 2.调用dao 的修改激活状态的方法
            return  false ;

        }

//        return false;
    }


    /**
     *  登录方法
     *  根据用户名和密码查询的方法
     * @param user
     * @return
     */
    @Override
    public User login(User user) {

        return userDao.findByUsernameAndPassword(
                user.getUsername(),
                user.getPassword()
        );
    }
}
