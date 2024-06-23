package com.duofuen.permission.domain.entity;

import com.duofuen.permission.common.Constant;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "dk_user")
public class DKUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer version = Constant.VERSION;
    private long createTime;
    private long updateTime;
    private Integer sort = Constant.SORT;

    @Column(nullable = false)
    private String userName;
    @JsonIgnore
    @Column(nullable = false)
    private String password;
    private String phone;
    private String email;

    private boolean admin = false;
    @Column(name = "dk_role_id")
    private Integer dkRoleId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dk_role_id", insertable = false, updatable = false)
    private DKRole dkRole;

    private String dkRoleName;

    private boolean deleted = false;

    @Column(name = "dk_store_id")
    private Integer dkStoreId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dk_store_id", insertable = false, updatable = false)
    private DKStore dkStore;

    private String dkStoreName;


    public DKRole getDkRole() {
        return dkRole;
    }

    public void setDkRole(DKRole dkRole) {
        this.dkRole = dkRole;
    }

    public String getDkRoleName() {
        return dkRoleName;
    }

    public void setDkRoleName(String dkRoleName) {
        this.dkRoleName = dkRoleName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getDkRoleId() {
        return dkRoleId;
    }

    public void setDkRoleId(Integer dkRoleId) {
        this.dkRoleId = dkRoleId;
    }


    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Integer getDkStoreId() {
        return dkStoreId;
    }

    public void setDkStoreId(Integer dkStoreId) {
        this.dkStoreId = dkStoreId;
    }

    public DKStore getDkStore() {
        return dkStore;
    }

    public void setDkStore(DKStore dkStore) {
        this.dkStore = dkStore;
    }

    public String getDkStoreName() {
        return dkStoreName;
    }

    public void setDkStoreName(String dkStoreName) {
        this.dkStoreName = dkStoreName;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}
