package com.ais.sys.models;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Picture extends BaseModel{
    private Integer id;

    private String picSubPath;
    
    private String mainPicture;
    
    private String middlePicture;
    
    private String largePicture;
    
    private String picName;

    private String picSubMinPath;

    private Date createTime;

    private String createBy;

    private Date updateTime;

    private String lastUpdateBy;

    private Double picSize;

    private Double width;

    private Double length;

    private String location;
    
    private String host;
    
    private String protocol;
    
    public Picture(Integer page, Integer size) {
    	setPage(page);
    	setSize(size);
    }

    public Picture(Integer id, String picSubPath, String picName, String picSubMinPath, Date createTime, String createBy, Date updateTime, String lastUpdateBy, Double picSize, Double width, Double length, String location) {
        this.id = id;
        this.picSubPath = picSubPath;
        this.picName = picName;
        this.picSubMinPath = picSubMinPath;
        this.createTime = createTime;
        this.createBy = createBy;
        this.updateTime = updateTime;
        this.lastUpdateBy = lastUpdateBy;
        this.picSize = picSize;
        this.width = width;
        this.length = length;
        this.location = location;
    }

    public Picture() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPicSubPath() {
        return picSubPath;
    }

    public void setPicSubPath(String picSubPath) {
        this.picSubPath = picSubPath == null ? null : picSubPath.trim();
    }

    public String getPicName() {
        return picName;
    }

    public void setPicName(String picName) {
        this.picName = picName == null ? null : picName.trim();
    }

    public String getPicSubMinPath() {
        return picSubMinPath;
    }

    public void setPicSubMinPath(String picSubMinPath) {
        this.picSubMinPath = picSubMinPath == null ? null : picSubMinPath.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy == null ? null : createBy.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy == null ? null : lastUpdateBy.trim();
    }

    public Double getPicSize() {
        return picSize;
    }

    public void setPicSize(Double picSize) {
        this.picSize = picSize;
    }

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location == null ? null : location.trim();
    }

	public String getMainPicture() {
		return mainPicture;
	}

	public void setMainPicture(String mainPicture) {
		this.mainPicture = mainPicture;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getMiddlePicture() {
		return middlePicture;
	}

	public String getLargePicture() {
		return largePicture;
	}

	public void setMiddlePicture(String middlePicture) {
		this.middlePicture = middlePicture;
	}

	public void setLargePicture(String largePicture) {
		this.largePicture = largePicture;
	}
    
}