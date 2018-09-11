package com.gs.workflow.systemmanager.service.repository;

import com.gs.workflow.systemmanager.service.entity.ApplyCompleteHistoryEntity;
import com.gsafety.springboot.common.pagerepository.QueryMetaDataRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by zhengyali on 2018/1/22.
 */
@Repository
public interface ApplyCompleteEntityRepository  extends CrudRepository<ApplyCompleteHistoryEntity, Long>,QueryMetaDataRepository<ApplyCompleteHistoryEntity,Long> {
    /**
     * 获取该业务的完成历史
     * @param applyId
     * @param isReplyComplete
     * @return
     */
    @Query(value = "select A  from  ApplyCompleteHistoryEntity A where A.applyInfoId=?1 AND A.completeUserId=?2 and A.isReplyComplete=?3 order by  A.createTime desc ")
    ApplyCompleteHistoryEntity  findOneByApplyIdAndCompleteUserId(String applyId,String  completeUserId,boolean isReplyComplete);

    /**
     * 更新状态
     * @param applyInfoId
     */
    @Modifying
    @Query(value = "update  ApplyCompleteHistoryEntity  A set A.isCurrentProcess='0' where A.applyInfoId=?1")
    void updateApplyCompleteHistory(String applyInfoId);

    /**
     * 获取当前最新的完成历史
     * @param applyInfoId
     * @return
     */
    @Query(value = "select A  from  ApplyCompleteHistoryEntity A where A.applyInfoId=?1 AND A.isCurrentProcess='1'")
    ApplyCompleteHistoryEntity    findOneByApplyIdAndIsCurrentProcess(String applyInfoId );
}
