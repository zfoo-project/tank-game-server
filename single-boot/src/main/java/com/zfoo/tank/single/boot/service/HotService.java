package com.zfoo.tank.single.boot.service;

import com.zfoo.event.manager.EventBus;
import com.zfoo.hotswap.util.HotSwapUtils;
import com.zfoo.protocol.collection.CollectionUtils;
import com.zfoo.protocol.util.FileUtils;
import com.zfoo.scheduler.anno.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

/**
 * @author godotg
 */
@Component
public class HotService {

    private static final Logger logger = LoggerFactory.getLogger(HotService.class);

    @Scheduler(cron = "0/1 * * * * ?")
    public void cronHotswap() throws IOException {
        var path = "/hotswap";
        var files = FileUtils.getAllReadableFiles(new File(path));
        if (CollectionUtils.isEmpty(files)) {
            return;
        }

        logger.info("开始热更新[{}]", files.size());
        var hotObject = new HotObject();
        hotObject.print();
        for(var file : files) {
            if (!file.isFile()) {
                continue;
            }
            var bytes = FileUtils.readFileToByteArray(file);
            HotSwapUtils.hotswapClass(bytes);
            FileUtils.deleteFile(file);
        }
        logger.info("热更新之后[{}]", files.size());

        EventBus.asyncExecute(new Runnable() {
            @Override
            public void run() {
                var newHotObject = new HotObject();
                newHotObject.print();
            }
        });
    }

}
