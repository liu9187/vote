package com.minxing365.vote.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.alibaba.fastjson.JSONObject;
import com.minxing365.vote.dao.UserMapper;
import com.minxing365.vote.pojo.excel.CountExcel;
import com.minxing365.vote.service.VotingDetailsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v2/vote")
@Api("信息采集统计api文档")
public class VoteDetailsController {
    private Logger log = LoggerFactory.getLogger(VoteDetailsController.class);
    @Autowired
    VotingDetailsService votingDetailsService;
    @Autowired
    UserMapper userMapper;
    @Value("${name}")
    private String name;

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
     * 信息采集数据
     *
     * @return
     */
    @ApiOperation("信息采集统计")
    @ApiImplicitParams(@ApiImplicitParam(paramType = "query", name = "sign", dataType = "String", defaultValue = "0",value = "下载excel标识"))
    @RequestMapping(value = "/selectVotingDetails2ByName", method = {RequestMethod.GET})
    public String selectVotingDetails2ByName(HttpServletRequest request, @RequestParam(defaultValue = "0", name = "sign",required = false) Integer sign) {
        JSONObject object = new JSONObject();
        try {
            List<CountExcel> list = votingDetailsService.selectVotingDetails2ByName();
            object.put("list", list);
            String uid = String.valueOf(request.getSession().getAttribute("uid"));
          //  uid="137";
            if (null != uid) {
                String userName = userMapper.getUserName(uid);
                object.put("userName", userName);
            }
//            List<CountExcel> _list = new ArrayList<>();
//            _list.addAll(list);
            //ExcelExportUtil 输出数据的时候 removelist 中的数据
            if (sign == 1) {
                try {
                    Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("信息采集", "信息统计"), CountExcel.class, list);
                    FileOutputStream fo = new FileOutputStream("D:/信息采集"+"_"+System.currentTimeMillis()+".xls");
                    workbook.write(fo);
                    workbook.close();
                    fo.close();
                    object.put("message", "数据导入成功");
                } catch (Exception e) {
                    log.error("<<<<<<导出数据出现异常", e);
                }

            }
        } catch (Exception e) {
            log.error("<<<<<<<selectVotingDetails2ByName调用出现错误", e);
        }
        return object.toJSONString();
    }


}
