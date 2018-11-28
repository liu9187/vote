package com.minxing365.vote.service;

import com.minxing365.vote.pojo.VoteDetailsVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * service服务
 */
public interface VotingDetailsService {
    /**
     * 根据地查询详情
     * @param name
     * @return
     */
    String selectVotingDetailsByName(@Param("name") String name);

}
