package com.minxing365.vote.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.minxing365.vote.bean.AnswerTable;
import com.minxing365.vote.bean.OptionSublistTable;
import com.minxing365.vote.bean.OptionTable;
import com.minxing365.vote.bean.VoteMainTable;
import com.minxing365.vote.dao.VoteMapper;
import com.minxing365.vote.pojo.AnswerCount;
import com.minxing365.vote.pojo.VoteAndOption;
import com.minxing365.vote.pojo.VoteCount;
import com.minxing365.vote.service.TimerService;
import com.minxing365.vote.service.VoteService;
import com.minxing365.vote.util.JnbEsbUtil;
import com.minxing365.vote.util.PageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class VoteServiceImpl implements VoteService {
    Logger log = LoggerFactory.getLogger(VoteServiceImpl.class);
    @Autowired
    private VoteMapper voteMapper;
    @Autowired
    private TimerService timerService;
    @Value("${defaultVale}")
    private int defaultVale;

    @Override
    @Transactional
    public String insertVoteMainTable(VoteMainTable voteMainTable) {
        //根据id查询主表内容
        //  VoteMainTable voteMainTable=null;
        String id = null;
        try {
            //   voteMainTable= voteMapper.selectVoteMainTableById( id );
            //新增主表
            Integer re = voteMapper.insertVoteMainTable(voteMainTable);
            if (re > 0) {
                log.info("----新增主表成功----");
                //    String voteTitle = voteMainTable.getVoteTitle();
                //接口进行发布
//                Integer result = null;
//                if (null != voteMainTable.getId()) {
//                    result = voteMapper.updateState(voteMainTable.getId());
//                } else {
//                    log.error("<<<<<主表发布失败");
//                    return null;
//                }
                //获取id
                id = voteMainTable.getId();
//                if (result > 0) {
//                    //状态更新成功，可以发布消息
//                    // 发布消息接口
//                    log.info("-------消息推送开始");
//                    Thread thread = new Thread(() -> PushMessage.sendOcuMessageToUsers("投票通知", voteTitle, id));
//                    thread.start();
//                }
            } else {
                log.error("<<<<<主表新增异常");
                return null;
            }
        } catch (Exception e) {
            log.error("发布消息出现异常", e);
        }
        return id;
    }

    @Override
    @Transactional
    public Integer insertOptionTable(OptionTable optionTable) {
        voteMapper.insertOptionTable(optionTable);
        Integer id = optionTable.getId();
        return id;
    }

    @Override
    @Transactional
    public Integer insertAnswerTable(AnswerTable answerTable) {
        voteMapper.insertAnswerTable(answerTable);
        Integer id = answerTable.getId();
        return id;
    }

    @Override
    public List<VoteAndOption> selectVoteAndOption(String id) {
        return voteMapper.selectVoteAndOption(id);
    }

    @Override
    public AnswerTable selectAnswer(String id, String optionTitle) {
        return voteMapper.selectAnswer(id, optionTitle);
    }

    @Override
    public OptionTable selectOptionTableById(Integer id) {
        return voteMapper.selectOptionTableById(id);
    }

    @Override
    public VoteMainTable selectVoteMainTableById(String id) {
        return voteMapper.selectVoteMainTableById(id);
    }

    @Override
    public List<OptionTable> selectOptionTableByvoteId(String voteId) {
        return voteMapper.selectOptionTableByvoteId(voteId);
    }

    @Override
    public String getId() {
        String id = JnbEsbUtil.CreateMsgId();
        return id;
    }

    @Override
    @Transactional
    public Integer updateVoteMainTable(String voteTitle, String describes, String remarks, Long startTime, Long endTime, String id) {
        return voteMapper.updateVoteMainTable(voteTitle, describes, remarks, startTime, endTime, id);
    }

    @Override
    @Transactional
    public Integer updateOptionTable(String optionTitle, String pictureUrl, String remarks, String department, Integer id) {
        return voteMapper.updateOptionTable(optionTitle, pictureUrl, remarks, department, id);
    }

    @Override
    @Transactional
    public Integer updateState(String id) {
        Integer result = voteMapper.updateState(id);
        log.info("------id==" + id);
        //根据id查询主表内容
        VoteMainTable voteMainTable = null;
        log.info("----result1=" + result);
        try {
            voteMainTable = voteMapper.selectVoteMainTableById(id);
        } catch (Exception e) {
            log.error("<<<<<<查询主表信息失败", e);
        }
        if (result > 0) {
            String voteTitle = voteMainTable.getVoteTitle();
            Long endTime = voteMainTable.getEndTime();
            log.info("-------消息推送开始");
            //发布消息接口
            Thread thread = new Thread(() -> PushMessage.sendOcuMessageToUsers("投票通知", voteTitle, id, endTime));
            thread.start();
            //开启定时任务

            try {
                timerService.overdueVotingService(id, endTime);
            } catch (Exception e) {
                log.error("<<<<<<<发布消息定时任务异常", e);
            }


        }

        return result;
    }

    @Override
    @Transactional
    public String deleteVote(String id) {
        JSONObject object = new JSONObject();
        try {
            //删除主表
            Integer main = voteMapper.deleteVoteMainTable(id);
            //删除选择表
            if (main > 0) {
                //判断是不是存在选择表
                List<OptionTable> list = voteMapper.selectOptionTableByvoteId(id);
                if (list.size() > 0) {
                    Integer in = voteMapper.deleteOptionTable(id);
                    if (in > 0) {
                        //判断选择表字表是否有数据
                        for (int i = 0; i < list.size(); i++) {
                            voteMapper.updateSublist(list.get(i).getId());
                        }
                    }

                }

                object.put("message", "删除成功");
                log.info("-----删除成功");
            } else {
                log.info("-----投票已发布或投票不存在");
                object.put("message", "投票已发布或投票不存在");
            }
        } catch (Exception e) {
            log.error("<<<<<<delete failed ", e);
        }
        return object.toJSONString();
    }

    /**
     * @param id
     * @return
     */
    @Override
    @Transactional
    public Integer deleteOptionTableById(Integer id) {
        return voteMapper.deleteOptionTableById(id);
    }


    /**
     * 统计查询
     *
     * @param state         状态
     * @param createUserNum 行员号
     * @param voteTitle     主题
     * @param pageNum
     * @param pageSize
     * @return
     */

    @Override
    public List<VoteCount> select(Integer state, String createUserNum, String voteTitle, Integer pageNum, Integer pageSize, Integer pageNum1, Integer pageSize1) {
        String voteTitleStr = "%" + voteTitle + "%";
        log.info("----状态 ：state==" + state + ";行员号：createUserNum ==" + createUserNum + "; 主题：voteTitle==" + voteTitle + ";模糊条件 ：voteTitleStr " + voteTitleStr);
        List<VoteCount> countList = new ArrayList<>();
        try {
            //分页插件
            //  PageHelper.startPage( pageNum, pageSize );
            List<VoteMainTable> voteList = voteMapper.selectVoteMainTableByCondition(state, createUserNum, voteTitleStr);
            //显示查询页面
            //判断是否有相应的结果
            if (null != voteList && voteList.size() > 0) {
                //如果有
                for (int i = 0; i < voteList.size(); i++) {
                    if (null == voteList.get(i).getId()) {
                        log.error("<<<<<获取主表id为null");
                        continue;
                    }
                    VoteCount voteCount = new VoteCount();
                    //主表id
                    String id = voteList.get(i).getId();
                    //主表主题
                    String vote = voteList.get(i).getVoteTitle();
                    voteCount.setVote(vote);
                    //描述
                    String describes = voteList.get(i).getDescribes();
                    voteCount.setDescribes(describes);
                    //主表备注
                    String remarks1 = voteList.get(i).getRemarks();
                    voteCount.setRemarks(remarks1);
                    //答案统计列表
                    List<AnswerCount> list = new ArrayList<>();

                    //根据主表id调用选择表信息
                    //获取选择表信息
                    List<OptionTable> optionList = voteMapper.selectOptionTableByvoteId(id);
                    if (null != optionList && optionList.size() > 0) {
                        for (int j = 0; j < optionList.size(); j++) {
                            if (null == optionList.get(j).getId()) {
                                log.error("<<<<<获取选择表id为null");
                                continue;
                            }
                            //根据选择表的信息查询答案表
                            Integer optionId = optionList.get(j).getId();
                            AnswerCount answerCount = new AnswerCount();
                            //添加到 list
                            answerCount.setOptionId(optionId);
                            //选择表题目
                            String optiontile = optionList.get(j).getOptionTitle();
                            //添加到 list 选择表题目
                            answerCount.setOptiontile(optiontile);
                            //获取选择表图片路径
                            String pictureUrl = optionList.get(j).getPictureUrl();
                            //添加视频路径到视图
                            answerCount.setPictureUrl(pictureUrl);
                            //获取视频路径
//                            String viewUrl = optionList.get(j).getViewUrl();
//                            //视频添加到视图
//                            answerCount.setViewUrl(viewUrl);
                            //选择表备注
                            String remarks = optionList.get(j).getRemarks();
                            answerCount.setRemarks(remarks);
                            //获取答案总条数
                            Integer count = voteMapper.selectCount(optionId);
                            //答案总条数 添加到 list
                            answerCount.setCount(count);
                            list.add(answerCount);
                        }

                    }
                    //list 封装到 对象
                    PageUtils<AnswerCount> pageUtils = new PageUtils<>(pageNum1, pageSize1, list);
                    voteCount.setListOption(pageUtils.getList());
                    voteCount.setPages(pageUtils.getPages());
                    voteCount.setTotal(pageUtils.getTotal());
                    //封装到显示list
                    countList.add(voteCount);
                }

            } else {
                //如果没有
                log.info("-----没有查到相关投票");
            }
        } catch (Exception e) {
            log.error("<<<<<<<<selectVoteMainTableByCondition 方法失败", e);
        }

        return countList;
    }

    /**
     * app 投票显示
     *
     * @param id
     * @return
     */
    @Override
    public String selectOne(String id, Integer pageNum, Integer pageSize, String loginNum) {
        log.info("--------loginNum=" + loginNum);
        VoteCount voteCount = new VoteCount();
        JSONObject object = new JSONObject();
        try {
            //根据主表id查询主表
            VoteMainTable voteMainTable = voteMapper.selectVoteMainTableById(id);
            //如果 查询非空
            if (null != voteMainTable) {
                //主表标题
                if (null != voteMainTable.getVoteTitle()) {
                    voteCount.setVote(voteMainTable.getVoteTitle());
                } else {
                    voteCount.setVote("");
                }
                //描述
                if (null != voteMainTable.getDescribes()) {
                    String describes = voteMainTable.getDescribes();
                    voteCount.setDescribes(describes);
                } else {
                    voteCount.setDescribes("");
                }
                //主表备注
                if (null != voteMainTable.getRemarks()) {
                    String remarks1 = voteMainTable.getRemarks();
                    voteCount.setRemarks(remarks1);
                } else {
                    voteCount.setRemarks("");
                }

                //答案统计列表
                List<AnswerCount> listOption = new ArrayList<>();
                //根据主表id调用选择表信息
                //获取选择表信息
                List<OptionTable> optionList = voteMapper.selectOptionTableByvoteId(id);
                if (null != optionList && optionList.size() > 0) {
                    for (int j = 0; j < optionList.size(); j++) {
                        if (null == optionList.get(j).getId()) {
                            log.error("<<<<<获取选择表id为null");
                            continue;
                        }
                        AnswerCount answerCount = new AnswerCount();
                        //根据选择表的信息查询答案表
                        Integer optionId = optionList.get(j).getId();
                        //添加到 list选择表id
                        answerCount.setOptionId(optionId);
                        //选择表题目
                        String optiontile = optionList.get(j).getOptionTitle();
                        //添加到 list 选择表题目
                        answerCount.setOptiontile(optiontile);
                        //获取选择表图片路径
                        String pictureUrl = optionList.get(j).getPictureUrl();
                        //添加视频路径到视图
                        answerCount.setPictureUrl(pictureUrl);
                        //获取视频路径
                        //  String viewUrl = optionList.get(j).getViewUrl();
                        //视频添加到视图
                        //  answerCount.setViewUrl(viewUrl);
                        //获取部门信息
                        String department = optionList.get(j).getDepartment();
                        answerCount.setDepartment(department);
                        //选择表备注
                        String remarks = optionList.get(j).getRemarks();
                        answerCount.setRemarks(remarks);
                        //获取答案总条数
                        Integer count = voteMapper.selectCount(optionId);
                        //答案总条数 添加到 list
                        answerCount.setCount(count);
                        //获取选择表子类
                        List<OptionSublistTable> listSublist = voteMapper.selectSublistByOptionId(optionId);
                        //选择表子类添加到对象
                        answerCount.setListSublist(listSublist);
                        //获取登陆人是否投票数量
                        log.info("-----optionId=" + optionId + " ; loginNum=" + loginNum);
                        if (null == loginNum || "".equals(loginNum)) {
                            answerCount.setIsVote(-1);
                        } else {
                            Integer isVote = voteMapper.isVote(optionId, loginNum);
                            answerCount.setIsVote(isVote);
                        }
                        //添加到选择表
                        listOption.add(answerCount);
                        //子表信息添加


                    }

                }
//                PageInfo<AnswerCount>  page = new PageInfo(list);
//                PageHelper.startPage( pageNum, pageSize );
                //分页
                PageUtils<AnswerCount> pageUtils = new PageUtils<>(pageNum, pageSize, listOption);

                //list 封装到 对象
                voteCount.setListOption(pageUtils.getList());
                object.put("total", pageUtils.getTotal());
                object.put("pages", pageUtils.getPages());

            }
            //查询主表为null
            else {
                //如果没有
                log.info("-----没有查到相关投票");
            }

        } catch (Exception e) {
            log.error("<<<<<<<<selectVoteMainTableByCondition 方法失败", e);
        }

        object.put("voteCount", voteCount);

        return object.toJSONString();
    }

    /**
     * app 查询
     *
     * @param optionTitle 选择表标题
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public String selectOptionTableByTitle(String optionTitle, Integer pageNum, Integer pageSize, String voteId) {
        String optionTitleStr = "%" + optionTitle + "%";
        log.info("-------optionTitleStr---" + optionTitleStr);
        //答案统计列表
        List<AnswerCount> list = new ArrayList<>();
        PageHelper.startPage(pageNum, pageSize);
        //根据主表id调用选择表信息
        //获取选择表信息
        List<OptionTable> optionList = voteMapper.selectOptionTableByTitle(optionTitleStr, voteId);
        if (null != optionList && optionList.size() > 0) {
            for (int j = 0; j < optionList.size(); j++) {
                if (null == optionList.get(j).getId()) {
                    log.error("<<<<<获取选择表id为null");
                    continue;
                }
                AnswerCount answerCount = new AnswerCount();
                //根据选择表的信息查询答案表
                Integer optionId = optionList.get(j).getId();
                //添加到 list选择表id
                answerCount.setOptionId(optionId);
                //选择表题目
                String optiontile = optionList.get(j).getOptionTitle();
                //添加到 list 选择表题目
                answerCount.setOptiontile(optiontile);
                //获取选择表图片路径
                String pictureUrl = optionList.get(j).getPictureUrl();
                //添加视频路径到视图
                answerCount.setPictureUrl(pictureUrl);
                //获取视频路径
                //   String viewUrl = optionList.get(j).getViewUrl();
                //视频添加到视图
                //  answerCount.setViewUrl(viewUrl);
                String department = optionList.get(j).getDepartment();
                answerCount.setDepartment(department);
                //选择表备注
                String remarks = optionList.get(j).getRemarks();
                answerCount.setRemarks(remarks);
                //获取答案总条数
                Integer count = voteMapper.selectCount(optionId);
                //答案总条数 添加到 list
                answerCount.setCount(count);
                list.add(answerCount);
            }
        }
        PageInfo<VoteMainTable> page = new PageInfo(list);
        JSONObject object = new JSONObject();
        object.put("list", list);
        object.put("total", page.getTotal());
        object.put("pages", page.getPages());
        return object.toJSONString();
    }

    @Override
    public List<VoteMainTable> selectVoteMainTable(Integer pageNum, Integer pageSize) {
        //分页插件
        PageHelper.startPage(pageNum, pageSize);
        return voteMapper.selectVoteMainTable();
    }

    @Override
    public List<VoteMainTable> selectVoteMainTableByState(Integer pageNum, Integer pageSize) {
        //分页插件
        PageHelper.startPage(pageNum, pageSize);
        return voteMapper.selectVoteMainTableByState();
    }

    @Override
    public boolean getCount(String userNum, String voteId) {
        Integer sum = 0;
        try {
            List<Integer> opIdList = voteMapper.selectOpIdByVoteIdAnduserNum(voteId);
            if (opIdList.size() > 0) {
                for (int i = 0; i < opIdList.size(); i++) {
                    if (null != opIdList.get(i)) {
                        Integer count = voteMapper.getAnswerCount(userNum, opIdList.get(i));
                        //     log.info("----第" + i + "次加入到总条数-----");
                        sum += count;
                    } else {
                        log.error("<<<<<<数据异常");
                        return false;
                    }
                }
            } else {
                //没有查询到 选择表
                log.info("-----没有查到选择表,sum=" + sum);
                return false;
            }
        } catch (Exception e) {
            log.error("<<<<<<<验证用户投票接口异常", e);
        }

        log.info("--------当天用户投票总条数：sum=" + sum);
        if (sum < defaultVale) {
            return true;
        }
        return false;
    }

    @Override
    public List<OptionSublistTable> selectSublistByOptionId(Integer optionId) {
        return voteMapper.selectSublistByOptionId(optionId);
    }

    @Override
    public Integer insertOptionSublistTable(OptionSublistTable optionSublistTable) {
        voteMapper.insertOptionSublistTable(optionSublistTable);
        Integer subId = optionSublistTable.getId();
        return subId;
    }

    @Override
    public Integer updateOptionSublist(Integer id, String pictureUrl, String viewUrl, String sublistTitle, String remarks) {
        return voteMapper.updateOptionSublist(id, pictureUrl, viewUrl, sublistTitle, remarks);
    }

    @Override
    public Integer deleteSublistSate(Integer id) {
        return voteMapper.deleteSublistSate(id);
    }


}
