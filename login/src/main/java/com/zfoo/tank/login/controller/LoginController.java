package com.zfoo.tank.login.controller;

import com.zfoo.orm.OrmContext;
import com.zfoo.orm.util.MongoIdUtils;
import com.zfoo.scheduler.util.TimeUtils;
import com.zfoo.tank.common.entity.AccountEntity;
import com.zfoo.tank.common.entity.PlayerEntity;
import com.zfoo.tank.common.result.BaseResponse;
import com.zfoo.tank.common.result.CodeEnum;
import com.zfoo.tank.common.util.HttpLoginUtils;
import com.zfoo.tank.login.config.WebConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import static com.zfoo.tank.common.util.HttpLoginUtils.*;
/**
 * @author sun
 */
@Controller
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private WebConfiguration webConfiguration;

    @PostMapping(value = "/api/login")
    @ResponseBody
    public BaseResponse login(@RequestBody HttpLoginRequest request) {
        var token = request.getToken();
        var type = request.getType();
        var platform = request.getPlatform();
        var version = request.getVersion();
        log.info("login [{}] [{}] [{}] [{}]", token, type, platform, version);


        // 默认登录token就是玩家名称
        var account = token;
        var name = token;

        // 创建玩家的流程，这里只给一个玩家id，具体的创建放在home
        var accountEntity = OrmContext.getAccessor().load(account, AccountEntity.class);
        if (accountEntity == null) {
            var id = MongoIdUtils.getIncrementIdFromMongoDefault(PlayerEntity.class);
            var entity = AccountEntity.valueOf(account, name, "", id);
            var insertFlag = OrmContext.getAccessor().insert(entity);
            if (!insertFlag) {
                return BaseResponse.valueOf(CodeEnum.login_19);
            }
            accountEntity = entity;
        }

        var uid = accountEntity.getUid();
        var myToken = HttpLoginUtils.toToken(uid, name);

        log.info("登录成功[uid:{}][token:{}][myToken:{}]", uid, token, myToken);
        return BaseResponse.valueOf(CodeEnum.OK, new HttpLoginResult(myToken, TimeUtils.now(), webConfiguration.getConnectUrls()));
    }

}
