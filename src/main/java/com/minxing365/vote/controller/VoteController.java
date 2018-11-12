package com.minxing365.vote.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.minxing365.vote.bean.AnswerTable;
import com.minxing365.vote.bean.OptionTable;
import com.minxing365.vote.bean.VoteMainTable;
import com.minxing365.vote.pojo.VoteAndOption;
import com.minxing365.vote.pojo.VoteCount;
import com.minxing365.vote.service.VoteService;
import com.minxing365.vote.util.JnbEsbUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v2/vote")
public class VoteController {
    Logger log = LoggerFactory.getLogger(VoteController.class);

    @Autowired
    private VoteService voteService;
    @Value("${img.location}")
    private String location;

    /**
     * 保存主表
     *
     * @param voteTitle
     * @param createUserNum
     * @param createUserName
     * @param endTime
     * @return
     */
    @RequestMapping(value = "/insertVoteMainTable", method = {RequestMethod.GET})
    public String insertVoteMainTable(@RequestParam String id,
                                      @RequestParam String voteTitle,
                                      @RequestParam String createUserNum,
                                      @RequestParam String createUserName,
                                      @RequestParam String describes,
                                      @RequestParam String remarks,
                                      @RequestParam Long endTime
    ) {
        log.info("insertVoteMainTable parameter : id=" + id + " ; createUserNum=" + createUserNum + "; createUserName" + createUserName + "; endTime" + endTime + ";describes:" + describes + ";remarks:" + remarks+";voteTitle:"+voteTitle);
        JSONObject jsonObject = new JSONObject();

        if (null == endTime || null == id || null == voteTitle || null == createUserName || null == describes || null == remarks ||
                "".equals(voteTitle) || "".equals(createUserNum) || "".equals(createUserName) || "".equals(describes) || "".equals(remarks)) {
            jsonObject.put("message", "参数错误");
            log.error("<<<<<<<<<parameter error ");
            return jsonObject.toJSONString();

        }
        VoteMainTable voteMainTable = new VoteMainTable();
        voteMainTable.setId(id);
        voteMainTable.setVoteTitle(voteTitle);
        voteMainTable.setCreateUserName(createUserName);
        voteMainTable.setCreateUserNum(createUserNum);
        voteMainTable.setEndTime(endTime);
        voteMainTable.setDescribes(describes);
        voteMainTable.setRemarks(remarks);
        voteMainTable.setState(1);
        try {
            Integer result = voteService.insertVoteMainTable(voteMainTable);
            if (null != result && result > 0) {
                log.info("-----insertVoteMainTable : result----"+result);
                jsonObject.put("message", "新增方法成功");
                jsonObject.put("result", result);
            }
        } catch (Exception e) {
            jsonObject.put("message", "<<<<<<新增方法失败");
            jsonObject.put("result", -1);
            log.error("<<<<<<insertVoteMainTable failed", e);

        }
        return jsonObject.toJSONString();
    }

    /**
     * 保存选项表
     *
     * @param optionTable
     * @return
     */
    @RequestMapping(value = "/insertOptionTable", method = {RequestMethod.POST})
    public String insertOptionTable(@RequestBody OptionTable optionTable) {
        log.info("-----" + optionTable.getOptionTitle() + "----" + optionTable.getRemarks() + "-----" + optionTable.getViewUrl() + "----" + optionTable.getVoteId() + "---" + optionTable.getOptionFlag());
        Integer id = voteService.insertOptionTable(optionTable);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        if (id > 0) {
            log.info("-----insertOptionTable invocation succeeded-----");
            jsonObject.put("message", "insertOptionTable invocation succeeded");
            return jsonObject.toJSONString();
        }
        log.error("<<<<<<insertOptionTable failed");
        jsonObject.put("message", "insertOptionTable failed");
        return jsonObject.toJSONString();
    }

    /**
     * 保存答案表
     *
     * @param answerTable
     * @return
     */
    @RequestMapping(value = "/insertAnswerTable", method = {RequestMethod.POST})
    public String insertAnswerTable( AnswerTable answerTable) {
        JSONObject jsonObject = new JSONObject();
        Integer id = voteService.insertAnswerTable(answerTable);
        jsonObject.put("id", id);
        if (id > 0) {
            jsonObject.put("message", "insertAnswerTable invocation succeeded");
            log.info("-------insertAnswerTable invocation succeeded");
            return jsonObject.toJSONString();
        }
        jsonObject.put("message", "insertAnswerTable failed");
        log.error("<<<<<<<insertAnswerTable failed");
        return jsonObject.toJSONString();
    }

