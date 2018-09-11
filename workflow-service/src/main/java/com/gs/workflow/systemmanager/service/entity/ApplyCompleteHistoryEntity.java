package com.gs.workflow.systemmanager.service.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by zhengyali on 2017/11/17.
 * 任务完成历史
 */
@Entity
@Table(name = "b_apply_complete_history")
public class ApplyCompleteHistoryEntity {
    /*
主键ID
*/
    @Id
    @Column(name = "id", nullable = false, length = 36)
    private String id;
    /**
     * 申请Id
     */
    @Column(name = "apply_info_id")
    private String applyInfoId;

    /**
     * 工作流流程ID
     */
    @Column(name = "process_id")
    private String processId;

    /**
     * 創建時間
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 完成人
     */
    @Column(name = "complete_user_id")
    private  String completeUserId;
    /**
     * 当前流程
     */
    @Column(name = "is_current_process")
    private  String isCurrentProcess;
    /**
     * 是否首次完成 true 重新
     */
    @Column(name = "is_reply_complete")
    private boolean  isReplyComplete;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApplyInfoId() {
        return applyInfoId;
    }

    public void setApplyInfoId(String applyInfoId) {
        this.applyInfoId = applyInfoId;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCompleteUserId() {
        return completeUserId;
    }

    public void setCompleteUserId(String completeUserId) {
        this.completeUserId = completeUserId;
    }

    public String getIsCurrentProcess() {
        return isCurrentProcess;
    }

    public void setIsCurrentProcess(String isCurrentProcess) {
        this.isCurrentProcess = isCurrentProcess;
    }

    public boolean isReplyComplete() {
        return isReplyComplete;
    }

    public void setReplyComplete(boolean replyComplete) {
        isReplyComplete = replyComplete;
    }
}
