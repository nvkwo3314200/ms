package com.ais.sys.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ais.sys.exception.ServiceException;
import com.ais.sys.models.Picture;
import com.ais.sys.models.ResponseData;
import com.ais.sys.services.PictureService;

@RestController
@RequestMapping(value="picture")
public class PictureController extends BaseController {
	
	@Autowired
	PictureService pictureService;
	
	@RequestMapping(value="search")
	public ResponseData<?> search(@RequestBody Picture picture) {
		return ResponseData.success(pictureService.search(picture), null);
	}
	
	@RequestMapping(value="delete")
	public ResponseData<?> delete(@RequestBody Picture picture) {
		try {
			pictureService.deleteInfo(picture);
			return ResponseData.success(null,  null);
		} catch (ServiceException e) {
			return ResponseData.danger().add(e.getMessage());
		}
	}
	
	@RequestMapping(value="get")
	public ResponseData<?> get(@RequestBody Picture picture) {
		try {
			Picture result = pictureService.getInfo(picture);
			if(result == null) {
				throw new ServiceException("没有对应记录");
			}
			return ResponseData.success(null,  result);
		} catch (ServiceException e) {
			return ResponseData.danger().add(e.getMessage());
		}
	}
	
	@RequestMapping(value="save")
	public ResponseData<?> save(@RequestBody Picture picture) {
		if(picture.getId() == null) {
			pictureService.insertInfo(picture);
		} else {
			pictureService.updateInfo(picture);
		}
		return ResponseData.success(null, picture);
	}
}