    @RequestMapping(value = "/selectVoteAndOption", method = {RequestMethod.GET})
    public String selectVoteAndOption(@RequestParam(defaultValue = "") String id) {
        JSONObject jsonObject = new JSONObject();
        List<VoteAndOption> result = voteService.selectVoteAndOption(id);
        jsonObject.put("list", result);
        if (result.size() > 0) {
            jsonObject.put("message", "selectVoteAndOption  invocation succeeded");
            log.info("-------selectVoteAndOption  invocation succeeded");
            return jsonObject.toJSONString();
        }
        jsonObject.put("message", "selectVoteAndOption  invocation failed");
        log.info("-------selectVoteAndOption  invocation failed");
        return jsonObject.toJSONString();
    }

    /**
     * 查询是否投票
     *
     * @param id
     * @param optionTitle
     * @return
     */
    @RequestMapping(value = "/selectAnswer", method = {RequestMethod.GET})
    public String selectAnswer(@RequestParam String id, @RequestParam String optionTitle) {
        JSONObject jsonObject = new JSONObject();
        AnswerTable result = voteService.selectAnswer(id, optionTitle);
        jsonObject.put("result", result);
        if (null == result) {
            jsonObject.put("message", "查询人员没有投票记录");
            jsonObject.put("result", null);
            log.info("------no voting  record------");
            return jsonObject.toJSONString();
        }
        jsonObject.put("result", result);
        jsonObject.put("message", "已经投票，不能重复投票");
        return jsonObject.toJSONString();
    }

    /**
     * 根据选择表id查询选择表
     *
     * @param id 选择表id
     * @return
     */
    @RequestMapping(value = "/selectOptionTableById", method = {RequestMethod.GET})
    public String selectOptionTableById(@RequestParam Integer id) {
        JSONObject jsonObject = new JSONObject();
        if (null == id) {
            log.error("<<<<<<parameter error");
            jsonObject.put("message", "参数传入错误");
            jsonObject.put("result", -1);
            return jsonObject.toJSONString();
        }
        OptionTable result = voteService.selectOptionTableById(id);
        if (null == result) {
            log.error("<<<<<<selectOptionTableById  invocation failed");
            jsonObject.put("message", "selectOptionTableById 调用失败");
            jsonObject.put("result", null);
            return jsonObject.toJSONString();
        }
        log.info("------selectOptionTableById  invocation succeeded");
        jsonObject.put("message", "selectOptionTableById 调用成功");
        jsonObject.put("result", result);
        return jsonObject.toJSONString();
    }

    /**
     * 根据主表id查询选项表信息
     *
     * @param voteId
     * @return
     */
    @RequestMapping(value = "/selectOptionTableByvoteId", method = {RequestMethod.GET})
    public String selectOptionTableByvoteId(@RequestParam(defaultValue = "") String voteId) {
        JSONObject jsonObject = new JSONObject();
        if ("".equals(voteId)) {
            log.error("<<<<<<parameter error");
            jsonObject.put("message", "参数传入错误");
            jsonObject.put("result", -1);
            return jsonObject.toJSONString();
        }
        List<OptionTable> list = voteService.selectOptionTableByvoteId(voteId);
        if (list.size() > 0) {
            log.info("-------selectOptionTableByvoteId  invocation succeeded");
            jsonObject.put("message", "selectOptionTableByvoteId 调用成功");
            jsonObject.put("list", list);
            return jsonObject.toJSONString();
        }
        log.info("<<<<selectOptionTableByvoteId  invocation failed");
        jsonObject.put("message", "selectOptionTableByvoteId 调用失败");
        jsonObject.put("list", list);
        return jsonObject.toJSONString();
    }

    /**
     * 获取id
     *
     * @return
     */
    @RequestMapping(value = "/getId", method = {RequestMethod.GET})
    public String getId() {
        JSONObject jsonObject = new JSONObject();
        String id = voteService.getId();
        jsonObject.put("id", id);
        if (null == id || "".equals(id)) {
            log.info("------getId invocation failed");
            jsonObject.put("message", "获取id失败");
        }
        log.info("------getId invocation succeeded ");
        jsonObject.put("message", "获取id成功");
        return jsonObject.toJSONString();
    }

    /**
     * 根据主键id修改主表
     *
     * @param voteTitle 主题
     * @param endTime 结束时间
     * @param id
     * @return
     */
    @RequestMapping(value = "/updateVoteMainTable", method = {RequestMethod.PUT})
    public String updateVoteMainTable(@RequestParam(defaultValue = "", required = false) String voteTitle, @RequestParam(required = false) Long endTime, @RequestParam String id) {
        JSONObject jsonObject = new JSONObject();
        if (!JnbEsbUtil.isInteger(id)) {
            jsonObject.put("message", "<<<<<<parameter error");
            return jsonObject.toJSONString();
        }
        Integer result = 0;
        try {
            result = voteService.updateVoteMainTable(voteTitle, endTime, id);
            if (result > 0) {
                log.error("------updateVoteMainTable Updated data.");
                jsonObject.put("message", "有数据更新");
            } else {
                log.error("------updateVoteMainTable no updated data.");
                jsonObject.put("message", "没有数据更新");
            }
        } catch (Exception e) {
            log.error("<<<<<<<updateVoteMainTable invocation failed", e);
            jsonObject.put("message", "根据主键id修改主表失败");
        }

        jsonObject.put("result", result);
        return jsonObject.toJSONString();
    }

