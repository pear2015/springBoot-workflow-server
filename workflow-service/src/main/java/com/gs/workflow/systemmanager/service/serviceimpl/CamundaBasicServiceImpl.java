package com.gs.workflow.systemmanager.service.serviceimpl;

import com.gs.workflow.common.util.VariableUtil;
import com.gs.workflow.systemmanager.contract.model.crms.ApplyCompleteHistory;
import com.gs.workflow.systemmanager.contract.service.ApplyCompleteHistoryService;
import com.gs.workflow.systemmanager.contract.service.CamundaBasicService;
import org.apache.commons.lang.StringUtils;

import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangqiang on 2017/9/27.
 * 工作流操作实现
 */
@Service
@Transactional
public class CamundaBasicServiceImpl implements CamundaBasicService {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private ApplyCompleteHistoryService applyCompleteHistoryService;


    /**
     * 部署流程定义
     *
     * @param file
     */
    @Override
    public String deployProcess(MultipartFile file) {
        return "";
    }

    /**
     * 启动流程
     * <p>
     * }
     *
     * @param processKey
     * @param bussinessKey
     * @param workFlowVariables
     * @return
     */
    @Override
    public String startProcessByProcessKey(String processKey, String bussinessKey, Map<String, Object> workFlowVariables) {
        try {
            if (workFlowVariables.isEmpty()) {
                return runtimeService.startProcessInstanceByKey(processKey, bussinessKey).getProcessInstanceId();
            } else {
                return  runtimeService.startProcessInstanceByKey(processKey, bussinessKey, workFlowVariables).getProcessInstanceId();
            }
        } catch (Exception e) {
            logger.info("processKey=" + processKey + "start is failure",e);
            return null;
        }

    }

    /**
     * 完成任务
     *
     * @param taskId            任务ID
     * @param workFlowVariables 流程变量
     * @return
     */
    @Override
    public boolean completeTask(String taskId, Map<String, Object> workFlowVariables) {
        try {
            if (workFlowVariables.isEmpty()) {
                this.taskService.complete(taskId);
            } else {
                this.taskService.complete(taskId, workFlowVariables);
            }
            return true;
        } catch (Exception e) {
            logger.info("taskId=" + taskId + "complete is failure",e);
            return false;
        }
    }

    /**
     * 完成任务
     * 此处做修改
     * 1 singleTask 不为空 任务完成成功 保存完成历史
     * 2 singleTask 为空  查询任务完成历史 判断任务是否完成 (解决犯罪服务调用工作流成功 自身包失败 导致犯罪服务调用工作流，任务完成一直失败)
     * @param assignee          任务参与者
     * @param processInstanceId 流程实例ID
     * @param variables         流程变量
     * @return
     */
    @Override
    public boolean completeTaskAndSetVariables(String businessKey,String assignee, String processInstanceId, Map<String, Object> variables) {
        try {
            Task singleTask = this.taskService.createTaskQuery().taskAssignee(assignee).processInstanceId(processInstanceId).singleResult();
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
            if (singleTask != null&&processInstance!=null) {
                String taskId = singleTask.getId();
                this.taskService.complete(taskId, variables);
                return saveApplyCompleteHistory(processInstance.getBusinessKey(),assignee, processInstanceId,variables.containsKey(VariableUtil.ISREPLY)?Boolean.parseBoolean(variables.get(VariableUtil.ISREPLY).toString()):false);
            }else {
                return isHasComplete(businessKey, assignee, variables.containsKey(VariableUtil.ISREPLY)?Boolean.parseBoolean(variables.get(VariableUtil.ISREPLY).toString()):false);
            }
        } catch (Exception e) {
            logger.info("processInstanceId=" + processInstanceId + "complete is failure",e);
            return false;
        }
    }

