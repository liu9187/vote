package com.minxing365.vote.service;

import com.minxing365.vote.bean.AnswerTable;
import com.minxing365.vote.bean.OptionSublistTable;
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
    String insertVoteMainTable(VoteMainTable voteMainTable);

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
     * @param voteTitle 主题
     * @param describes 描述
     * @param remarks 备注
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param id
     * @return
     */
    Integer updateVoteMainTable(@Param("voteTitle") String voteTitle,@Param("describes")String describes,
                                @Param("remarks") String remarks, @Param("startTime") Long startTime,  @Param("endTime") Long endTime, @Param("id") String id);

    /**
     * 修改选择表
     * @param optionTitle
     * @param pictureUrl
     * @param remarks
     * @param id
     * @return
     */
    Integer updateOptionTable(@Param("optionTitle") String optionTitle, @Param("pictureUrl") String pictureUrl, @Param("remarks") String remarks,@Param("department") String department, @Param("id") Integer id);

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
    List<VoteCount> select(@Param("state") Integer state, @Param("createUserNum") String createUserNum, @Param("voteTitle") String voteTitle,@Param( "pageNum" ) Integer pageNum,@Param( "pageSize" ) Integer pageSize,@Param( "pageNum1" ) Integer pageNum1,@Param( "pageSize1" ) Integer pageSize1);

    /**
     * app页面显示
     * @param id
     * @return
     */
    String selectOne(String id,Integer pageNum,Integer pageSize,String optionTitle);

    /**
     * app 查询接口
     * @param optionTitle 选择表标题
     * @return
     */
    String selectOptionTableByTitle( String optionTitle,Integer pageNum, Integer pageSize,String voteId);

    /**
     * 首页列表
     * @return
     */
    List<VoteMainTable> selectVoteMainTable(Integer pageNum,Integer pageSize);

    /**
     * 根据发布状态查询列表
     * @return
     */
    List<VoteMainTable>  selectVoteMainTableByState(Integer pageNum,Integer pageSize);

    /**
     * 验证用户投票数量
     * @param userNum
     * @param voteId
     * @return
     */
    boolean getCount(@Param("userNum") String userNum, @Param("voteId") String voteId);

    /**
     * 根据选择表id查询选择表子表
     * @param optionId
     * @return
     */
    List<OptionSublistTable> selectSublistByOptionId(@Param("optionId") Integer optionId);

    /**
     * 新增选择表子表
     * @param optionSublistTable
     * @return
     */
    Integer insertOptionSublistTable(OptionSublistTable optionSublistTable);

    /**
     * 更新选择表子表
     * @param id
     * @param pictureUrl
     * @param viewUrl
     * @param sublistTitle
     * @param remarks
     * @return
     */
    Integer updateOptionSublist(@Param("id")  Integer id,@Param("pictureUrl")  String pictureUrl,@Param("viewUrl")  String viewUrl,@Param("sublistTitle")  String sublistTitle,@Param("remarks")  String remarks );

    /**
     * 删除子表
     * @param id
     * @return
     */
    Integer deleteSublistSate(@Param("id") Integer id);

}
