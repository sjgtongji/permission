package com.duofuen.permission.service;

import com.duofuen.permission.controller.bean.LoginRequest;
import com.duofuen.permission.controller.bean.LoginResponse;
import com.duofuen.permission.service.api.IUserService;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {
    @Override
    public LoginResponse login(LoginRequest request) {
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setErrorCode(200);
        loginResponse.setErrorMessage("success");
        loginResponse.getData().setToken("111");
        loginResponse.getData().setDeviceId("111");
        loginResponse.getData().setDeviceNo(111);
        return loginResponse;
    }
}
