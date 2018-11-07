package com.minxing365.vote.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.minxing365.vote.bean.AnswerTable;
import com.minxing365.vote.bean.OptionTable;
import com.minxing365.vote.bean.VoteMainTable;
import com.minxing365.vote.dao.VoteMapper;
import com.minxing365.vote.pojo.AnswerCount;
import com.minxing365.vote.pojo.VoteAndOption;
import com.minxing365.vote.pojo.VoteCount;
import com.minxing365.vote.service.VoteService;
import com.minxing365.vote.util.JnbEsbUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class VoteServiceImpl implements VoteService {
    Logger log = LoggerFactory.getLogger( VoteServiceImpl.class );
    @Autowired
    private VoteMapper voteMapper;

    @Override
    @Transactional
    public Integer insertVoteMainTable(VoteMainTable voteMainTable) {

        return voteMapper.insertVoteMainTable( voteMainTable );
    }

    @Override
    @Transactional
    public Integer insertOptionTable(OptionTable optionTable) {
        voteMapper.insertOptionTable( optionTable );
        Integer id = optionTable.getId();
        return id;
    }

    @Override
    @Transactional
    public Integer insertAnswerTable(AnswerTable answerTable) {
        voteMapper.insertAnswerTable( answerTable );
        Integer id = answerTable.getId();
        return id;
    }

    @Override
    public List<VoteAndOption> selectVoteAndOption(String id) {
        return voteMapper.selectVoteAndOption( id );
    }

    @Override
    public AnswerTable selectAnswer(String id, String optionTitle) {
        return voteMapper.selectAnswer( id, optionTitle );
    }

    @Override
    public OptionTable selectOptionTableById(Integer id) {
        return voteMapper.selectOptionTableById( id );
    }

    @Override
    public VoteMainTable selectVoteMainTableById(String id) {
        return voteMapper.selectVoteMainTableById( id );
    }

    @Override
    public List<OptionTable> selectOptionTableByvoteId(String voteId) {
        return voteMapper.selectOptionTableByvoteId( voteId );
    }

    @Override
    public String getId() {
        String id = JnbEsbUtil.CreateMsgId();
        return id;
    }

    @Override
    @Transactional
    public Integer updateVoteMainTable(String voteTitle, Long endTime, String id) {
        return voteMapper.updateVoteMainTable( voteTitle, endTime, id );
    }

    @Override
    @Transactional
    public Integer updateOptionTable(String optionTitle, String pictureUrl, String viewUrl, Integer optionFlag, String remarks, Integer id) {
        return voteMapper.updateOptionTable( optionTitle, pictureUrl, viewUrl, optionFlag, remarks, id );
    }

    @Override
    @Transactional
    public Integer updateState(String id) {
        Integer result = voteMapper.updateState( id );
        if (result > 0) {
            //发布消息接口
        }
        return result;
    }

    @Override
    @Transactional
    public String deleteVote(String id) {
        JSONObject object = new JSONObject();
        try {
            //删除主表
            Integer main = voteMapper.deleteVoteMainTable( id );
            //删除选择表
            if (main > 0) {
                voteMapper.deleteOptionTable( id );
                object.put( "message", "删除成功" );
                log.info( "-----删除成功" );
            } else {
                log.info( "-----投票已发布或投票不存在" );
                object.put( "message", "投票已发布或投票不存在" );
            }
        } catch (Exception e) {
            log.error( "<<<<<<delete failed ", e );
        }
        return object.toJSONString();
    }

    @Override
    @Transactional
    public Integer deleteOptionTableById(Integer id) {
        return voteMapper.deleteOptionTableById( id );
    }

    @Override
    public List<VoteCount> select(Integer state, String createUserNum, String voteTitle, Integer pageNum, Integer pageSize) {
        String voteTitleStr = "%" + voteTitle + "%";
        log.info( "----状态 ：state==" + state + ";行员号：createUserNum ==" + createUserNum + "; 主题：voteTitle==" + voteTitle + ";模糊条件 ：voteTitleStr " + voteTitleStr );
        List<VoteCount> countList = new ArrayList<>();
        try {
            //分页插件
            PageHelper.startPage( pageNum, pageSize );
            List<VoteMainTable> voteList = voteMapper.selectVoteMainTableByCondition( state, createUserNum, voteTitleStr );
            //显示查询页面

            VoteCount voteCount = new VoteCount();
            //判断是否有相应的结果
            if (null != voteList && voteList.size() > 0) {
                //如果有
                for (int i = 0; i < voteList.size(); i++) {
                    if (null == voteList.get( i ).getId()) {
                        log.error( "<<<<<获取主表id为null" );
                        continue;
                    }
                    //主表id
                    String id = voteList.get( i ).getId();
                    //主表主题
                    String vote = voteList.get( i ).getVoteTitle();
                    voteCount.setVote( vote );
                    //答案统计列表
                    List<AnswerCount> list = new ArrayList<>();
                    AnswerCount answerCount = new AnswerCount();
                    //根据主表id调用选择表信息
                        //获取选择表信息
                        List<OptionTable> optionList = voteMapper.selectOptionTableByvoteId( id );
                        if (null != optionList && optionList.size() > 0) {
                            for (int j = 0; j < optionList.size(); j++) {
                                if (null == optionList.get( j ).getId()) {
                                    log.error( "<<<<<获取选择表id为null" );
                                    continue;
                                }
                                //根据选择表的信息查询答案表
                                Integer optionId = optionList.get( j ).getId();
                                //添加到 list
                                answerCount.setOptionId( optionId );
                                //选择表题目
                                String optiontile = optionList.get( j ).getOptionTitle();
                                //添加到 list 选择表题目
                                answerCount.setOptiontile( optiontile );
                                //获取答案总条数
                                Integer count = voteMapper.selectCount( optionId );
                                //答案总条数 添加到 list
                                answerCount.setCount( count );
                                list.add( answerCount );
                            }

                        }
                    //list 封装到 对象
                    voteCount.setList( list );
                    //封装到显示list
                    countList.add( voteCount );
                }

            } else {
                //如果没有
                log.info( "-----没有查到相关投票" );
            }
        } catch (Exception e) {
            log.error( "<<<<<<<<selectVoteMainTableByCondition 方法失败", e );
        }

        return countList;
    }

    @Override
    public List<VoteMainTable> selectVoteMainTable(Integer pageNum, Integer pageSize) {
        //分页插件
        PageHelper.startPage( pageNum, pageSize );
        return voteMapper.selectVoteMainTable();
    }



}
