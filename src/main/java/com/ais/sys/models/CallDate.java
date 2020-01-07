package com.ais.sys.models;

import java.util.Date;


public class CallDate extends BaseModel {

    private Integer id;

    private Date execDate;

    private Integer waitSecond;

    private Integer status;

    private String createBy;

    private String lastUpdateBy;

    private Date createDate;

    private Date lastUpdateDate;

    private String remark;

    private String email;

    public CallDate(Integer status, String createBy) {
        this.status = status;
        this.createBy = createBy;
    }

    public CallDate(Integer id, Date execDate, Integer waitSecond, Integer status, String createBy, String lastUpdateBy, Date createDate, Date lastUpdateDate, String remark) {
        this.id = id;
        this.execDate = execDate;
        this.waitSecond = waitSecond;
        this.status = status;
        this.createBy = createBy;
        this.lastUpdateBy = lastUpdateBy;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
        this.remark = remark;
    }

    public CallDate() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getExecDate() {
        return execDate;
    }

    public void setExecDate(Date execDate) {
        this.execDate = execDate;
    }

    public Integer getWaitSecond() {
        return waitSecond;
    }

    public void setWaitSecond(Integer waitSecond) {
        this.waitSecond = waitSecond;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}