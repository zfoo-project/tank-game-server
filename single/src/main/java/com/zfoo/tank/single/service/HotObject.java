package com.zfoo.tank.single.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author godotg
 */
public class HotObject {

    private static final Logger logger = LoggerFactory.getLogger(HotObject.class);

    public void print() {
        logger.info("没有热更新的输出内容");
    }

}
