package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Route;

import java.util.List;

public interface  RouteDao {

    /**
     * 查询总记录数
     * @param cid
     * @return
     */
    int findTotalCount(int cid,String rname);

    /**
     * 查询list集合
     * @param cid
     * @param start
     * @param pageSize
     * @return
     */
    List<Route> findByPage(int cid, int start, int pageSize,String rname);

    /**
     * 根据id查询route对象
     * @param rid
     * @return
     */
    Route findOne(int rid);
}
