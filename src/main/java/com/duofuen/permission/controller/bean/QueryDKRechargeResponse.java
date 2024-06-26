package com.duofuen.permission.controller.bean;

import com.duofuen.permission.domain.entity.DKMember;
import com.duofuen.permission.domain.entity.DKRecharge;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

public class QueryDKRechargeResponse extends BaseResponse<QueryDKRechargeResponse.Data>{
    public QueryDKRechargeResponse(){
        super();
        this.data = new QueryDKRechargeResponse.Data();
    }
    public class Data {
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private List<DKRecharge> data = new ArrayList<>();

        private int current = 1;

        private int pageSize = 20;

        private boolean success = false;

        private int total = 1;

        public List<DKRecharge> getData() {
            return data;
        }

        public void setData(List<DKRecharge> data) {
            this.data = data;
        }

        public int getCurrent() {
            return current;
        }

        public void setCurrent(int current) {
            this.current = current;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }
    }
}
