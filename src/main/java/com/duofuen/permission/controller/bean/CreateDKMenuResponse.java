package com.duofuen.permission.controller.bean;

import com.fasterxml.jackson.annotation.JsonInclude;

public class CreateDKMenuResponse extends BaseResponse<CreateDKMenuResponse.Data>{
    public CreateDKMenuResponse() {
        super();
        this.data = new CreateDKMenuResponse.Data();
    }

    public class Data {
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Integer menuId;

        public Integer getMenuId() {
            return menuId;
        }

        public void setMenuId(Integer menuId) {
            this.menuId = menuId;
        }
    }
}
