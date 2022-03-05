package cn.itcast.travel.service;

public interface FavoriteService {

    /**
     * 用户是否收藏路线
     * @param rid
     * @param uid
     * @return
     */
    boolean isFavorite(String rid, int uid);


    /**
     * 添加收藏
     * @param rid
     * @param uid
     */
    void add(String rid, int uid);
}
