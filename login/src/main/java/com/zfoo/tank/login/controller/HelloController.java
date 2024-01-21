package com.zfoo.tank.login.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author sun
 */
@Controller
public class HelloController {

    @RequestMapping(value = "/hello")
    @ResponseBody
    public String hello() {
        return "world";
    }

}
