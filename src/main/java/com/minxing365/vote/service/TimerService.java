package com.minxing365.vote.service;

import com.minxing365.vote.timers.OverdueVotingTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Timer;

/**
 * 定时任务服务
 */
@Service
public class TimerService {
    @Autowired
    private OverdueVotingTask overdueVotingTask;
    //守护线程
    private Timer timer = new Timer(true);

    public void overdueVotingService(String voteId, Long endTime) {
        overdueVotingTask.setVoteId(voteId);

        Date dateRef = new Date(endTime);
        timer.schedule(overdueVotingTask, dateRef);

    }
}
