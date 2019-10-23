package com.ais.sys.models;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Picture extends BaseModel implements Serializable{
	
	private static final long serialVersionUID = -8093239217865869608L;

	private Integer id;

    private String picSubPath;
    
    private String mainPicture;
    
    private String middlePicture;
    
    private String largePicture;
    
    private String picName;

    private String picSubMinPath;

    private Double picSize;

    private Double width;

    private Double length;

    private String location;
    
    private String host;
    
    private String protocol;
    
    private String picDesc; // 图片描述
    
    public Picture(Integer page, Integer size) {
    	setPage(page);
    	setSize(size);
    }

    public Picture(Integer id, String picSubPath, String picName, String picSubMinPath, Date createTime, String createBy, Date updateTime, String lastUpdateBy, Double picSize, Double width, Double length, String location) {
        this.id = id;
        this.picSubPath = picSubPath;
        this.picName = picName;
        this.picSubMinPath = picSubMinPath;
        setCreatedDate(createTime);
        setCreatedBy(createBy);
        setLastUpdatedDate(updateTime);
        setLastUpdatedBy(lastUpdateBy);
        this.picSize = picSize;
        this.width = width;
        this.length = length;
        this.location = location;
    }
    
    public Picture(Integer id, String picSubPath, String picName, String picSubMinPath, Date createTime, String createBy, Date updateTime, String lastUpdateBy, Double picSize, Double width, Double length, String location, String picDesc) {
        this.id = id;
        this.picSubPath = picSubPath;
        this.picName = picName;
        this.picSubMinPath = picSubMinPath;
        setCreatedDate(createTime);
        setCreatedBy(createBy);
        setLastUpdatedDate(updateTime);
        setLastUpdatedBy(lastUpdateBy);
        this.picSize = picSize;
        this.width = width;
        this.length = length;
        this.location = location;
        this.picDesc = picDesc;
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

	public String getPicDesc() {
		return picDesc;
	}

	public void setPicDesc(String picDesc) {
		this.picDesc = picDesc;
	}

	@Override
	public String toString() {
		return "Picture [id=" + id + ", picSubPath=" + picSubPath + ", mainPicture=" + mainPicture + ", middlePicture="
				+ middlePicture + ", largePicture=" + largePicture + ", picName=" + picName + ", picSubMinPath="
				+ picSubMinPath + ", picSize=" + picSize + ", width=" + width + ", length=" + length + ", location="
				+ location + ", host=" + host + ", protocol=" + protocol + ", picDesc=" + picDesc + "]";
	}
    
}