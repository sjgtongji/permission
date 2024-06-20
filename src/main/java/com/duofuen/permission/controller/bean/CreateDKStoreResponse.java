package com.duofuen.permission.controller.bean;

import com.fasterxml.jackson.annotation.JsonInclude;

public class CreateDKStoreResponse extends BaseResponse<CreateDKStoreResponse.Data>{

    public CreateDKStoreResponse() {
        super();
        this.data = new Data();
    }

    public class Data {

    }
}
