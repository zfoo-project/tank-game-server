package com.zfoo.tank.admin.controller;

import com.zfoo.net.NetContext;
import com.zfoo.net.packet.common.Message;
import com.zfoo.protocol.util.AssertionUtils;
import com.zfoo.protocol.util.StringUtils;
import com.zfoo.tank.admin.model.gm.ChangeCurrencyRequest;
import com.zfoo.tank.admin.model.gm.ChangePlayerLevelRequest;
import com.zfoo.tank.admin.service.LoginService;
import com.zfoo.tank.common.protocol.admin.AdminCurrencyAsk;
import com.zfoo.tank.common.protocol.admin.AdminPlayerLevelAsk;
import com.zfoo.tank.common.result.BaseResponse;
import com.zfoo.tank.common.result.CodeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author yidingzhao
 * @version 1.0
 * @since 2021-07-06 17:54
 */
@Controller
@CrossOrigin
public class GmController {
    private static final Logger logger = LoggerFactory.getLogger(GmController.class);

    @Autowired
    private LoginService loginService;

    @PostMapping(value = "/api/gm/playerLevel", consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public BaseResponse playerLevel(HttpServletRequest request, @RequestBody ChangePlayerLevelRequest cm) {
        var adminEntity = loginService.adminUserInfo(request);
        AssertionUtils.notNull(adminEntity);

        var userName = cm.getUserName();
        var userId = loginService.getUserIdByAccount(userName);
        if (userId <= 0) {
            return BaseResponse.valueOf(CodeEnum.FAIL, StringUtils.format("用户[userName:{}]不存在", userName));
        }

        try {
            var message = NetContext.getConsumer().syncAsk(AdminPlayerLevelAsk.valueOf(userId, cm.getPlayerLevel(),cm.getExp())
                    , Message.class, userId).packet();
            return BaseResponse.valueOf(CodeEnum.OK, message);
        } catch (Exception e) {
            logger.error("玩家等级修改gm发生未知异常", e);
            return BaseResponse.valueOf(CodeEnum.FAIL, e.toString());
        }
    }

    @PostMapping(value = "/api/gm/currency", consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public BaseResponse currency(HttpServletRequest request, @RequestBody ChangeCurrencyRequest cm) {
        var adminEntity = loginService.adminUserInfo(request);
        AssertionUtils.notNull(adminEntity);

        var userName = cm.getUserName();
        var userId = loginService.getUserIdByAccount(userName);
        if (userId <= 0) {
            return BaseResponse.valueOf(CodeEnum.FAIL, StringUtils.format("用户[userName:{}]不存在", userName));
        }

        try {
            var message = NetContext.getConsumer().syncAsk(AdminCurrencyAsk.valueOf(userId, cm.getGold(),cm.getGem(),cm.getEnergy()), Message.class, userId).packet();
            return BaseResponse.valueOf(CodeEnum.OK, message);
        } catch (Exception e) {
            logger.error("货币修改gm发生未知异常", e);
            return BaseResponse.valueOf(CodeEnum.FAIL, e.toString());
        }
    }
}
