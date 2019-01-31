package com.ais.sys.daos;

import com.ais.sys.models.RoleModel;

public interface UserRoleManagerMapper {
	int userOfRoleId();
    int insert(RoleModel roleModel);
    int update(RoleModel roleModel);
    int delete(RoleModel roleModel);
}