package com.duofuen.permission.domain.entity;

import com.duofuen.permission.common.Constant;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "dk_table_order")
public class DKTableOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer version = Constant.VERSION;
    private long createTime;
    private long updateTime;
    private Integer sort = Constant.SORT;

    @Column(name = "dk_store_id")
    private Integer dkStoreId;
    private boolean deleted = false;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dk_store_id", insertable = false, updatable = false)
    private DKStore dkStore;

    private String dkStoreName;

    private long startTime;
    private long endTime;

    @Column(name = "dk_table_id")
    private Integer dkTableId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dk_table_id", insertable = false, updatable = false)
    private DKTable dkTable;

    private String dkTableName;

    private boolean member;

    @Column(name = "dk_member_id")
    private Integer dkMemberId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dk_member_id", insertable = false, updatable = false)
    private DKMember dkMember;

    private String dkMemberName;
    private String dkMemberPhone;

    private Double valueByCash;
    private Double valueByCard;
    private Double valueForMinus;
    private Double originValue;
    private String minusDesc;
    private boolean opened;

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Double getOriginValue() {
        return originValue;
    }

    public void setOriginValue(Double originValue) {
        this.originValue = originValue;
    }

    public String getMinusDesc() {
        return minusDesc;
    }

    public void setMinusDesc(String minusDesc) {
        this.minusDesc = minusDesc;
    }

    public boolean isOpened() {
        return opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
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

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public Integer getDkTableId() {
        return dkTableId;
    }

    public void setDkTableId(Integer dkTableId) {
        this.dkTableId = dkTableId;
    }

    public DKTable getDkTable() {
        return dkTable;
    }

    public void setDkTable(DKTable dkTable) {
        this.dkTable = dkTable;
    }

    public String getDkTableName() {
        return dkTableName;
    }

    public void setDkTableName(String dkTableName) {
        this.dkTableName = dkTableName;
    }

    public boolean isMember() {
        return member;
    }

    public void setMember(boolean member) {
        this.member = member;
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

    public String getDkMemberPhone() {
        return dkMemberPhone;
    }

    public void setDkMemberPhone(String dkMemberPhone) {
        this.dkMemberPhone = dkMemberPhone;
    }

    public Double getValueByCash() {
        return valueByCash;
    }

    public void setValueByCash(Double valueByCash) {
        this.valueByCash = valueByCash;
    }

    public Double getValueByCard() {
        return valueByCard;
    }

    public void setValueByCard(Double valueByCard) {
        this.valueByCard = valueByCard;
    }

    public Double getValueForMinus() {
        return valueForMinus;
    }

    public void setValueForMinus(Double valueForMinus) {
        this.valueForMinus = valueForMinus;
    }
}
