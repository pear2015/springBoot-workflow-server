package com.gs.workflow.systemmanager.service.serviceimpl.crime;

import com.gs.workflow.systemmanager.contract.model.crms.WaitApportion;
import com.gs.workflow.systemmanager.service.datamappers.WaitApportionMapper;
import com.gs.workflow.systemmanager.service.entity.WaitApportionEntity;
import com.gs.workflow.systemmanager.service.repository.WaitApportionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by zhengyali on 2017/10/19.
 */
@Transactional
@Service
public class WaitApportionServiceImpl {
    @Autowired
    private WaitApportionRepository waitApportionRepository;
    @Autowired
    private WaitApportionMapper waitApportionMapper;

    /**
     * 工作流等待分派保存
     *
     * @param waitApportion
     * @return
     */
    public boolean saveWaitApportion(WaitApportion waitApportion) {
        WaitApportionEntity waitApportionEntity = waitApportionMapper.modelToEntity(waitApportion);
        waitApportionEntity.setApportionId(UUID.randomUUID().toString());
        waitApportionEntity.setCreateTime(new Date());
        waitApportionRepository.save(waitApportionEntity);
        return true;
    }

    /**
     * 获取等待分派列表
     * @return
     */
    public List<WaitApportion> findAll() {
        List<WaitApportionEntity> waitApportionEntityList=waitApportionRepository.findAll();
        return  waitApportionMapper.entitiestoModels(waitApportionEntityList);
    }
}
