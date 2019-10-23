package com.ais.sys.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ais.sys.cache.RedisCache;
import com.ais.sys.cache.RedisEvict;
import com.ais.sys.daos.PictureMapper;
import com.ais.sys.exception.ServiceException;
import com.ais.sys.models.Picture;
import com.ais.sys.utils.ConstantUtil;
import com.ais.sys.utils.PropertiesUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service("pictureService")
public class PictureService {
	@Autowired
	PictureMapper pictureMapper;
	
	@RedisCache(type = Picture.class)
	public PageInfo<Picture> search(final Picture picture) {
		PageInfo<Picture> pageInfo = null;
		pageInfo = PageHelper.startPage(picture.getPage(), picture.getSize())
				.doSelectPageInfo(() -> pictureMapper.selectByModel(picture));
		setMainPicturePath(pageInfo.getList());
		return pageInfo;
	}
	
	@RedisCache(type = Picture.class)
	public List<Picture> list(Picture picture) {
		List<Picture> picList = null;
			picList = pictureMapper.selectByModel(picture);
			setMainPicturePath(picList);
		return picList;
	}
	
	@RedisEvict(type = Picture.class)
	public void deleteInfo(Picture[] pictures) throws ServiceException {
		for(Picture picture : pictures) {
			if(picture.getId() != null) {
				Picture picInfo = pictureMapper.selectByPrimaryKey(picture.getId());
				if(picInfo != null) {
					try {
						deleteFile(picture);
					} catch (IOException e) {
						
						throw new ServiceException("删除文件失败：" + e.getMessage());
					}
				}
				pictureMapper.deleteByPrimaryKey(picture.getId());
			} else {
				throw new ServiceException("id 不能为空");
			}
		}
	}
	
	private void deleteFile(Picture picture) throws IOException {
		String path = PropertiesUtils.getValue(ConstantUtil.PropertiesKeys.IMAGE_ROOT_PATH);
		if(picture.getPicSubMinPath() != null) {
			String[] imgs = picture.getPicSubMinPath().split(",");
			if(imgs != null && imgs.length > 0) {
				for(String img : imgs) {
					if(StringUtils.isNotBlank(img)) {
						String imgPath = path + img;
						try {
							FileUtils.forceDelete(new File(imgPath));
						} catch (IOException e) {
							if(e instanceof FileNotFoundException) {
								continue ;
							} else {
								throw e;
							}
						}
					}
				}
			}
		}
		
	}
	
	@RedisCache(type = Picture.class)
	public Picture getInfo(Picture picture) {
		Picture pictureModel = pictureMapper.selectByPrimaryKey(picture.getId());
		if(pictureModel != null) {
			setMainPicture(pictureModel);
		}

		return pictureModel;
	}
	
	@RedisEvict(type = Picture.class)
	public void insertInfo(Picture picture) {
		pictureMapper.insertSelective(picture);
	}
	
	@RedisEvict(type = Picture.class)
	public void updateInfo(Picture picture) {
		pictureMapper.updateByPrimaryKeySelective(picture);
	}
	
	public void setMainPicturePath(List<Picture> picList) {
		if(picList != null) {
			for(Picture item: picList) {
				setMainPicture(item);
			}
		}
	}
	
	public void setMainPicture(Picture picture) {
		setHost(picture);
		String subPath = picture.getPicSubMinPath();
		if(subPath != null) {
			String[] paths = subPath.split(",");
			if(paths != null && paths.length > 0) {
				picture.setMainPicture(paths[0]);
				if(paths.length > 1) {
					picture.setMiddlePicture(paths[1]);
					if(paths.length > 2) {
						picture.setLargePicture(paths[2]);
					}
				}
			}
		}
	}
	
	private void setHost(Picture picture) {
		picture.setHost(PropertiesUtils.getValue(ConstantUtil.PropertiesKeys.IMAGE_HOST));
		picture.setProtocol(PropertiesUtils.getValue(ConstantUtil.PropertiesKeys.IMAGE_PROTOCOL));
	}
}
