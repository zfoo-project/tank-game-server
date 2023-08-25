package com.zfoo.tank.single.aop;

import com.zfoo.net.NetContext;
import com.zfoo.scheduler.manager.SchedulerBus;
import com.zfoo.tank.single.controller.BattleController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class AopTest {

    private static final Logger logger = LoggerFactory.getLogger(BattleController.class);

    static {
        SchedulerBus.schedule(new Runnable() {
            @Override
            public void run() {
                NetContext.getApplicationContext().getBean(AopTest.class).hello();
            }
        }, 5000, TimeUnit.MILLISECONDS);
    }

    @MyAnnotation(methodName = "test aop")
    public void hello() {
        logger.info("test aop");
    }

}
