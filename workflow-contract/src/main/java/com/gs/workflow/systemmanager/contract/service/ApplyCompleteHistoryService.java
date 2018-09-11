package com.gs.workflow.systemmanager.contract.service;

import com.gs.workflow.systemmanager.contract.model.crms.ApplyCompleteHistory;

/**
 * Created by zhengyali on 2018/1/22.
 */
public interface ApplyCompleteHistoryService {

    /**
     * 保存完成历史
     * @param applyId
     * @param completeUserId
     * @param completeType
     * @return
     */
    ApplyCompleteHistory findAllByApplyIdAndCompleteUserId(String applyId,String completeUserId, boolean  completeType);

    /**
     * 保存完成历史
     * @param applyCompleteHistory
     * @return
     */
    boolean saveApplyCompleteHistory(ApplyCompleteHistory applyCompleteHistory);

    /**
     * 更新流程
     */
    boolean updateApplyCompleteHistory(String  applyId);
    /**
     * 获取当前的任务完成状态
     */
    ApplyCompleteHistory findOneByApplyIdAndIsCurrentProcess(String applyId);
}
