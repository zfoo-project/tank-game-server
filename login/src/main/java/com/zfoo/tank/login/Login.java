package com.zfoo.tank.login;

import com.zfoo.event.model.AppStartEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

@SpringBootApplication(exclude = {
        MongoDataAutoConfiguration.class,
        MongoRepositoriesAutoConfiguration.class,
        MongoAutoConfiguration.class
})
public class Login {

    private static final Logger logger = LoggerFactory.getLogger(Login.class);

    /**
     * java -Dspring.profiles.active=pro -jar web-1.0.jar
     */
    public static void main(String[] args) {
        var context = SpringApplication.run(Login.class, args);
        context.registerShutdownHook();
        context.publishEvent(new AppStartEvent(context));
        logger.info("start web application!");
    }

}
