package com.minxing365.vote.listener;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

public class ApplicationEventListener implements ApplicationListener<ContextRefreshedEvent> {
    private static final Logger logger = Logger.getLogger(ApplicationEventListener.class);
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent()==null){
            logger.info("start init message listener");
            event.getApplicationContext().getBean( InitListener.class ).init();
        }

    }
}
