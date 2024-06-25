package com.duofuen.permission.controller.bean;

import com.fasterxml.jackson.annotation.JsonInclude;

public class ModifyDKMemberResponse extends BaseResponse<ModifyDKMemberResponse.Data>  {
    public ModifyDKMemberResponse() {
        super();
        this.data = new ModifyDKMemberResponse.Data();
    }
    public class Data {
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Integer id;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }
    }
}
