package com.zfoo.asimple;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author godotg
 */
@RestController
public class MyController {

    @RequestMapping("/")
    String home() {
        return "Hello World Native!";
    }

}
