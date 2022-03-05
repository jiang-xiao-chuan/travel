package cn.itcast.travel.service;


import cn.itcast.travel.domain.User;

public interface UserService {

    /**
     * 用户注册
     * @param user
     * @return
     */
    boolean register(User user);


    /**
     * 用户激活
     * @param code
     * @return 布尔值判断是否激活
     */
    boolean active(String code);

    /**
     * 用户登录
     * @param user
     * @return 从数据库中查询出来的真正的用户
     */
    User login(User user);
}