    /**
     * 根据选择表主键id修改
     *
     * @param optionTitle
     * @param pictureUrl
     * @param viewUrl
     * @param optionFlag
     * @param remarks
     * @param id
     * @return
     */
    @RequestMapping(value = "/updateOptionTable", method = {RequestMethod.PUT})
    public String updateOptionTable(@RequestParam(required = false) String optionTitle, @RequestParam(required = false) String pictureUrl, @RequestParam(required = false) String viewUrl, @RequestParam(required = false) Integer optionFlag, @RequestParam(required = false) String remarks, @RequestParam Integer id) {
        JSONObject jsonObject = new JSONObject();
        if (null == id) {
            jsonObject.put("message", "<<<<<<id parameter null");
            return jsonObject.toJSONString();
        }
        if ("".equals(optionTitle)) {
            optionTitle = null;
        }
        if ("".equals(pictureUrl)) {
            pictureUrl = null;
        }
        if ("".equals(viewUrl)) {
            viewUrl = null;
        }
        if ("".equals(remarks)) {
            remarks = null;
        }
        Integer result = 0;
        try {
            result = voteService.updateOptionTable(optionTitle, pictureUrl, viewUrl, optionFlag, remarks, id);
            if (result > 0) {
                log.error("------Updated data.");
                jsonObject.put("message", "有数据更新");
            } else {
                log.error("------No updated data.");
                jsonObject.put("message", "没有数据更新");
            }
        } catch (Exception e) {
            log.error("<<<<<<<updateVoteMainTable invocation failed", e);
            jsonObject.put("message", "根据选择表id修改主表失败");
        }
        jsonObject.put("result", result);
        return jsonObject.toJSONString();
    }

    /**
     * 发布消息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/publishVote", method = {RequestMethod.PUT})
    public String publishVote(@RequestParam String id) {
        JSONObject jsonObject = new JSONObject();
        Integer result = 0;
        try {
            result = voteService.updateState(id);
            if (result > 0) {
                log.error("------publishVote Updated data.");
                jsonObject.put("message", "有数据更新");
            } else {
                log.error("------publishVote No updated data.");
                jsonObject.put("message", "没有数据更新");
            }
        } catch (Exception e) {
            log.error("<<<<<publishVote filed", e);
            jsonObject.put("message", "发布信息失败");
        }
        jsonObject.put("result", result);
        return jsonObject.toJSONString();
    }

    /**
     * 删除投票
     *
     * @param id 主表id
     * @return
     */
    @RequestMapping(value = "/deleteVote", method = {RequestMethod.DELETE})
    public String deleteVote(@RequestParam String id) {
        JSONObject object = new JSONObject();
        if (!JnbEsbUtil.isInteger(id)) {
            object.put("message", "id 类型错误");
            log.error("<<<<<<parameter error,id==" + id);
            return object.toJSONString();
        }
        String result = null;
        try {
            result = voteService.deleteVote(id);
        } catch (Exception e) {
            log.error("deleteVote 失败", e);
        }

        return result;
    }

    /**
     * 根据选择表id删除选择表
     *
     * @param id 子表id
     * @return
     */
    @RequestMapping(value = "/deleteOptionTableById", method = {RequestMethod.DELETE})
    public String deleteOptionTableById(@RequestParam Integer id) {
        JSONObject object = new JSONObject();
        Integer result = null;
        if (null == id) {
            object.put("message", "参数错误");
            object.put("result", "null");
            log.error("<<<<<<<parameter error");
        }
        try {
            result = voteService.deleteOptionTableById(id);
            if (result > 0) {
                object.put("message", "删除成功");
                object.put("result", result);
            } else {
                object.put("message", "删除数据不存在");
                object.put("result", result);
            }
            log.info("<<<<<<<deleteOptionTableById 操作成功");
        } catch (Exception e) {
            log.error("<<<<<<deleteOptionTableById 操作异常", e);
        }

        return object.toJSONString();
    }

