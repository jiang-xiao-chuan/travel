package cn.itcast.travel.dao;


import cn.itcast.travel.domain.User;

public interface UserDao {


    /**
     * 根据用户名查询用户信息
     * @param username
     * @return
     */
    User findByUsername(String username);

    /**
     * 保存用户功能
     * @param user
     */
    void save(User user);

    /**
     * 根据激活码查询用户
     * @param code
     * @return 用户
     */
    User findUserByCode(String code);

    /**
     * 更新数据库中user的status状态
     * @param user
     */
    void updateStatus(User user);

    /**
     *
     * @param username
     * @param password
     * @return 查询出来的用户对象
     */
    User findByUsernameAndPassword(String username, String password);
}
