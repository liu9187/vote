package com.minxing365.vote.dao;
import com.minxing365.vote.bean.Oauth2AccessToken;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;


/**
 * Created by SZZ on 2017/5/10.
 */
@Component
public interface UserMapper {
    /**
     * 查询用户
     * @param accountId
     * @param networkId
     * @return
     */
    @Select("SELECT id FROM users WHERE network_id = #{networkId} AND account_id = #{accountId} AND actived=1 limit 1")
    Integer findUidByAccountIdAndNetWorkId(@Param("accountId") Long accountId, @Param("networkId") Integer networkId);

    //    @Results({@Result(column = "account_id", property = "accountId"), @Result(column = "expires_at", property = "expiredTime")})
    @Select("SELECT account_id as accountId ,expires_at as expiredTime from oauth2_access_tokens " +
            "WHERE token = #{token} " +
            "ORDER BY expires_at DESC " +
            "LIMIT 1")
    Oauth2AccessToken findAccountByToken(@Param("token") String token);

    /**
     * 查询社区管理员的表
     * @param nd
     * @param accountId
     * @return
     */
    @Select("SELECT\n" + "\tid\n" + "FROM\n" + "\tuser_network_adminnings\n" + "WHERE\n"
            + "\tuser_id = (\n" + "\t\tSELECT\n" + "\t\t\tid\n" + "\t\tFROM\n" + "\t\t\tusers\n"
            + "\t\tWHERE\n" + "\t\t\tnetwork_id = #{nd}\n" + "\t\tAND account_id = #{accountId}\n" + "\t)\n"
            + "AND network_id = #{nd}")
    Integer checkNetWorkAdmin(@Param("nd") String nd, @Param("accountId") Long accountId);

    /**
     * 获取登录用户名字
     * @param uid
     * @return
     */
    @Select("SELECT `name` FROM users WHERE id=#{uid} LIMIT 1;")
    String getUserName(@Param("uid") String uid);

}
