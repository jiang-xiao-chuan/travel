package cn.itcast.travel.service.impl;


import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.dao.impl.UserDaoImpl;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.util.MailUtils;
import cn.itcast.travel.util.UuidUtil;

public class UserServiceImpl implements UserService {

    private UserDao dao = new UserDaoImpl();

    /**
     * 注册用户
     * @param user
     * @return
     */
    @Override
    public boolean register(User user) {

        //1.根据用户名查询用户对象
        User u  = dao.findByUsername(user.getUsername());
        //判断u是否为空
        if (u != null){
            //用户名存在，注册失败
            return false;
        }

        //2.保存用户信息
        //2.1设置激活码，唯一字符串
        user.setCode(UuidUtil.getUuid());
        //2.2设置激活状态
        user.setStatus("N");
        dao.save(user);


        //3.激活邮件发送，邮件正文
        String content ="<a href='http://localhost/travel/user/active?code="+user.getCode()+"'>点击激活【黑马旅游网】</a>";
        MailUtils.sendMail(user.getEmail(),content,"激活邮件");
        return true;
    }

    /**
     * 激活的方法
     * @param code
     * @return 返回是否成功激活的状态
     */
    @Override
    public boolean active(String code) {
        //1.根据激活码查询User对象
        User user =  dao.findUserByCode(code);
        //判空操作
        if (user != null){
            //2.调用dao的修改激活状态的方法
            dao.updateStatus(user);
            return true;
        }else {
            return false;
        }

    }

    /**
     *
     * @param user
     * @return 通过传入的用户名和密码查询到的数据库中的用户
     */
    @Override
    public User login(User user) {
        return dao.findByUsernameAndPassword(user.getUsername(), user.getPassword());
    }
}
