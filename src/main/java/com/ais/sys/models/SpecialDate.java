package com.ais.sys.models;

import java.util.Date;

public class SpecialDate {
    private Integer id;

    private Date specialDate;

    private String name;

    private Integer type;

    private String createBy;

    private String lastUpdateBy;

    private Date createDate;

    private Date lastUpdateDate;

    private String remark;

    public SpecialDate(Integer id, Date specialDate, String name, Integer type, String createBy, String lastUpdateBy, Date createDate, Date lastUpdateDate, String remark) {
        this.id = id;
        this.specialDate = specialDate;
        this.name = name;
        this.type = type;
        this.createBy = createBy;
        this.lastUpdateBy = lastUpdateBy;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
        this.remark = remark;
    }

    public SpecialDate() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getSpecialDate() {
        return specialDate;
    }

    public void setSpecialDate(Date specialDate) {
        this.specialDate = specialDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy == null ? null : createBy.trim();
    }

    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy == null ? null : lastUpdateBy.trim();
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}