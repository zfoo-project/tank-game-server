

package com.zfoo.tank.home.config;

import com.zfoo.net.config.model.NetConfig;
import com.zfoo.orm.config.OrmConfig;
import com.zfoo.storage.config.StorageConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class AutoConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "orm")
    public OrmConfig ormConfig() {
        return new OrmConfig();
    }


    @Bean
    @ConfigurationProperties(prefix = "storage")
    public StorageConfig storageConfig() {
        return new StorageConfig();
    }

    @Bean
    @ConfigurationProperties(prefix = "net")
    public NetConfig netConfig() {
        return new NetConfig();
    }
}
