package com.minxing365.vote.listener;

import com.minxing365.vote.dao.InitMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
@Component
public class InitListener {
    @Autowired
    private InitMapper initMapper;

    Logger logger = LoggerFactory.getLogger(InitListener.class);
    public void init(){
        logger.info("-------start init vote-----进入初始化设置----");
        //尝试创建 主表
         try {
            initMapper.findVote();
        }catch (Exception e){
            logger.info( "Table vote_main_table not exist, Start to create table" );
            initMapper.initVote();
        }
        //尝试创建 选择表
        try {
            initMapper.findOption();
        }catch (Exception e){
            logger.info( "Table option_table not exist, Start to create table" );
            initMapper.initOption();
        }

        try {
            initMapper.findAnswer();
        }catch (Exception e){
            logger.info( "Table answer_table not exist, Start to create table" );
            initMapper.initAnswer();
        }
    }
}
