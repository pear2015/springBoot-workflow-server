package com.gs.workflow.systemmanager.contract.model.common;

/**
 * Created by zhangqiang on 2017/9/26.
 */
public class User {
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 用户状态
     */
    private String status;
    /**
     * 角色ID
     */
    private String roleId;
    /***
     * 角色类型
     * */
    private  String roleType;


    /**
     * Get
     * @return
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Set
     * @param userId
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }
    /**
     * Get
     * @return
     */
    public String getStatus() {
        return status;
    }

    /**
     * Set
     * @param status
     */
    public void setStatus(String status) {
        this.status = status;
    }
    /**
     * Get
     * @return
     */
    public String getRoleId() {
        return roleId;
    }

    /**
     * Set
     * @param roleId
     */
    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }
}