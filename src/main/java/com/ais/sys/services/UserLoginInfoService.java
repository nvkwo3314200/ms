package com.ais.sys.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ais.sys.daos.UserLoginInfoMapper;
import com.ais.sys.models.UserInfoModel;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
@Service("userLoginInfoService")
public class UserLoginInfoService extends BaseService{
	private static final Logger LOG = LoggerFactory
			.getLogger(MenuService.class);
	private UserLoginInfoMapper mapper;
	public UserLoginInfoMapper getMapper() {
		return mapper;
	}
	@Autowired
	public void setMapper(UserLoginInfoMapper mapper) {
		this.mapper = mapper;
	}
	
	public PageInfo<UserInfoModel> search(UserInfoModel model){
		return PageHelper.startPage(model.getPage(), model.getSize()).doSelectPageInfo(() -> mapper.searchUserLoginInfo(model));
	}
}
