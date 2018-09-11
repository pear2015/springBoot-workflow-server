package com.gs.workflow.systemmanager.service.repository;



import com.gs.workflow.systemmanager.service.entity.WaitApportionEntity;
import com.gsafety.springboot.common.pagerepository.QueryMetaDataRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by tanjie on 2017/8/8.
 */
@Repository
public interface WaitApportionRepository extends CrudRepository<WaitApportionEntity, Long>,QueryMetaDataRepository<WaitApportionEntity,Long> {

}
