package com.duofuen.permission.domain.entity;

import com.duofuen.permission.common.Constant;

import javax.persistence.*;
@Entity
@Table(name = "dk_goods")
public class DKGoods {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer version = Constant.VERSION;
    private long createTime;
    private long updateTime;
    private Integer sort = Constant.SORT;

    @Column(nullable = false)
    private String name;

    private boolean deleted = false;

    @Column(name = "dk_store_id")
    private Integer dkStoreId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dk_store_id", insertable = false, updatable = false)
    private DKStore dkStore;

    private String dkStoreName;

    private Double price;

    private Integer exist;



    @Column(name = "dk_catagory_id")
    private Integer dkCatagoryId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dk_catagory_id", insertable = false, updatable = false)
    private DKCatagory dkCatagory;

    private String dkCatagoryName;

    private boolean payByCash = true;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getExist() {
        return exist;
    }

    public void setExist(Integer exist) {
        this.exist = exist;
    }

    public Integer getDkCatagoryId() {
        return dkCatagoryId;
    }

    public void setDkCatagoryId(Integer dkCatagoryId) {
        this.dkCatagoryId = dkCatagoryId;
    }

    public DKCatagory getDkCatagory() {
        return dkCatagory;
    }

    public void setDkCatagory(DKCatagory dkCatagory) {
        this.dkCatagory = dkCatagory;
    }

    public String getDkCatagoryName() {
        return dkCatagoryName;
    }

    public void setDkCatagoryName(String dkCatagoryName) {
        this.dkCatagoryName = dkCatagoryName;
    }

    public boolean isPayByCash() {
        return payByCash;
    }

    public void setPayByCash(boolean payByCash) {
        this.payByCash = payByCash;
    }
}
