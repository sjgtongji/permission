package com.duofuen.permission.service.api;

import com.duofuen.permission.controller.bean.LoginRequest;
import com.duofuen.permission.controller.bean.LoginResponse;

public interface IUserService {
    LoginResponse login(LoginRequest request);
}