    /**
     * 根据主表id查询 主表信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/selectVoteMainTableById", method = {RequestMethod.GET})
    public String selectVoteMainTableById(@RequestParam String id) {
        JSONObject object = new JSONObject();
        //判断id 是否为数字和不为null
        if (!JnbEsbUtil.isInteger(id)) {
            object.put("message", "参数传入错误");
            log.error("<<<<<parameter error");
            return object.toJSONString();
        }
        try {
            VoteMainTable voteMainTable = voteService.selectVoteMainTableById(id);
            if (null != voteMainTable) {
                log.info("message", "查询消息成功");
                object.put("message", "查询消息成功");
                object.put("voteMainTable", voteMainTable);
            } else {
                log.info("message", "查询消息失败");
                object.put("message", "查询消息失败");
                object.put("voteMainTable", "null");
            }
        } catch (Exception e) {
            log.error("selectVoteMainTableById 方法错误", e);
        }

        return object.toJSONString();
    }

    /**
     * 首页显示
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/selectVoteMainTable", method = {RequestMethod.GET})
    public String selectVoteMainTable(@RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize) {
        JSONObject jsonObject = new JSONObject();
        try {
            List<VoteMainTable> list = voteService.selectVoteMainTable(pageNum, pageSize);
            PageInfo<VoteMainTable> page = new PageInfo(list);

            jsonObject.put("list", list);
            //总页数
            jsonObject.put("pages", page.getPages());
            //总记录数
            jsonObject.put("total", page.getTotal());
        } catch (Exception e) {
            log.error("<<<<<selectVoteMainTable 调用失败", e);
        }
        return jsonObject.toJSONString();
    }

    /**
     * 投票信息查询
     *
     * @param state         发布状态
     * @param createUserNum 行员号
     * @param voteTitle     主题
     * @param pageNum       起始页
     * @param pageSize      每页显示条数
     * @return
     */
    @RequestMapping(value = "/select", method = {RequestMethod.GET})
    public String select(@RequestParam Integer state, @RequestParam String createUserNum, @RequestParam String voteTitle, @RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize) {
        JSONObject jsonObject = new JSONObject();
        try {
            List<VoteCount> list = voteService.select(state, createUserNum, voteTitle, pageNum, pageSize);
            PageInfo<VoteMainTable> page = new PageInfo(list);
            jsonObject.put("list", list);
            //总页数
            jsonObject.put("pages", page.getPages());
            //总记录数
            jsonObject.put("total", page.getTotal());
            //  jsonObject.put( "message","查询成功" );
            log.info("-------select invocation succeeded");
        } catch (Exception e) {
            jsonObject.put("message", "select接口调用异常");
            log.info("-------select invocation failed", e);
        }
        return jsonObject.toJSONString();
    }

    /**
     * app页面显示
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/selectOne", method = {RequestMethod.GET})
    public String selectOne(@RequestParam String id) {
        JSONObject jsonObject = new JSONObject();
        try {
            VoteCount voteCount = voteService.selectOne(id);
            jsonObject.put("voteCount", voteCount);
            jsonObject.put("message", "查询成功");
            log.info("------selectOne 返回结果 voteCount："+voteCount+" ; message:查询成功");
        } catch (Exception e) {
            jsonObject.put("message", "selectOne接口调用异常");
            log.info("-------selectOne invocation failed", e);
        }
        return jsonObject.toJSONString();
    }

    /**
     * app 查询
     * @param optionTitle 选择表 主题
     * @param pageNum 起始页
     * @param pageSize 每页条数
     * @return
     */
       @RequestMapping(value = "/selectOptionTableByTitle", method = {RequestMethod.GET})
      public String selectOptionTableByTitle(@RequestParam(defaultValue = "") String optionTitle,@RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "6") Integer pageSize){
              log.info("--selectOptionTableByTitle---optionTitle="+optionTitle+";pageNum="+pageNum+" ;pageSize="+pageSize);
             String result=null;
           try {
             result=  voteService.selectOptionTableByTitle(optionTitle,pageNum,pageSize);
           }catch (Exception e){
                 log.error("<<<<调用app查询错误",e);
           }

        return result;
      }

    /**
     * 上传
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject upload(MultipartFile pic, HttpServletRequest request, HttpServletResponse response) throws Exception {
        JSONObject result = new JSONObject();
        File directory = new File("");// 参数为空
        String imgPath = directory.getCanonicalPath() + File.separator + "img" + File.separator;
        log.info("------" + imgPath + "-------");
        if (pic == null || pic.getSize() == 0) {
            log.error("<<<<<<<<<图片传入错误");
            result.put("msg", "图片传入错误");
            return result;
        }
        File file = new File(imgPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        FileOutputStream fileOutputStream = null;
        try {

            //获取文件名和后缀
            String filename = UUID.randomUUID().toString().replaceAll("-", "") + ".png";
            String returnUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/img/" + filename;
            String url = imgPath + filename;
            fileOutputStream = new FileOutputStream(new File(file, filename));
            FileCopyUtils.copy(pic.getInputStream(), fileOutputStream);
            result.put("msg", "OK");
            result.put("picUrl", returnUrl);
            return result;
        } finally {
            try {
                fileOutputStream.close();
            } catch (Exception e) {
            }
            try {
                pic.getInputStream().close();
            } catch (Exception e) {
            }
        }
    }
}
