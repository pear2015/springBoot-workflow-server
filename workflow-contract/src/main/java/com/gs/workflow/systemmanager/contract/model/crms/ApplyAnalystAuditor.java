package com.gs.workflow.systemmanager.contract.model.crms;

import java.util.Date;

/**
 * Created by zhangqiang on 2017/9/26.
 * 任务和分析员审核员关联表DTO
 */
public class ApplyAnalystAuditor {

    /**
     * 申请ID
     */
    private String applyInfoId;
    /**
     * 分析员ID
     */
    private String analystId;
    /**
     * 审核员ID
     */
    private String auditorId;
    /**
     * 分析结果是否不通过
     */
    private int analysisResultFail;
    /**
     * 优先级
     */
    private String priority;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 业务类型
     * 1 申请业务
     * 2 公告业务
     */
    private  String  businessType;

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
    public String getAuditorId() {
        return auditorId;
    }

    /**
     * Set
     *
     * @param auditorId
     */
    public void setAuditorId(String auditorId) {
        this.auditorId = auditorId;
    }

    /**
     * Get
     *
     * @return
     */
    public int getAnalysisResultFail() {
        return analysisResultFail;
    }

    /**
     * Set
     *
     * @param analysisResultFail
     */
    public void setAnalysisResultFail(int analysisResultFail) {
        this.analysisResultFail = analysisResultFail;
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

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}
