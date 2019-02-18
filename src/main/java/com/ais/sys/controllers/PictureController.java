package com.ais.sys.controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ais.sys.exception.ServiceException;
import com.ais.sys.models.Picture;
import com.ais.sys.models.ResponseData;
import com.ais.sys.services.PictureService;
import com.ais.sys.utils.ImageUtils;

@RestController
@RequestMapping(value="picture")
public class PictureController extends BaseController {
	
	@Autowired
	PictureService pictureService;
	
	@Autowired 
	ImageUtils imageUtils;
	
	@RequestMapping(value="search")
	public ResponseData<?> search(@RequestBody Picture picture) {
		return ResponseData.success(pictureService.search(picture), null);
	}
	
	@RequestMapping(value="delete")
	public ResponseData<?> delete(@RequestBody final Picture[] pictures) {
		try {
			pictureService.deleteInfo(pictures);
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
	
	@RequestMapping(value="upload",method = RequestMethod.POST)
	public String uploadPic(HttpServletRequest request, String fileName,  @RequestParam("file") MultipartFile file, Model model) {
		// 如果文件不为空，写入上传路径
		try {
			if (!file.isEmpty()) {
				// 上传文件路径
				String realPath = request.getSession().getServletContext().getRealPath("/")+ "/images/";
				// 上传文件名
				String filename = file.getOriginalFilename();
				if(!StringUtils.isEmpty(fileName)) filename = fileName + filename.substring(filename.lastIndexOf("."));
				File filepath = new File(realPath, filename);
				
				// 判断路径是否存在，如果不存在就创建一个
				if (!filepath.getParentFile().exists()) {
					filepath.getParentFile().mkdirs();
				}
				// 将上传文件保存到一个目标文件当中
				String des = realPath + File.separator + filename;
				file.transferTo(new File(des));
				Picture picture = imageUtils.resizeImage(des, filename, null);
				pictureService.insertInfo(picture);
				FileUtils.forceDeleteOnExit(new File(des));
				model.addAttribute("MESSAGE", "上传文件成功");
				List<String> list = new ArrayList<String>();
				for(String item : picture.getPicSubMinPath().split(",")) {
					list.add(picture.getHost() + "/" + item);
				}
				model.addAttribute("filePaths", list);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			model.addAttribute("MESSAGE", "上传文件失败");
		}
		return "picture";
	}
}
