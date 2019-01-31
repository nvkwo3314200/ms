package com.ais.sys.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	
	public PageInfo<Picture> search(final Picture picture) {
		PageInfo<Picture> pageInfo = PageHelper.startPage(picture.getPage(), picture.getSize())
				.doSelectPageInfo(() -> pictureMapper.selectByModel(picture));
		setMainPicturePath(pageInfo.getList());
		return pageInfo;
	}
	
	public List<Picture> list(Picture picture) {
		List<Picture> picList = pictureMapper.selectByModel(picture);
		setMainPicturePath(picList);
		return picList;
	}
	
	public void deleteInfo(Picture picture) throws ServiceException {
		if(picture.getId() != null) {
			pictureMapper.deleteByPrimaryKey(picture.getId());
		} else {
			throw new ServiceException("id 不能为空");
		}
	}
	
	public Picture getInfo(Picture picture) {
		Picture pictureModel = pictureMapper.selectByPrimaryKey(picture.getId());
		if(pictureModel != null) {
			setHost(pictureModel);
		}

		return pictureModel;
	}
	
	public void insertInfo(Picture picture) {
		pictureMapper.insertSelective(picture);
	}
	
	public void updateInfo(Picture picture) {
		pictureMapper.updateByPrimaryKeySelective(picture);
	}
	
	public void setMainPicturePath(List<Picture> picList) {
		if(picList != null) {
			for(Picture item: picList) {
				setHost(item);
				String subPath = item.getPicSubMinPath();
				if(subPath != null) {
					String[] paths = subPath.split(",");
					if(paths != null && paths.length > 0) {
						item.setMainPicture(paths[0]);
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
