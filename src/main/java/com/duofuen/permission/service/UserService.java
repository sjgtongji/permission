package com.duofuen.permission.service;

import com.duofuen.permission.controller.bean.LoginRequest;
import com.duofuen.permission.controller.bean.LoginResponse;
import com.duofuen.permission.domain.entity.User;
import com.duofuen.permission.domain.repo.ProjectRepo;
import com.duofuen.permission.domain.repo.UserRepo;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService{
    private final UserRepo userRepo;
    private final ProjectRepo projectRepo;
    public UserService(UserRepo userRepo, ProjectRepo projectRepo) {
        this.userRepo = userRepo;
        this.projectRepo = projectRepo;
    }

    public LoginResponse login(LoginRequest request) {
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setCode(200);
        loginResponse.setMessage("success");
        loginResponse.getData().setToken("111");
        loginResponse.getData().setDeviceId(request.getUserName());
        loginResponse.getData().setDeviceNo(111);
        return loginResponse;
    }

    public Optional<User> queryById(Integer userId){
        return userRepo.findById(userId);
    }

    public User save(User user){
        return userRepo.save(user);
    }

    public Optional<User> findByUsernameAndPassword(String username, String password){
        return userRepo.findByUserNameAndPassword(username , password);
    }

}
