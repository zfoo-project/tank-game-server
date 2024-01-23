package com.zfoo.tank.single.boot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author godotg
 */
public class HotObject {

    private static final Logger logger = LoggerFactory.getLogger(HotObject.class);

    public void print() {
        logger.info("没有热更新的输出内容");

        int a = 1;
        int b = 1000;
        int c = a + b;
        logger.info("热更新的输出内容");
        logger.info("---------------{}-----------------------", c);
        logger.info("---------------{}-----------------", c);
        logger.info("---------------{}------------", c);
        logger.info("---------------{}", c);
    }

}
