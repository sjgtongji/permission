package com.duofuen.permission.service;

import com.duofuen.permission.common.ErrorNum;
import com.duofuen.permission.controller.bean.DKLoginRequest;
import com.duofuen.permission.controller.bean.DKLoginResponse;
import com.duofuen.permission.domain.entity.DKUser;
import com.duofuen.permission.domain.entity.RestToken;
import com.duofuen.permission.domain.entity.User;
import com.duofuen.permission.domain.repo.DKStoreRepo;
import com.duofuen.permission.domain.repo.DKUserRepo;
import com.duofuen.permission.domain.repo.RestTokenRepo;
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
import java.util.Optional;
import java.util.UUID;

@Service
public class DKUserService {
    private final DKUserRepo userRepo;
    private final RestTokenRepo restTokenRepo;
    private final DKStoreRepo storeRepo;
    private static Logger log = LogManager.getLogger();
    public DKUserService(DKUserRepo userRepo, RestTokenRepo restTokenRepo, DKStoreRepo storeRepo) {
        this.userRepo = userRepo;
        this.restTokenRepo = restTokenRepo;
        this.storeRepo = storeRepo;
    }
    public DKUser save(DKUser user){
        return userRepo.save(user);
    }
    public DKLoginResponse login(DKLoginRequest request) {
        log.info("登录", request);
        DKLoginResponse response = new DKLoginResponse();
        if(request.getUserName() == null || request.getUserName().equals("")){
            response.setResult(ErrorNum.INVALID_PARAM_USERNAME);
            return response;
        }

        if(request.getPassword() == null || request.getPassword().equals("")){
            response.setResult(ErrorNum.INVALID_PARAM_PWD);
            return response;
        }
        Optional<DKUser> userOptional = findByUsernameAndPassword(request.getUserName() , request.getPassword());
        if(!userOptional.isPresent()){
            response.setResult(ErrorNum.INVALID_PARAM_USERNAME);
            return response;
        }
        DKUser user = userOptional.get();
        response.getData().setToken(updateToken(user.getId()));
        response.getData().setUserId(user.getId());
        return response;
    }

//    public Optional<User> queryById(Integer userId){
//        return userRepo.findById(userId);
//    }
//

    public Optional<DKUser> findByUsernameAndPassword(String username, String password){
        return userRepo.findByUserNameAndPassword(username , password);
    }

    @Transactional
    public String updateToken(Integer userId) {
        Date expireTime = Date.from(Instant.now().plusSeconds(1800));
        Optional<RestToken> restToken = restTokenRepo.findByUserId(userId);
        RestToken token = null;
        if(!restToken.isPresent()){
            token = new RestToken();
            token.setUserId(userId);
            token.setToken(Base64Utils.encodeToString(UUID.randomUUID().toString().getBytes()));
            token.setExpireTime(expireTime);
            restTokenRepo.save(token);
        }else{
            token = restToken.get();
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
            restTokenRepo.save(token);
        }

        log.info("rest token updated deviceId {}, token {}， expireTime {}", userId, token.getToken(), expireTime);

        return token.getToken();
    }
//
//    public Page<User> findAll(Specification<User> specification, Pageable pageable) {
//        return userRepo.findAll(specification , pageable);
//    }
//
//    public long count(Specification<User> specification){
//        return userRepo.count(specification);
//    }
//
    public Optional<DKUser> findByIdAndDeleted(Integer id , boolean deleted){
        return userRepo.findByIdAndDeleted(id , deleted);
    }
//
//    public List<User> findAllByIdInAndDeleted(List<Integer> ids , boolean deleted){
//        return userRepo.findAllByIdInAndDeleted(ids , deleted);
//    }
//
//    public List<User> saveAll(List<User> users){
//        return userRepo.saveAll(users);
//    }
//
    public boolean tokenValid(String token){
        Optional<RestToken> restToken = restTokenRepo.findByToken(token);
        if(!restToken.isPresent()){
            return false;
        }
        return restToken.get().getExpireTime().after(Date.from(Instant.now()));
    }

    public boolean isAdmin(String token){
        DKUser user = findUserByToken(token);
        if(user == null){
            return false;
        }
        return user.isAdmin();
    }

    public DKUser findUserByToken(String token){
        if(!tokenValid(token)){
            return null;
        }
        Optional<RestToken> restToken = restTokenRepo.findByToken(token);
        if(!restToken.isPresent()){
            return null;
        }
        Integer userId = restToken.get().getUserId();
        Optional<DKUser> user = userRepo.findByIdAndDeleted(userId , false);
        if(!user.isPresent()){
            return null;
        }
        return user.get();
    }

    public Page<DKUser> findAll(Specification<DKUser> specification, Pageable pageable) {
        return userRepo.findAll(specification , pageable);
    }

    public long count(Specification<DKUser> specification){
        return userRepo.count(specification);
    }
}
