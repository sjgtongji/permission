package com.duofuen.permission.service;

import com.duofuen.permission.common.ErrorNum;
import com.duofuen.permission.controller.bean.LoginRequest;
import com.duofuen.permission.controller.bean.LoginResponse;
import com.duofuen.permission.domain.entity.Project;
import com.duofuen.permission.domain.entity.RestToken;
import com.duofuen.permission.domain.entity.User;
import com.duofuen.permission.domain.repo.ProjectRepo;
import com.duofuen.permission.domain.repo.RestTokenRepo;
import com.duofuen.permission.domain.repo.UserRepo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService{
    private final UserRepo userRepo;
    private final ProjectRepo projectRepo;
    private final RestTokenRepo restTokenRepo;
    private static Logger log = LogManager.getLogger();
    public UserService(UserRepo userRepo, ProjectRepo projectRepo, RestTokenRepo restTokenRepo) {
        this.userRepo = userRepo;
        this.projectRepo = projectRepo;
        this.restTokenRepo = restTokenRepo;
    }

    public LoginResponse login(LoginRequest request) {
        log.info("登录", request);
        LoginResponse response = new LoginResponse();
        if(request.getUserName() == null || request.getUserName().equals("")){
            response.setResult(ErrorNum.INVALID_PARAM_USERNAME);
            return response;
        }

        if(request.getPassword() == null || request.getPassword().equals("")){
            response.setResult(ErrorNum.INVALID_PARAM_PWD);
            return response;
        }
        Optional<User> userOptional = findByUsernameAndPassword(request.getUserName() , request.getPassword());
        if(!userOptional.isPresent()){
            response.setResult(ErrorNum.INVALID_PARAM_USERNAME);
            return response;
        }
        User user = userOptional.get();
        response.getData().setToken(updateToken(user.getId()));
        response.getData().setUserId(user.getId());
        return response;
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

    @Transactional
    public String updateToken(Integer userId) {
        Date expireTime = Date.from(Instant.now().plusSeconds(1800));
        Optional<RestToken> restToken = restTokenRepo.findByUserId(userId);
        RestToken token = restToken.get();
        if (null != token) {
            // if not expire, update expireTime
            // if expired, generate new token and update expireTime
            if (Instant.now().isAfter(restToken.get().getExpireTime().toInstant())) {
                String newToken = Base64Utils.encodeToString(UUID.randomUUID().toString().getBytes());
                token.setToken(newToken);
            }
            restToken.get().setExpireTime(expireTime);
        } else {
            token = new RestToken();

            token.setUserId(userId);
            token.setToken(Base64Utils.encodeToString(UUID.randomUUID().toString().getBytes()));
            token.setExpireTime(expireTime);
        }
        log.info("rest token updated deviceId {}, token {}， expireTime {}", userId, token.getToken(), expireTime);
        restTokenRepo.save(token);
        return token.getToken();
    }

    public Page<User> findAll(Specification<User> specification, Pageable pageable) {
        return userRepo.findAll(specification , pageable);
    }

    public long count(Specification<User> specification){
        return userRepo.count(specification);
    }

    public Optional<User> findByIdAndDeleted(Integer id , boolean deleted){
        return userRepo.findByIdAndDeleted(id , deleted);
    }

    public List<User> findAllByIdInAndDeleted(List<Integer> ids , boolean deleted){
        return userRepo.findAllByIdInAndDeleted(ids , deleted);
    }

    public List<User> saveAll(List<User> users){
        return userRepo.saveAll(users);
    }

    public boolean tokenValid(String token){
        Optional<RestToken> restToken = restTokenRepo.findByToken(token);
        if(!restToken.isPresent()){
            return false;
        }
        return restToken.get().getExpireTime().before(Date.from(Instant.now()));
    }


}
