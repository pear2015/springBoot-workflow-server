package com.gs.workflow.systemmanager.service.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by zhengyali on 2017/10/19.
 */
@Entity
@Table(name = "b_wait_apportion")
public class WaitApportionEntity {
    /*
  主键ID
  */
    @Id
    @Column(name = "id", nullable = false, length = 36)
    private String apportionId;

    @Column(name = "apply_info_id")
    private String applyInfoId;

    /**
     * 創建時間
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 权限
     */
    @Column(name = "priority")
    private String applyPriority;
    /**
     * 等待激活的原因类型1.分析员2.审核员
     */
    @Column(name = "wait_reason_type")
    private String waitReasonType;
    /**
     * 工作流流程ID
     */
    @Column(name = "process_id")
    private String processId;
    /**
     * 服务类型 1个人申请 2政府申请 3公告申请
     */
    @Column(name = "business_type")
    private  String businessType;

    public String getApportionId() {
        return apportionId;
    }

    public void setApportionId(String apportionId) {
        this.apportionId = apportionId;
    }

    public String getApplyInfoId() {
        return applyInfoId;
    }

    public void setApplyInfoId(String applyInfoId) {
        this.applyInfoId = applyInfoId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getApplyPriority() {
        return applyPriority;
    }

    public void setApplyPriority(String applyPriority) {
        this.applyPriority = applyPriority;
    }

    public String getWaitReasonType() {
        return waitReasonType;
    }

    public void setWaitReasonType(String waitReasonType) {
        this.waitReasonType = waitReasonType;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }
}
