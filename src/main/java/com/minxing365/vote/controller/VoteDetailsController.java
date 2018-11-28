package com.minxing365.vote.controller;

import com.minxing365.vote.service.VotingDetailsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/vote")
@Api("投票详情api文档")
public class VoteDetailsController {
    @Autowired
    VotingDetailsService votingDetailsService;

    /**
     * 根据id查询数据
     * @param name 投票主题
     * @return
     */
    @ApiOperation("根据id查询数据")
    @ApiImplicitParams(@ApiImplicitParam(paramType = "query",name = "name",dataType = "String",required = true,value = "投票主题"))
    @RequestMapping(value = "/selectVotingDetailsById", method = {RequestMethod.GET})
    public String selectVotingDetailsById(@RequestParam String name) {

        return votingDetailsService.selectVotingDetailsByName(name);
    }
}
