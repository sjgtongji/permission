package com.duofuen.permission.controller.bean;

import com.fasterxml.jackson.annotation.JsonInclude;

public class CreateProjectResponse extends  BaseResponse<CreateProjectResponse.Data>{

    public CreateProjectResponse() {
        super();
        this.data = new CreateProjectResponse.Data();
    }

    public class Data {
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Integer projectId;

        public Integer getProjectId() {
            return projectId;
        }

        public void setProjectId(Integer projectId) {
            this.projectId = projectId;
        }
    }
}
