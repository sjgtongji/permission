package com.duofuen.permission.domain.entity;

import com.duofuen.permission.common.Constant;

import javax.persistence.*;

@Entity
@Table(name = "dk_table_order_goods")
public class DKTableOrderGoods {
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

    private Double price;

    private Integer quality;

    private Double totalPrice;



    @Column(name = "dk_catagory_id")
    private Integer dkCatagoryId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dk_catagory_id", insertable = false, updatable = false)
    private DKCatagory dkCatagory;

    private String dkCatagoryName;


    @Column(name = "dk_goods_id")
    private Integer dkGoodsId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dk_goods_id", insertable = false, updatable = false)
    private DKGoods dkGoods;


    private String dkGoodsName;


    @Column(name = "dk_table_order_id")
    private Integer dkTableOrderId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dk_table_order_id", insertable = false, updatable = false)
    private DKTableOrder dkTableOrder;


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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuality() {
        return quality;
    }

    public void setQuality(Integer quality) {
        this.quality = quality;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
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

    public Integer getDkGoodsId() {
        return dkGoodsId;
    }

    public void setDkGoodsId(Integer dkGoodsId) {
        this.dkGoodsId = dkGoodsId;
    }

    public DKGoods getDkGoods() {
        return dkGoods;
    }

    public void setDkGoods(DKGoods dkGoods) {
        this.dkGoods = dkGoods;
    }

    public String getDkGoodsName() {
        return dkGoodsName;
    }

    public void setDkGoodsName(String dkGoodsName) {
        this.dkGoodsName = dkGoodsName;
    }

    public Integer getDkTableOrderId() {
        return dkTableOrderId;
    }

    public void setDkTableOrderId(Integer dkTableOrderId) {
        this.dkTableOrderId = dkTableOrderId;
    }

    public DKTableOrder getDkTableOrder() {
        return dkTableOrder;
    }

    public void setDkTableOrder(DKTableOrder dkTableOrder) {
        this.dkTableOrder = dkTableOrder;
    }
}
