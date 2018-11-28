package com.minxing365.vote.dao;

import com.minxing365.vote.pojo.VoteDetailsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 投票详情
 */
@Mapper
@Component
public interface VotingDetailsMapper {
    /**
     * 根据id查询投票详情
     * @param name 投票主题
     * @return
     */
    @Select("SELECT  sf.id ,sf.app_id AS appId,si.user_id AS userId,sa.`name` ,sf.body FROM survey_forms sf ,survey_apps sa,survey_instance si WHERE sf.app_id=sa.id AND sa.id=si.app_id AND sa.`name` LIKE #{name} GROUP BY sf.id;;")
    List<VoteDetailsVo> selectVotingDetailsByName(@Param("name") String name);

}
