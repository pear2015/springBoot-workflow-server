package com.gs.workflow.systemmanager.contract.model.crms;

import java.util.Date;

/**
 * Created by zhangqiang on 2017/9/26.
 * 等待激活表
 */
public class WaitApportion {
    /**
     * id
     */
    private String apportionId;
    /**
     * 申请ID
     */
    private String applyInfoId;
    /**
     * 优先级
     */
    private String applyPriority;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 等待激活的原因类型1.分析员2.审核员
     */
    private String waitReasonType;
    /**
     * 工作流流程ID
     */
    private String processId;
    /**
     * 服务类型
     */
    private  String businessType;

    /**
     * Get
     *
     * @return
     */
    public String getApplyInfoId() {
        return applyInfoId;
    }

    /**
     * Set
     *
     * @param applyInfoId
     */
    public void setApplyInfoId(String applyInfoId) {
        this.applyInfoId = applyInfoId;
    }

    public String getApplyPriority() {
        return applyPriority;
    }

    public void setApplyPriority(String applyPriority) {
        this.applyPriority = applyPriority;
    }

    /**
     * Get
     *
     * @return
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * Set
     *
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getWaitReasonType() {
        return waitReasonType;
    }

    public void setWaitReasonType(String waitReasonType) {
        this.waitReasonType = waitReasonType;
    }

    /**
     * Get
     *
     * @return
     */
    public String getProcessId() {
        return processId;
    }

    /**
     * Set
     *
     * @param processId
     */
    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getApportionId() {
        return apportionId;
    }

    public void setApportionId(String apportionId) {
        this.apportionId = apportionId;
    }
}
