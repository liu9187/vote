package com.minxing365.vote.timers;
import com.minxing365.vote.dao.VoteMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.TimerTask;

/**
 * 投票过去任务
 */
@Service
public class OverdueVotingTask extends TimerTask {

    Logger log = LoggerFactory.getLogger(OverdueVotingTask.class);
    @Autowired
    VoteMapper voteMapper;
    private String voteId;

    public void setVoteId(String voteId) {
        this.voteId = voteId;
    }
    //需要执行的定时任务
    @Override
    public void run() {
     Integer result= voteMapper.updateState4(voteId);
        if (result>0){
            log.info("投票过期任务启动,投票时间结束");
        }else {
            log.info("投票过期任务启动，投票已过期或者未发布");
        }

    }
}
