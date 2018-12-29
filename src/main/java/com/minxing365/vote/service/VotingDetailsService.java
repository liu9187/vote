package com.minxing365.vote.service;

import com.alibaba.fastjson.JSONArray;
import com.minxing365.vote.pojo.ResultVo;
import com.minxing365.vote.pojo.VoteDetailsVo;
import com.minxing365.vote.pojo.excel.CountExcel;
import org.apache.ibatis.annotations.Param;

import java.io.UnsupportedEncodingException;
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

    /**
     * 获取信息采集的 需要的信息
     * @return
     */
    List<CountExcel> selectVotingDetails2ByName() throws UnsupportedEncodingException;

}
