package com.gs.workflow.systemmanager.service.serviceimpl;
import com.gs.workflow.systemmanager.contract.model.crms.ApplyCompleteHistory;
import com.gs.workflow.systemmanager.contract.service.ApplyCompleteHistoryService;
import com.gs.workflow.systemmanager.service.datamappers.ApplyCompleteHistoryMapper;
import com.gs.workflow.systemmanager.service.entity.ApplyCompleteHistoryEntity;
import com.gs.workflow.systemmanager.service.repository.ApplyCompleteEntityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.UUID;

/**
 * Created by zhengyali on 2018/1/22.
 */
@Service
@Transactional
public class ApplyCompleteHistoryServiceImpl implements ApplyCompleteHistoryService {
    @Autowired
    private ApplyCompleteEntityRepository applyCompleteEntityRepository;
    @Autowired
    private ApplyCompleteHistoryMapper applyCompleteHistoryMapper;
    Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 根据申请Id,完成人,完成类型获取完成历史
     *
     * @param applyId
     * @param completeUserId
     * @param completeType
     * @return
     */
    @Override
    public ApplyCompleteHistory findAllByApplyIdAndCompleteUserId(String applyId, String completeUserId, boolean completeType) {
        ApplyCompleteHistoryEntity applyCompleteHistoryModel = applyCompleteEntityRepository.findOneByApplyIdAndCompleteUserId(applyId, completeUserId, completeType);
        return applyCompleteHistoryMapper.entityToModel(applyCompleteHistoryModel);
    }

    /**
     * 保存完成历史
     *
     * @param applyCompleteHistory
     * @return
     */
    @Override
    public boolean saveApplyCompleteHistory(ApplyCompleteHistory applyCompleteHistory) {
        try {
            ApplyCompleteHistoryEntity applyCompleteHistoryModel = applyCompleteEntityRepository.findOneByApplyIdAndCompleteUserId(applyCompleteHistory.getApplyInfoId(), applyCompleteHistory.getCompleteUserId(), applyCompleteHistory.isReplyComplete());
            applyCompleteHistory.setId(applyCompleteHistoryModel == null ? UUID.randomUUID().toString() : applyCompleteHistoryModel.getId());
            ApplyCompleteHistoryEntity entity = applyCompleteHistoryMapper.modelToEntity(applyCompleteHistory);
            if(updateApplyCompleteHistory(applyCompleteHistory.getApplyInfoId())){
                applyCompleteEntityRepository.save(entity);
                return true;
            }
           return  false;
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            logger.info("Exception: save apply complete history Cell Exception", e);
            return false;
        }
    }

    /**
     * 更新之前的完成历史
     * @param applyInfoId
     * @return
     */
    @Override
    public boolean updateApplyCompleteHistory(String  applyInfoId) {
        try {
            applyCompleteEntityRepository.updateApplyCompleteHistory(applyInfoId);
            return true;
        }catch (Exception e ){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            logger.info("Exception: save apply complete history Cell Exception", e);
            return false;
        }

    }

    /**
     * 获取当前最新的完成历史
     * @param applyId
     * @return
     */
    @Override
    public ApplyCompleteHistory findOneByApplyIdAndIsCurrentProcess(String applyId) {
        return applyCompleteHistoryMapper.entityToModel(applyCompleteEntityRepository.findOneByApplyIdAndIsCurrentProcess(applyId));
    }
}
