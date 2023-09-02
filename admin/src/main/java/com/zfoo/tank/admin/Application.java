/*
 * Copyright (C) 2020 The zfoo Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.zfoo.tank.admin;


import com.zfoo.event.model.AppStartEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

/**
 * 1. 将前端打包好的包copy到static目录
 * 2. 修改application.properties配置文件：
 * server.address=192.168.1.191（本机对外ip，使用ifconfig查看）
 * server.port=80
 * 3. 使用package命令，在target目录生成river-2.0.jar
 * <p>
 * Linux shell中有三种输入输出，分别为标准输入，标准输出，错误输出，分别对应0，1，2。
 * 下面的将标准输出定位到nohup.log，将错误输出重定向到标准输出。
 * 4. nohup java -jar river-2.0.jar &
 * 5. tailf nohup.log
 * 6. kill -15 pid
 *
 *
 * <p>
 * java的启动参数
 * <p>
 * 1.-DpropName=propValue的形式携带，要放在-jar参数前面
 * eg:java -Dxxx=test -DprocessType=1 -jar xxx.jar
 * 取值：System.getProperty("propName")
 * <p>
 * 2.参数直接跟在命令后面
 * eg:java -jar xxx.jar processType=1 processType2=2
 * 取值：参数就是jar包里主启动类中main方法的args参数，按顺序来
 * <p>
 * 3.springboot的方式，--key=value方式
 * eg:java -jar xxx.jar --xxx=test
 * 取值：spring的@value("${xxx}")
 *
 * @author jaysunxiao
 * @version 1.0
 * @since 2019-03-10 19:43
 */
@SpringBootApplication
@ImportResource(locations = {"app.xml"})
public class Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);


    public static void main(String[] args) {
        var context = SpringApplication.run(Application.class, args);
        context.registerShutdownHook();
        context.publishEvent(new AppStartEvent(context));
        logger.info("Start Web Application!");
    }


}
