package com.minxing365.vote.dao;

import com.minxing365.vote.bean.AnswerTable;
import com.minxing365.vote.bean.OptionTable;
import com.minxing365.vote.bean.VoteMainTable;
import com.minxing365.vote.pojo.VoteAndOption;
import org.apache.ibatis.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface VoteMapper {
    /**
     * 保存主表
     *
     * @param voteMainTable
     * @return
     */
    @Insert("INSERT INTO vote_main_table (id,vote_title,create_user_num,create_user_name,end_time,state,remarks ,describes)"+
            "VALUES" + "(#{id}, #{voteTitle},#{createUserNum},#{createUserName},#{endTime},#{state},#{remarks},#{describes})")
    Integer insertVoteMainTable(VoteMainTable voteMainTable);

    /**
     * 保存选项表
     *
     * @param optionTable
     * @return
     */
    @Insert("INSERT INTO option_table(vote_id,option_title,picture_url,view_url,option_flag,remarks)VALUES(#{voteId},#{optionTitle},#{pictureUrl},#{viewUrl},#{optionFlag},#{remarks})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    Integer insertOptionTable(OptionTable optionTable);

    /**
     * 保存答案表
     *
     * @param answerTable
     * @return
     */
    @Insert("INSERT INTO answer_table(option_id,option_title,answer_user_name)VALUES(#{optionId},#{optionTitle},#{answerUserName})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    Integer insertAnswerTable(AnswerTable answerTable);

    /**
     * 查询主表和选择表信息
     *
     * @param id
     * @return
     */
    @Select("SELECT\n" + "  v.id, \n" + "\tv.state,\n" + "\tv.create_time AS createTime,\n" + "\tv.create_user_num AS createUserNum,\n" + "\tv.create_user_name AS createUserName,\n" + "\tv.end_time AS endTime,\n" + "\tv.vote_title AS voteTitle,\n" + "\to.id AS optionId,\n" + "\to.option_flag AS optionFlag ,\n" + "\to.option_title AS optionTitle,\n" + "\to.picture_url AS pictureUrl,\n" + "\to.remarks,\n" + "\to.view_url AS viewUrl,\n" + "\to.vote_id AS voteId\n" + "\n" + "FROM\n" + "\tvote_main_table v\n" + "LEFT JOIN option_table o ON v.id = o.vote_id\n" + "WHERE\n" + "   v.id=#{id}" + "GROUP BY\n" + "\to.id")
    List<VoteAndOption> selectVoteAndOption(String id);

    /**
     * 查询答案表
     *
     * @param id
     * @param optionTitle
     * @return
     */
    @Select("SELECT a.answer_user_name,a.id,a.option_id,a.option_title \n" + "FROM vote_main_table v LEFT JOIN option_table o ON o.vote_id=v.id\n" + "LEFT JOIN answer_table a ON a.option_id=o.id\n" + "WHERE v.id=#{id} AND a.option_title=#{optionTitle} LIMIT 1")
    AnswerTable selectAnswer(@Param(value = "id") String id, @Param(value = "optionTitle") String optionTitle);

    /**
     * 根据选项表id查询选项表信息
     *
     * @param id
     * @return
     */
    @Select("SELECT  id,vote_id AS voteId,option_title AS optionTitle,picture_url AS pictureUrl,view_url,option_flag AS viewUrl,remarks  FROM  option_table WHERE id=#{id}")
    OptionTable selectOptionTableById(@Param(value = "id") Integer id);

    /**
     * 根据主表id 查询主表
     *
     * @param id
     * @return
     */
    @Select("SELECT id,vote_title AS voteTitle,create_user_name AS createUserName,create_user_num AS createUserNum,end_time AS endTime,state  FROM vote_main_table  WHERE  id =#{id} ")
    VoteMainTable selectVoteMainTableById(@Param("id") String id);

    /**
     * 首页查询列表
     * @return
     */
    @Select("SELECT id,vote_title AS voteTitle,create_user_name AS createUserName,create_user_num AS createUserNum,end_time AS endTime,state  FROM vote_main_table  ")
    List<VoteMainTable> selectVoteMainTable();

    /**
     * 根据主表id查询选项表
     *
     * @param voteId
     * @return
     */
    @Select("SELECT  id,vote_id AS voteId,option_title AS optionTitle,picture_url AS pictureUrl,view_url AS viewUrl,option_flag AS optionFlag ,remarks  FROM  option_table WHERE vote_id=#{voteId}")
    List<OptionTable> selectOptionTableByvoteId(@Param("voteId") String voteId);

    /**
     * 更新主表
     *
     * @param voteTitle
     * @param endTime
     * @param id
     * @return
     */
    @UpdateProvider(type = IntegralSqlBuilder.class, method = "updateVoteMainTable")
    Integer updateVoteMainTable(@Param("voteTitle") String voteTitle, @Param("endTime") Long endTime, @Param("id") String id);

    /**
     * 更新选择表
     *
     * @param optionTitle
     * @param pictureUrl
     * @param viewUrl
     * @param optionFlag
     * @param remarks
     * @param id
     * @return
     */
    @Update("UPDATE option_table SET option_title=IFNULL(#{optionTitle},option_title)," + "picture_url=IFNULL(#{pictureUrl},picture_url) ,view_url=IFNULL(#{viewUrl},view_url)," + "option_flag=IFNULL(#{optionFlag},option_flag)  ,remarks=IFNULL(#{remarks},remarks) WHERE id=#{id} ")
    Integer updateOptionTable(@Param("optionTitle") String optionTitle, @Param("pictureUrl") String pictureUrl, @Param("viewUrl") String viewUrl, @Param("optionFlag") Integer optionFlag, @Param("remarks") String remarks, @Param("id") Integer id);

    /**
     * 修改主表状态
     *
     * @param id
     * @return
     */
    @Update("UPDATE vote_main_table SET state=2 WHERE id=#{id}")
    Integer updateState(@Param("id") String id);

    /**
     * 通过主表id删除主表
     *
     * @param id
     * @return
     */
    @Delete("DELETE FROM vote_main_table WHERE id=#{id} AND state=1")
    Integer deleteVoteMainTable(@Param("id") String id);

    /**
     * 通过主键删除 选择表
     *
     * @param id
     * @return
     */
    @Delete("DELETE FROM option_table WHERE  vote_id=#{id}")
    Integer deleteOptionTable(@Param("id") String id);


    /**
     * 根据选择表id 删除选择表
     */
    @Delete("DELETE FROM option_table WHERE  id=#{id}")
    Integer deleteOptionTableById(@Param("id") Integer id);

    /**
     * 根据条件查询主表信息
     *
     * @param state
     * @param createUserNum
     * @param voteTitle
     * @return
     */
    @Select("SELECT id ,vote_title AS voteTitle FROM vote_main_table WHERE state=#{state} AND create_user_num=#{createUserNum} AND vote_title LIKE #{voteTitle}")
    List<VoteMainTable> selectVoteMainTableByCondition(@Param("state") Integer state, @Param("createUserNum") String createUserNum, @Param("voteTitle") String voteTitle);

    /**
     * 查询答案表条数
     *
     * @param optionId
     * @return
     */
    @Select("SELECT COUNT(id)  FROM  answer_table WHERE option_id=#{optionId}")
    Integer selectCount(@Param("optionId") Integer optionId);

    class IntegralSqlBuilder {
        Logger logger = LoggerFactory.getLogger( IntegralSqlBuilder.class );

        /**
         * 根据主表id 修改主表
         *
         * @param voteTitle
         * @param endTime
         * @param id
         * @return
         */
        public String updateVoteMainTable(@Param("voteTitle") final String voteTitle, @Param("endTime") final Long endTime, @Param("id") final String id) {
            StringBuffer sql = new StringBuffer();
            try {
                sql.append( "UPDATE  vote_main_table" );

                if (!"".equals( voteTitle ) && null != voteTitle) {
                    sql.append( "  set  vote_title=#{voteTitle}   " );

                    if (!"".equals( endTime ) && null != endTime) {
                        sql.append( " , end_time=#{endTime} " );
                    }
                } else {
                    if (!"".equals( endTime ) && null != endTime) {
                        sql.append( " set end_time=#{endTime} " );
                    }
                }
                sql.append( "WHERE state=1 AND id=#{id}" );
            } catch (Exception e) {
                logger.error( "error is mapper updateVoteMainTable:sql" + sql.toString(), e );
            }
            logger.info( "----查询sql==" + sql.toString() );
            return sql.toString();
        }

    }
}
