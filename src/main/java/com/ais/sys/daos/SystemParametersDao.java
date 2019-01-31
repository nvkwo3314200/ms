package com.ais.sys.daos;

import java.util.List;

import com.ais.sys.models.SystemParametersModel;

public interface SystemParametersDao {
	List<SystemParametersModel> selectSystemParameters(SystemParametersModel model);
	int deleteSystemParameters(SystemParametersModel model);
	int insertSystemParameters(SystemParametersModel model);
	int updateSystemParameters(SystemParametersModel model);
	//cgp
	int deleteAll(SystemParametersModel model);
}
