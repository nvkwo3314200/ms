package com.ais.sys.services;

import com.ais.sys.daos.OneNoteTaskMapper;
import com.ais.sys.models.OneNoteTask;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class OneNoteTaskService {

    @Resource
    private OneNoteTaskMapper oneNoteTaskMapper;

    public void insert(OneNoteTask oneNoteTask) {
        oneNoteTaskMapper.insert(oneNoteTask);
    }

    public OneNoteTask findOne(OneNoteTask oneNoteTask) {
        List<OneNoteTask> taskList = oneNoteTaskMapper.selectByModel(oneNoteTask);
        if(CollectionUtils.isNotEmpty(taskList)) {
            return taskList.get(0);
        }
        return null;
    }

    public List<OneNoteTask> findList(OneNoteTask oneNoteTask) {
        return oneNoteTaskMapper.selectByModel(oneNoteTask);
    }

    public int update(OneNoteTask oneNoteTask) {
        return oneNoteTaskMapper.updateByPrimaryKey(oneNoteTask);
    }

    public int delete(OneNoteTask oneNoteTask) {
        return oneNoteTaskMapper.deleteByPrimaryKey(oneNoteTask.getId());
    }

}
