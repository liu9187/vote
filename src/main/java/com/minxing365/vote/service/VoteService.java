package com.minxing365.vote.service;

import com.minxing365.vote.bean.AnswerTable;
import com.minxing365.vote.bean.OptionTable;
import com.minxing365.vote.bean.VoteMainTable;
import com.minxing365.vote.pojo.AnswerCount;
import com.minxing365.vote.pojo.VoteAndOption;
import com.minxing365.vote.pojo.VoteCount;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VoteService {


    /**
     * 保存主题表
     * @param voteMainTable
     * @return
     */
    Integer insertVoteMainTable(VoteMainTable voteMainTable);

    /**
     * 选项表
     * @param optionTable
     * @return
     */
    Integer insertOptionTable(OptionTable optionTable);

    /**
     * 答案表
     * @param answerTable
     * @return
     */
    Integer insertAnswerTable(AnswerTable answerTable);

    /**
     * 查询主表和选择表
     * @param id 主表id
     * @return
     */
    List<VoteAndOption> selectVoteAndOption(String id);

    /**
     * 查询答案表
     * @param id
     * @param optionTitle
     * @return
     */
    AnswerTable selectAnswer(@Param( value = "id") String id, @Param( value = "optionTitle") String optionTitle);

    /**
     * 根绝选择表id查询选择表信息
     * @param id
     * @return
     */
    OptionTable selectOptionTableById (@Param( value = "id") Integer id);

    /**
     * 根据主表id 查询主表
     * @param id 主表id
     * @return
     */
    VoteMainTable selectVoteMainTableById(@Param( "id" ) String id);

    /**
     * 根据主表id查询选项表id
     * @param voteId
     * @return
     */
    List<OptionTable> selectOptionTableByvoteId(@Param("voteId") String voteId);

    /**
     * 获取主键id
     * @return
     */
    String getId();

    /**
     * 更新主表
     * @param voteTitle
     * @param endTime
     * @param id
     * @return
     */
    Integer updateVoteMainTable(@Param("voteTitle") String voteTitle, @Param("endTime") Long endTime, @Param("id") String id);

    /**
     * 修改选择表
     * @param optionTitle
     * @param pictureUrl
     * @param viewUrl
     * @param optionFlag
     * @param remarks
     * @param id
     * @return
     */
    Integer updateOptionTable(@Param("optionTitle") String optionTitle, @Param("pictureUrl") String pictureUrl, @Param("viewUrl") String viewUrl, @Param("optionFlag") Integer optionFlag, @Param("remarks") String remarks, @Param("id") Integer id);

    /**
     * 修改主表状态
     * @param id
     * @return
     */
    Integer updateState(@Param("id") String id);

    /**
     * 根据主表id删除主表和相关信息
     * @param id
     * @return
     */
    String deleteVote(@Param("id") String id);

    /**
     * 根据选择表id删除选择表数据
     * @param id
     * @return
     */
    Integer deleteOptionTableById(@Param( "id" ) Integer id);

    /**
     * 查询
     * @param state 状态
     * @param createUserNum 行员号
     * @param voteTitle 主题
     * @return
     */
    List<VoteCount> select(@Param("state") Integer state, @Param("createUserNum") String createUserNum, @Param("voteTitle") String voteTitle,@Param( "pageNum" ) Integer pageNum,@Param( "pageSize" ) Integer pageSize);

    /**
     * app页面显示
     * @param id
     * @return
     */
    VoteCount selectOne(String id);

    /**
     * app 查询接口
     * @param optionTitle 选择表标题
     * @return
     */
    String selectOptionTableByTitle(@Param("optionTitle") String optionTitle,Integer pageNum, Integer pageSize);

    /**
     * 首页列表
     * @return
     */
    List<VoteMainTable> selectVoteMainTable(Integer pageNum,Integer pageSize);
}
