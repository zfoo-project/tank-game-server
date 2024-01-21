package com.zfoo.tank.login.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * 全局环境变量，可以通过environment访问运行时的一些配置数据
 *
 * @author sun
 */
@Configuration
public class EnvironmentConfig {

    @Autowired
    private Environment environment;

    public int getServerPort() {
        return environment.getProperty("server.port", Integer.class);
    }

}
