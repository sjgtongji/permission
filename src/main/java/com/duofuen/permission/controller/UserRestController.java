package com.duofuen.permission.controller;

import com.duofuen.permission.controller.bean.LoginRequest;
import com.duofuen.permission.controller.bean.LoginResponse;
import com.duofuen.permission.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/rest/user")
public class UserRestController {
    private static Logger log = LogManager.getLogger();

    private final UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @Transactional
    @PostMapping("/login")
    @ResponseBody
    public LoginResponse login(@RequestBody LoginRequest request) {
        try {
            log.info("手机登录, {}", request);
            LoginResponse response =  userService.login(request);
            log.info("登陆成功！");
            return response;
        } catch (Exception e) {
            log.error("登陆失败！");
            log.error(e);
            LoginResponse response = new LoginResponse();
            response.setErrorCode(-1);
            response.setErrorMessage(e.getMessage());
            return response;
        }
    }
}
