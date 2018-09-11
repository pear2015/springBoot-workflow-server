package com.gs.workflow.systemmanager.contract.model.crms;

/**
 * Created by zhangqiang on 2017/9/28.
 * 分析员和流程实例关联
 */
public class AnalystAndProcess {
    /**
     * 流程实例ID
     */
    private String processId;

    /**
     * 分析员ID
     */
    private String analystId;

    /**
     * 业务申请Id
     */
    private String applyId;

    /**
     * 优先级
     */
    private String priority;
    /**
     * 业务类型
     * 0 个人申请
     * 1 政府申请
     * 2 公告
     */
    private  String  businessType;

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

    /**
     * Get
     *
     * @return
     */
    public String getAnalystId() {
        return analystId;
    }

    /**
     * Set
     *
     * @param analystId
     */
    public void setAnalystId(String analystId) {
        this.analystId = analystId;
    }

    /**
     * Get
     *
     * @return
     */
    public String getApplyId() {
        return applyId;
    }

    /**
     * Set
     */
    public void setApplyId(String applyId) {
        this.applyId = applyId;
    }

    /**
     * Get
     *
     * @return
     */
    public String getPriority() {
        return priority;
    }

    /**
     * Set
     */
    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }
}
