package com.duofuen.permission.controller.bean;

import com.fasterxml.jackson.annotation.JsonInclude;

public class CreateDKMemberResponse  extends BaseResponse<CreateDKMemberResponse.Data>{
    public CreateDKMemberResponse() {
        super();
        this.data = new CreateDKMemberResponse.Data();
    }

    public class Data {
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Integer dkMemberId;

        public Integer getDkMemberId() {
            return dkMemberId;
        }

        public void setDkMemberId(Integer dkMemberId) {
            this.dkMemberId = dkMemberId;
        }
    }
}