    /**
     * 保存任务完成历史
     * @param businessKey
     * @param assignee
     * @return
     */
    private  boolean saveApplyCompleteHistory(String businessKey,String assignee,String processId, boolean completeType){
        ApplyCompleteHistory applyCompleteHistory=new ApplyCompleteHistory();
        applyCompleteHistory.setApplyInfoId(businessKey);
        applyCompleteHistory.setCompleteUserId(assignee);
        applyCompleteHistory.setCreateTime(new Date());
        applyCompleteHistory.setIsCurrentProcess("1");
        applyCompleteHistory.setProcessId(processId);
        applyCompleteHistory.setReplyComplete(completeType);
        return applyCompleteHistoryService.saveApplyCompleteHistory(applyCompleteHistory);
    }

    /**
     *  判断当前任务是否已经完成
     *  获取当前的任务
     */
    private  boolean isHasComplete(String  businessKey,String completeUserId, boolean  replyComplete){
        ApplyCompleteHistory  applyCompleteHistory=applyCompleteHistoryService.findAllByApplyIdAndCompleteUserId(businessKey,completeUserId,replyComplete);
        return  applyCompleteHistory!=null;
    }

    /**
     * 激活流程
     *
     * @param processInstanceId 流程实例ID
     * @param workFlowVariables 流程变量
     */
    @Override
    public boolean activeProcessByProcessId(String processInstanceId, Map<String,Object> workFlowVariables) {
        try {
            if (!workFlowVariables.isEmpty()) {
                runtimeService.setVariables(processInstanceId, workFlowVariables);
            }
            runtimeService.activateProcessInstanceById(processInstanceId);
            return true;
        } catch (Exception e) {
            logger.info("processInstanceId=" + processInstanceId + "active  is failure",e);
            return false;
        }
    }

    /**
     * 获取任务
     *
     * @param assignee 任务参与者
     * @return
     */
    @Override
    public List<String> getTaskByAssignee(String assignee) {
        List<String> taskIds = new ArrayList<>();
        try {
            List<Task> tasks = this.taskService.createTaskQuery().taskAssignee(assignee).list();
            if (!tasks.isEmpty()) {
                for (Task task : tasks) {
                    taskIds.add(task.getId());
                }
            }
            return taskIds;
        } catch (Exception e) {
            logger.info("get task list  is failure",e);
            return taskIds;
        }
    }

    /**
     * 获取任务
     *
     * @param assignee          任务参与者
     * @param processInstanceId 流程实例ID
     * @return
     */
    @Override
    public String getTaskByAssigneeAndProcessId(String assignee, String processInstanceId) {
        String taskId = "";
        try {
            Task singleTask = this.taskService.createTaskQuery().taskAssignee(assignee).processInstanceId(processInstanceId).singleResult();
            if (singleTask != null) {
                taskId = singleTask.getId();
            }
            return taskId;
        } catch (Exception e) {
            logger.info("get task  is failure",e);
            return taskId;
        }
    }

    /**
     * 删除流程定义
     *
     * @param processDefinitionId
     * @return
     */
    @Override
    public boolean deleteProcessDefinitionById(String processDefinitionId, boolean cascade,
                                               boolean skipCustomListeners) {
        try {
            repositoryService.deleteProcessDefinition(processDefinitionId, cascade, skipCustomListeners);
            return true;
        } catch (Exception e) {
            logger.info("delete processDefinition  is failure",e);
            return false;
        }

    }

    /**
     * 删除部署
     *
     * @param deployId
     * @return
     */
    @Override
    public boolean deleteDeployById(String deployId, boolean cascade, boolean skipCustomListener) {
        try {
            repositoryService.deleteDeployment(deployId, cascade, skipCustomListener);
            return true;
        } catch (Exception e) {
            logger.info("delete deploy  is failure",e);
            return false;
        }

    }

    /**
     * 删除正在运行的流程实例
     *
     * @param processInstanceId   流程实例
     * @param deleteReason        删除原因
     * @param skipCustomListeners 是否跳过用户监听
     * @return
     */
    @Override
    public boolean deleteRunProcessInstanceById(String processInstanceId, String deleteReason, boolean skipCustomListeners) {
        try {
            if (!StringUtils.isBlank(processInstanceId)) {
                runtimeService.deleteProcessInstance(processInstanceId, deleteReason, skipCustomListeners);
            }
            return true;
        } catch (Exception e) {
            logger.info("delete run processInstance  is failure",e);
            return false;
        }
    }
}
