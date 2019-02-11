package com.minxing365.vote.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface InitMapper {
    /**
     *检测主表是否初始化
     * @return
     */
    @Select( "SELECT id  FROM vote_main_table LIMIT 1" )
    Integer  findVote();
    /**
     * 初始化主表
     */
    @Select( "CREATE TABLE vote_main_table(\n" +
            "            id VARCHAR(255)  COMMENT '主表id',\n" +
            "            vote_title VARCHAR(255) COMMENT '主题', \n" +
            "            create_user_num VARCHAR(255)  COMMENT '创建人工号', \n" +
            "            create_user_name  VARCHAR(255) COMMENT '创建人名称',\n" +
            "            create_time  timestamp   DEFAULT CURRENT_TIMESTAMP   COMMENT '创建时间',\n" +
            "            describes VARCHAR(255) COMMENT  '描述' ,\n" +
            "            remarks VARCHAR(255) COMMENT '备注' ,\n" +
            "            end_time bigint COMMENT '结束时间',state int COMMENT '状态  1 未发布 2 已发布 3 已删除 4 已结束' )" )
    void initVote();

    /**
     * 检测选择表是否创建表
     * @return
     */
    @Select( "SELECT id FROM  option_table LIMIT 1" )
     Integer findOption();
    /**
     * 创建选择表
     */
    @Select( "CREATE TABLE option_table(\n" +
            "id INT(11) NOT NULL  AUTO_INCREMENT ,\n" +
            "vote_id VARCHAR(255) NOT NULL COMMENT'主表ID',\n" +
            "option_title VARCHAR(255) NOT NULL COMMENT '主题',\n" +
            "picture_url  VARCHAR(255) COMMENT'图片地址',\n" +
            "view_url   VARCHAR(255) COMMENT'视频地址',\n" +
            "option_flag INT COMMENT'选项类型(1文本，2图片，3视频)',\n" +
            "create_time  timestamp   DEFAULT CURRENT_TIMESTAMP   COMMENT '创建时间',\n"+
            "remarks VARCHAR(255)  COMMENT'备注',\n" + "PRIMARY KEY (`id`)\n" + ")" )
    void initOption();

    /**
     * 检测 答案表是否创建
     * @return
     */
    @Select( "SELECT id FROM answer_table LIMIT 1" )
     Integer findAnswer();
    /**
     * 答题表
     */
    @Select( "CREATE TABLE answer_table(\n" +
            "id INT(11) NOT NULL  AUTO_INCREMENT ,\n" +
            "option_id INT(11) NOT NULL  COMMENT '选项表ID',\n" +
            "option_title VARCHAR(255)  COMMENT'选项表主题',\n" +
            "answer_user_name VARCHAR(255) COMMENT'答题人',\n" +
            "create_time  timestamp   DEFAULT CURRENT_TIMESTAMP   COMMENT '创建时间',\n"+
            "PRIMARY KEY (`id`)\n" + ")" )
    void initAnswer();


}
