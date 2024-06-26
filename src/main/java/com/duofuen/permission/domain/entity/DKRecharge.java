package com.duofuen.permission.domain.entity;

import com.duofuen.permission.common.Constant;

import javax.persistence.*;
@Entity
@Table(name = "dk_recharge")
public class DKRecharge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer version = Constant.VERSION;
    private long createTime;
    private long updateTime;
    private Integer sort = Constant.SORT;

    private boolean deleted = false;

    @Column(name = "dk_store_id")
    private Integer dkStoreId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dk_store_id", insertable = false, updatable = false)
    private DKStore dkStore;

    private String dkStoreName;

    @Column(name = "dk_member_id")
    private Integer dkMemberId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dk_member_id", insertable = false, updatable = false)
    private DKMember dkMember;

    private String dkMemberName;

    private String dkMemberPhone;

    @Column(name = "dk_user_id")
    private Integer dkUserId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dk_user_id", insertable = false, updatable = false)
    private DKUser dkUser;

    private String dkUserName;

    private Double rechargeValue;
    private Double extraValue;
    private Double totalValue;
    private Double valueBeforeRecharge;
    private Double valueAfterRecharge;


    public String getDkMemberPhone() {
        return dkMemberPhone;
    }

    public void setDkMemberPhone(String dkMemberPhone) {
        this.dkMemberPhone = dkMemberPhone;
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

    public Integer getDkMemberId() {
        return dkMemberId;
    }

    public void setDkMemberId(Integer dkMemberId) {
        this.dkMemberId = dkMemberId;
    }

    public DKMember getDkMember() {
        return dkMember;
    }

    public void setDkMember(DKMember dkMember) {
        this.dkMember = dkMember;
    }

    public String getDkMemberName() {
        return dkMemberName;
    }

    public void setDkMemberName(String dkMemberName) {
        this.dkMemberName = dkMemberName;
    }

    public Integer getDkUserId() {
        return dkUserId;
    }

    public void setDkUserId(Integer dkUserId) {
        this.dkUserId = dkUserId;
    }

    public DKUser getDkUser() {
        return dkUser;
    }

    public void setDkUser(DKUser dkUser) {
        this.dkUser = dkUser;
    }

    public String getDkUserName() {
        return dkUserName;
    }

    public void setDkUserName(String dkUserName) {
        this.dkUserName = dkUserName;
    }

    public Double getRechargeValue() {
        return rechargeValue;
    }

    public void setRechargeValue(Double rechargeValue) {
        this.rechargeValue = rechargeValue;
    }

    public Double getExtraValue() {
        return extraValue;
    }

    public void setExtraValue(Double extraValue) {
        this.extraValue = extraValue;
    }

    public Double getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(Double totalValue) {
        this.totalValue = totalValue;
    }

    public Double getValueBeforeRecharge() {
        return valueBeforeRecharge;
    }

    public void setValueBeforeRecharge(Double valueBeforeRecharge) {
        this.valueBeforeRecharge = valueBeforeRecharge;
    }

    public Double getValueAfterRecharge() {
        return valueAfterRecharge;
    }

    public void setValueAfterRecharge(Double valueAfterRecharge) {
        this.valueAfterRecharge = valueAfterRecharge;
    }
}
