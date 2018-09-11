package com.gs.workflow.systemmanager.service.serviceimpl.crime;

import com.gs.workflow.common.enums.MessageType;
import com.gs.workflow.common.enums.RoleType;
import com.gs.workflow.common.util.VariableUtil;
import com.gs.workflow.systemmanager.contract.model.common.NoticeMessage;
import com.gs.workflow.systemmanager.contract.model.common.User;
import com.gs.workflow.systemmanager.service.util.BubberSortLessTaskAssignee;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhengyali on 2017/10/31.
 */
@Service
public class NoticeServiceImpl {
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private SocketServiceImpl socketServiceImpl;
    @Autowired
    private TaskService taskService;
    @Autowired
    private  WorkFlowServiceImpl workFlowService;
    @Value("${auditor.event}")
    private String auditorEvent;
    @Value("${message.audit.notice.content}")
    private String auditorNoticeContent;
    private BubberSortLessTaskAssignee bubberSortLessTaskAssignee = new BubberSortLessTaskAssignee();
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * 1、检查是否有在线审核员
     *
     * @param execution
     * @return
     */
    public boolean noticeApportionResolve(DelegateExecution execution) {
        //1.根据角色类型获取在线审核员
        try {
            String priority = (String) execution.getVariable(VariableUtil.PRIORITY);
            List<User> userList = socketServiceImpl.getUserListByRoleType(RoleType.AUDITOR);
            //在线人员
            if (CollectionUtils.isEmpty(userList)) {
                //没有人直接挂起
                execution.setVariable(VariableUtil.AUDITOR, "");
                workFlowService.saveWaitApportion(VariableUtil.AUDITOR, execution.getBusinessKey(), execution.getProcessInstanceId(),"3",priority);
                runtimeService.suspendProcessInstanceById(execution.getProcessInstanceId());
                logger.info("NoticeServiceImpl:Workflow has suspend because of no auditor on line" + execution.getProcessInstanceId());
            } else {
                // 有人 任务分派
                List<String> userOnline = new ArrayList<>();
                userList.forEach(user -> userOnline.add(user.getUserId()));
                taskAssignee(VariableUtil.AUDITOR, bubberSortLessTaskAssignee.getLessTaskAssigneeId(userOnline,execution.getProcessDefinitionId()), execution,priority);
            }
        } catch (Exception e) {
            logger.error("noticeApportionResolve has Exception!",e);
            return false;
        }
        return true;
    }
    /**
     * 任务分派
     *
     * @param roleType
     * @param assignee
     * @param execution
     * @return
     */
    public boolean taskAssignee(String roleType, String assignee, DelegateExecution execution,String priority) {
        //没有分派到 流程挂起
        try {
                // 分派到 处理流程
                execution.setVariable(roleType, assignee);
                // 保存依赖关系成功 任务分派 发送消息
                if (workFlowService.saveApplyAnalystAuditor(roleType, assignee, execution.getBusinessKey(),"3",priority)) {
                    Task singleTask = this.taskService.createTaskQuery().processInstanceId(execution.getProcessInstanceId()).singleResult();
                    if (singleTask != null) {
                        this.taskService.setAssignee(singleTask.getId(), assignee);
                    }
                    sendNoticeMessage(assignee);
                } else {
                    //保存依赖关系 失败  流程挂起保存到等待分派列表
                    execution.setVariable(roleType, "");
                    runtimeService.suspendProcessInstanceById(execution.getProcessInstanceId());
                    //调用工作流保存等待分派失败保存到本地数据库
                    workFlowService.saveWaitApportion(roleType, execution.getBusinessKey(), execution.getProcessInstanceId(),"3",priority);
                    logger.info("NoticeServiceImpl:Workflow has suspend because of save relation between task and assignee failed" + execution.getProcessInstanceId());
                }
            return true;
        } catch (Exception e) {
            logger.error("taskAssignee has Exception!",e);
            execution.setVariable(roleType, "");
            workFlowService.saveWaitApportion(roleType, execution.getBusinessKey(), execution.getProcessInstanceId(),"3",priority);
            runtimeService.suspendProcessInstanceById(execution.getProcessInstanceId());
            logger.info("NoticeServiceImpl:Workflow has suspend because of assign task to people call exception");
            return false;
        }

    }

    /**
     * 给审核员发送消息
     * @param assignee
     */
    private void sendNoticeMessage(String assignee) {
        NoticeMessage messageInfo = new NoticeMessage();
        messageInfo.setReceiverRoleType(RoleType.AUDITOR);
        messageInfo.setReceiverId(assignee);
        messageInfo.setEvent(auditorEvent);
        messageInfo.setContent(auditorNoticeContent);
        messageInfo.setMessageType(MessageType.NOTICE_APPLY_AUDITOR_START);
        socketServiceImpl.sendMessage(messageInfo);
    }
}
