package com.minxing365.vote.dao;

import com.minxing365.vote.bean.AnswerTable;
import com.minxing365.vote.bean.OptionSublistTable;
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
    @Insert("INSERT INTO vote_main_table (id,vote_title,create_user_num,create_user_name,end_time,state,remarks ,describes,start_time)" +
            "VALUES" + "(#{id}, #{voteTitle},#{createUserNum},#{createUserName},#{endTime},#{state},#{remarks},#{describes},#{startTime})")
    Integer insertVoteMainTable(VoteMainTable voteMainTable);

    /**
     * 保存选项表
     *
     * @param optionTable
     * @return
     */
    @Insert("INSERT INTO option_table(vote_id,option_title,picture_url,remarks,department )VALUES(#{voteId},#{optionTitle},#{pictureUrl},#{remarks},#{department})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    Integer insertOptionTable(OptionTable optionTable);

    /**
     * 保存选择表子表
     * @param optionSublistTable
     * @return
     */
    @Insert("INSERT INTO option_sublist_table(option_id,picture_url,view_url,sublist_title,remarks)\n" +
            "VALUES(#{optionId},#{pictureUrl},#{viewUrl},#{sublistTitle},#{remarks})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    Integer insertOptionSublistTable(OptionSublistTable optionSublistTable);

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
    @Select("SELECT\n" +
            "\t v.id,\n" +
            "\t v.state ,v.start_time AS startTime ,v.create_user_num AS createUserNum ,v.create_user_name AS createUserName ,v.end_time AS endTime ,v.vote_title AS voteTitle ,o.id AS optionId , o.option_title AS optionTitle ,o.picture_url AS pictureUrl ,o.remarks ,o.vote_id AS voteId , o.department " +
            "FROM\n" +
            "vote_main_table v \n" +
            "LEFT JOIN option_table o ON v.id = o.vote_id \n" +
            "WHERE\n" +
            " o.state_option=1 AND v.id =#{id} GROUP BY o.id")
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
    @Select("SELECT  id,vote_id AS voteId,option_title AS optionTitle,picture_url AS pictureUrl,remarks,department  FROM  option_table WHERE state_option=1 AND id=#{id}")
    OptionTable selectOptionTableById(@Param(value = "id") Integer id);

    /**
     * 根据选择表id查询选择表子表
     * @param optionId
     * @return
     */
    @Select("SELECT id,option_id AS optionId,picture_url AS pictureUrl, sublist_title AS sublistTitle,view_url AS viewUrl,state_sublist AS stateSublist ,remarks   FROM option_sublist_table WHERE  state_sublist=1 AND   option_id=#{optionId}")
    List<OptionSublistTable> selectSublistByOptionId(@Param("optionId") Integer optionId);
    /**
     * 根据主表id 查询主表
     *
     * @param id
     * @return
     */
    @Select("SELECT id,vote_title AS voteTitle,create_user_name AS createUserName,create_user_num AS createUserNum,end_time AS endTime,state ,describes,remarks ,start_time AS startTime FROM vote_main_table  WHERE  id=#{id}")
    VoteMainTable selectVoteMainTableById(@Param("id") String id);

    /**
     * 根据发布状态查询主表
     *
     * @return
     */
    @Select("SELECT id,vote_title AS voteTitle,create_user_name AS createUserName,create_user_num AS createUserNum,end_time AS endTime,state ,describes,remarks,start_time AS startTime FROM vote_main_table  WHERE state=2")
    List<VoteMainTable> selectVoteMainTableByState();

    /**
     * 首页查询列表
     *
     * @return
     */
    @Select("SELECT id,vote_title AS voteTitle,create_user_name AS createUserName,create_user_num AS createUserNum,end_time AS endTime,start_time AS startTime, state  FROM vote_main_table WHERE state <>3")
    List<VoteMainTable> selectVoteMainTable();

    /**
     * 根据主表id查询选项表
     *
     * @param voteId
     * @return
     */
    @Select("SELECT  id,vote_id AS voteId,option_title AS optionTitle,picture_url AS pictureUrl,state_option AS stateOption,remarks,department  FROM  option_table WHERE state_option=1 AND vote_id=#{voteId}")
    List<OptionTable> selectOptionTableByvoteId(@Param("voteId") String voteId);

    /**
     * 根据选择表标题进行进行查询
     *
     * @param optionTitle
     * @return
     */
    @Select("SELECT\n" +
            "\tid,\n" +
            "\tvote_id AS voteId,\n" +
            "\toption_title AS optionTitle,\n" +
            "\tpicture_url AS pictureUrl,\n" +
            "  department,\n" +
            "  state_option AS stateOption, \n" +
            "\tremarks\n" +
            "FROM\n" +
            "\toption_table\n" +
            "WHERE\n" +
            "\tvote_id = #{voteId} AND state_option=1 AND  option_title LIKE #{optionTitle}")
    List<OptionTable> selectOptionTableByTitle(@Param("optionTitle") String optionTitle, @Param("voteId") String voteId);

    /**
     * 更新主表
     *
     * @param voteTitle
     * @param endTime
     * @param id
     * @return
     */
    @UpdateProvider(type = VoteSqlBuilder.class, method = "updateVoteMainTable")
    Integer updateVoteMainTable(@Param("voteTitle") String voteTitle, @Param("describes") String describes, @Param("remarks") String remarks, @Param("startTime") Long startTime, @Param("endTime") Long endTime, @Param("id") String id);

    /**
     *根据选择表id 更新选择表
     *
     * @param optionTitle
     * @param pictureUrl
     * @param remarks
     * @param id
     * @return
     */
    @UpdateProvider(type = VoteSqlBuilder.class,method = "updateOptionTable")
    Integer updateOptionTable(@Param("optionTitle") String optionTitle, @Param("pictureUrl") String pictureUrl, @Param("remarks") String remarks, @Param("department") String department,@Param("id") Integer id);

    /**
     * 修改主表状态为发布状态
     *
     * @param id
     * @return
     */
    @Update("UPDATE vote_main_table SET state=2 WHERE state=1 AND id=#{id}")
    Integer updateState(@Param("id") String id);

    /**
     * 修改投票状态为过期状态
     * @param id
     * @return
     */
    @Update("UPDATE vote_main_table SET state=4 WHERE state=2 AND id=#{id}")
    Integer updateState4(@Param("id") String id);

    /**
     * 根据选择表id删除选择表子表
     * @param optionId
     * @return
     */
    @Update("UPDATE option_sublist_table SET  state_sublist=0 WHERE option_id=#{optionId}")
    Integer updateSublist(@Param("optionId") Integer optionId);

    /**
     *
     *删除主表
     * @param id 主表id
     * @return
     */
    @Update("UPDATE vote_main_table SET state=3 WHERE state=1 AND id=#{id}")
    Integer deleteVoteMainTable(@Param("id") String id );

    /**
     * 通过主键删除 选择表
     *
     * @param id
     * @return
     */
    @Update("UPDATE option_table SET state_option=0 WHERE  vote_id=#{id}")
    Integer deleteOptionTable(@Param("id") String id);


    /**
     * 根据选择表id 删除选择表
     */
    @Update("UPDATE option_table SET state_option=0 WHERE id=#{id}")
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

    /**
     * 当前登陆人是否可以投票
     *
     * @param optionId
     * @param loginNum
     * @return
     */
    @Select("SELECT COUNT(id)  FROM  answer_table WHERE option_id=#{optionId} and  option_title=#{loginNum} AND TO_DAYS(create_time)=TO_DAYS(NOW())")
    Integer isVote(@Param("optionId") Integer optionId, @Param("loginNum") String loginNum);

    /**
     * 验证用户投票条数——根据行员号和主表id查询选择表id
     *
     * @param voteId
     * @return
     */
    @Select("SELECT  o.id  FROM  vote_main_table v LEFT JOIN option_table o ON o.vote_id =v.id WHERE  v.id=#{voteId} GROUP BY o.id")
    List<Integer> selectOpIdByVoteIdAnduserNum(@Param("voteId") String voteId);

    /**
     * 验证用户投票条数——根据行员号查询答案表条数
     *
     * @param userNum
     * @return
     */
    @Select(" SELECT COUNT(a.id) AS count  FROM  option_table o  LEFT JOIN answer_table a ON a.option_id=o.id WHERE   a.option_title=#{userNum} AND o.id=#{opId} AND TO_DAYS(a.create_time)=TO_DAYS(NOW())")
    Integer getAnswerCount(@Param("userNum") String userNum, @Param("opId") Integer opId);

    /**
     * 更新子表信息
     * @param id 子表id
     * @param pictureUrl 图片路径
     * @param viewUrl 视频路径
     * @param sublistTitle 子表说明
     * @param remarks 备注
     * @return
     */
    @UpdateProvider(type = VoteSqlBuilder.class, method = "updateOptionSublist")
    Integer updateOptionSublist(@Param("id")  Integer id,@Param("pictureUrl")  String pictureUrl,@Param("viewUrl")  String viewUrl,@Param("sublistTitle")  String sublistTitle,@Param("remarks")  String remarks );

    /**
     * 删除子表
     * @param id
     * @return
     */
    @Update("UPDATE option_sublist_table SET state_sublist=0 WHERE id=#{id}")
    Integer deleteSublistSate(@Param("id") Integer id);
    class VoteSqlBuilder {
        Logger logger = LoggerFactory.getLogger(VoteSqlBuilder.class);

        /**
         * 根据主表id 修改主表
         *
         * @param voteTitle
         * @param endTime
         * @param id
         * @return
         */
        public String updateVoteMainTable(@Param("voteTitle") final String voteTitle, @Param("describes") final String describes, @Param("remarks") final String remarks, @Param("startTime") final Long startTime, @Param("endTime") final Long endTime, @Param("id") final String id) {
            StringBuffer sql = new StringBuffer();
            try {
                sql.append("UPDATE  vote_main_table");

                if (!"".equals(voteTitle) && null != voteTitle) {
                    sql.append("  set  vote_title=#{voteTitle}   ");
                }else {
                    sql.append("  set  vote_title=vote_title   ");
                }
                if (!"".equals(describes) && null != describes) {
                    sql.append(" , describes=#{describes} ");
                }else {
                    sql.append(" , describes=describes ");
                }
                if (!"".equals(remarks) && null != remarks) {
                    sql.append(" , remarks=#{remarks} ");
                }else {
                    sql.append(" , remarks=remarks ");
                }
                if (!"".equals(startTime) && null != startTime) {
                    sql.append(" , start_time=#{startTime} ");
                }else {
                    sql.append(" , start_time=start_time ");
                }
                if (!"".equals(endTime) && null != endTime) {
                    sql.append(" , end_time=#{endTime} ");
                }else {
                    sql.append(" , end_time=end_time");
                }
                sql.append(" WHERE state=1 AND id=#{id}");
            } catch (Exception e) {
                logger.error("error is mapper updateVoteMainTable:sql" + sql.toString(), e);
            }
            logger.info("----更新主表sql==" + sql.toString());
            return sql.toString();
        }

        /**
         * 更新子表查询
         * @param id
         * @param pictureUrl
         * @param viewUrl
         * @param sublistTitle
         * @param remarks
         * @return
         */
        public String updateOptionSublist(@Param("id") final Integer id,@Param("pictureUrl") final String pictureUrl,@Param("viewUrl") final String viewUrl,@Param("sublistTitle") final String sublistTitle,@Param("remarks") final String remarks ){
            StringBuffer sql = new StringBuffer();
            try {
                 sql.append(" UPDATE option_sublist_table ");
                if (!"".equals(pictureUrl) && null != pictureUrl) {
                    sql.append("SET picture_url=#{pictureUrl},");
                }else {
                    sql.append("SET picture_url=picture_url, ");
                }
                if (!"".equals(viewUrl) && null != viewUrl) {
                    sql.append("view_url=#{viewUrl},");
                }else {
                    sql.append("view_url=view_url,");
                }

                if (!"".equals(sublistTitle) && null != sublistTitle) {
                    sql.append(" sublist_title=#{sublistTitle},");
                }else {
                    sql.append(" sublist_title=sublist_title,");
                }
                if (!"".equals(remarks) && null != remarks) {
                    sql.append(" remarks=#{remarks}");
                }else {
                    sql.append(" remarks=remarks");
                }
                sql.append(" WHERE id=#{id}");
            }catch (Exception e){
                logger.error("error is mapper updateOptionSublist:sql" + sql.toString(), e);
            }
            logger.info(" 更新子表 updateOptionSublist:sql" + sql.toString());
           return sql.toString();
        }

        /**
         *根据选择表id 更新选择表
         * @param optionTitle
         * @param pictureUrl
         * @param remarks
         * @param id
         * @return
         */
        public String updateOptionTable(@Param("optionTitle") final String optionTitle, @Param("pictureUrl")  final  String pictureUrl, @Param("remarks") final String remarks,@Param("department") final String department, @Param("id") final Integer id){
              StringBuilder sql=new StringBuilder();
               try {
                    sql.append("UPDATE option_table ");
                   if (!"".equals(optionTitle) && null != optionTitle) {
                       sql.append(" SET option_title=#{optionTitle},");
                   }else {
                       sql.append(" SET option_title=option_title , ");
                   }

                   if (!"".equals(pictureUrl) && null != pictureUrl) {
                       sql.append("picture_url=#{pictureUrl},");
                   }else {
                       sql.append("picture_url=picture_url, ");
                   }
                   if (!"".equals(remarks) && null != remarks) {
                       sql.append("remarks=#{remarks}, ");
                   }else {
                       sql.append("remarks=remarks , ");
                   }
                   if (!"".equals(department) && null != department) {
                       sql.append("department=#{department} ");
                   }else {
                       sql.append("department=department  ");
                   }
                   sql.append(" WHERE id=#{id}");

               }catch (Exception e){
                   logger.error("error is mapper updateOptionTable:sql" + sql.toString(), e);
               }
            logger.info(" 更新子表 updateOptionTable:sql" + sql.toString());
               return  sql.toString();

        }

    }
}
