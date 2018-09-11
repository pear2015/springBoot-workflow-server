package com.gs.workflow.systemmanager.contract.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * Created by zhangqiang on 2017/9/27.
 * 工作流操作service
 */
public interface CamundaBasicService {
    /**
     * 部署流程
     */
    String deployProcess(MultipartFile file);

    /**
     * 启动流程
     *
     * @param processKey
     * @param bussinessKey
     * @param workFlowVariables
     * @return
     */
    String startProcessByProcessKey(String processKey, String bussinessKey, Map<String, Object> workFlowVariables);

    /**
     * 完成任务
     *
     * @param taskId            任务ID
     * @param workFlowVariables 流程变量
     * @return
     */
    boolean completeTask(String taskId, Map<String, Object> workFlowVariables);

    /**
     * 完成任务
     *
     * @param assignee          任务参与者
     * @param processInstanceId 流程实例ID
     * @param variables         流程变量
     * @return
     */
    boolean completeTaskAndSetVariables(String businessKey,String assignee, String processInstanceId, Map<String, Object> variables);

    /**
     * 激活流程
     *
     * @param processInstanceId 流程实例ID
     * @param workFlowVariables 流程变量
     */
    boolean activeProcessByProcessId(String processInstanceId, Map<String, Object> workFlowVariables);

    /**
     * 获取任务列表
     *
     * @param assignee 任务参与者
     * @return
     */
    List<String> getTaskByAssignee(String assignee);

    /**
     * 获取任务
     *
     * @param assignee          任务参与者
     * @param processInstanceId 流程实例ID
     * @return
     */
    String getTaskByAssigneeAndProcessId(String assignee, String processInstanceId);

    /**
     * 删除流程定义
     *
     * @param processDefinitionId
     * @return
     */
    boolean deleteProcessDefinitionById(String processDefinitionId, boolean cascade, boolean skipCustomListeners);

    /**
     * 删除部署
     *
     * @param deployId
     * @return
     */
    boolean deleteDeployById(String deployId, boolean cascade, boolean skipCustomListeners);

    /**
     * 删除正在运行的流程实例
     *
     * @param processInstanceId
     * @return
     */
    boolean deleteRunProcessInstanceById(String processInstanceId, String deleteReason, boolean skipCustomListeners);

}
