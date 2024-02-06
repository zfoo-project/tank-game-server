

package com.zfoo.tank.push.config;

import com.zfoo.net.config.model.NetConfig;
import com.zfoo.orm.config.OrmConfig;
import com.zfoo.storage.config.StorageConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class AutoConfiguration {
    @Bean
    @ConfigurationProperties(prefix = "net")
    public NetConfig netConfig() {
        return new NetConfig();
    }
}
