package com.minxing365.vote.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.minxing365.vote.pojo.ResultVo;
import com.minxing365.vote.service.VotingDetailsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v2/vote")
@Api("投票详情api文档")
public class VoteDetailsController {
    Logger log = LoggerFactory.getLogger(VoteDetailsController.class);
    @Autowired
    VotingDetailsService votingDetailsService;

    /**
     * 根据name查询数据
     *
     * @param name 投票主题
     * @return
     */
    @ApiOperation("根据id查询数据")
    @ApiImplicitParams(@ApiImplicitParam(paramType = "query", name = "name", dataType = "String", required = true, value = "投票主题"))
    @RequestMapping(value = "/selectVotingDetailsByName", method = {RequestMethod.GET})
    public String selectVotingDetailsByName(@RequestParam String name) {
        log.info("------根据投票主题查询相关数据 参数：name=" + name);
        String result = null;
        try {
            result = votingDetailsService.selectVotingDetailsByName(name);
        } catch (Exception e) {
            log.error("<<<<<<<selectVotingDetailsByName调用出现错误", e);
        }
        return result;
    }

    /**
     * 根据name查询数据
     *
     * @param name 投票主题
     * @return
     */
    @ApiOperation("信息采集统计")
    @ApiImplicitParams(@ApiImplicitParam(paramType = "query", name = "name", dataType = "String", required = true, value = "信息采集主题"))
    @RequestMapping(value = "/selectVotingDetails2ByName", method = {RequestMethod.GET})
    public String selectVotingDetails2ByName(@RequestParam String name) {
        log.info("------信息采集主题查询相关数据 参数：name=" + name);
        JSONObject object = new JSONObject();
        try {
            JSONArray list =votingDetailsService.selectVotingDetails2ByName(name);
            object.put("list",list);
        } catch (Exception e) {
            log.error("<<<<<<<selectVotingDetails2ByName调用出现错误", e);
        }
        return object.toJSONString();
    }
}
