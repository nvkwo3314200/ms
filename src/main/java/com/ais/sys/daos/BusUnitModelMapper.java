package com.ais.sys.daos;

import java.util.List;
import java.util.Map;

import com.ais.sys.models.BusUnitModel;

public interface BusUnitModelMapper {
	List<BusUnitModel> search(BusUnitModel model);
	List<BusUnitModel> select(BusUnitModel model);
	int insert(BusUnitModel model);
	int update(BusUnitModel model);
	int delete(BusUnitModel model);
	String insertRun(BusUnitModel model);
}