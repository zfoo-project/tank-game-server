package com.zfoo.tank.login.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author godotg
 */
@Configuration
@ConfigurationProperties(prefix = "web")
public class WebConfiguration {

    private List<String> connectUrls;

    public List<String> getConnectUrls() {
        return connectUrls;
    }

    public void setConnectUrls(List<String> connectUrls) {
        this.connectUrls = connectUrls;
    }
}
