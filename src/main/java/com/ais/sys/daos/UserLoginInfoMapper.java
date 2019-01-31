package com.ais.sys.daos;

import java.util.List;

import com.ais.sys.models.UserInfoModel;

public interface UserLoginInfoMapper {

	List<UserInfoModel> searchUserLoginInfo(UserInfoModel model);
}
