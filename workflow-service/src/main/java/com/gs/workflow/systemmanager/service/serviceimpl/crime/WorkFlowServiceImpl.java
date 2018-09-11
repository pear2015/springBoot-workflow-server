package com.gs.workflow.systemmanager.service.serviceimpl.crime;

import com.gs.workflow.common.enums.MessageType;
import com.gs.workflow.common.enums.RoleType;
import com.gs.workflow.common.util.VariableUtil;
import com.gs.workflow.systemmanager.contract.model.common.NoticeMessage;
import com.gs.workflow.systemmanager.contract.model.common.User;
import com.gs.workflow.systemmanager.contract.model.crms.AnalystAndProcess;
import com.gs.workflow.systemmanager.contract.model.crms.ApplyAnalystAuditor;
import com.gs.workflow.systemmanager.contract.model.crms.WaitApportion;
import com.gs.workflow.systemmanager.service.util.BubberSortLessTaskAssignee;

import org.apache.commons.lang.StringUtils;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zhengyali on 2017/9/29.
 */

@Service
public class WorkFlowServiceImpl {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private CrimeServiceImpl crimeServiceImpl;

    @Autowired
    private SocketServiceImpl socketServiceImpl;
    @Autowired
    private WaitApportionServiceImpl waitApportionService;
    @Autowired
    private TaskService taskService;

    @Value("${analyst.event}")
    private String analystEvent;

    @Value("${auditor.event}")
    private String auditorEvent;
    @Value("${message.analysis.person.content}")
    private String analystPersonContent;
    @Value("${message.analysis.government.content}")
    private String analystGovernContent;
    @Value("${message.analysis.notice.content}")
    private String analystNoticeContent;


    @Value("${message.audit.person.content}")
    private String auditorPersonContent;
    @Value("${message.audit.government.content}")
    private String auditorGovernContent;

    @Value("${message.audit.notice.content}")
    private String auditorNoticeContent;

    private BubberSortLessTaskAssignee bubberSortLessTaskAssignee = new BubberSortLessTaskAssignee();

    /**
     * 分析席位分派前检查
     * 挂起：
     * 1、分析员
     * 1.1 没有在线分析员
     * 1.2 没有空闲分析员
     * 1.2 保存等待分派失败（保存到工作流的等待分派数据库）
     * 1.3  保存关联关系失败 (保存到工作流的等待分派数据库)
     * 2、审核员
     * 2.1 没有在线审核员
     * 2.2 保存等待分派失败（保存到工作流的等待分派数据库）
     * 2.3  保存关联关系失败 (保存到工作流的等待分派数据库)
     * 激活
     * 保存依赖关系成功
     **/
    public boolean apportionResolve(DelegateExecution execution, String roleType, String businessType) {
        //1.根据角色类型获取在线人员
        try {
            List<User> userList = socketServiceImpl.getUserListByRoleType(roleTypeExchange(roleType));
            //在线人员
            if (CollectionUtils.isEmpty(userList)) {
                //没有人直接挂起
                execution.setVariable(roleType, "");
                String priority = (String) execution.getVariable(VariableUtil.PRIORITY);
                saveWaitApportion(roleType, execution.getBusinessKey(), execution.getProcessInstanceId(), businessType, priority);
                runtimeService.suspendProcessInstanceById(execution.getProcessInstanceId());
                logger.info(VariableUtil.CURRENTTIME, new Date().  (), "WorkFlowServiceImpl:Suspend Process because of no people on line", execution.getProcessInstanceId());
            } else {
                // 有人 任务分派
                taskAssignee(roleType, getUserByRoleType(roleType, userList, execution.getProcessDefinitionId()), execution, businessType);
            }
        } catch (Exception e) {
            logger.error(new Date().toString() + "WorkFlowServiceImpl:apportionResolve has Exception", e);
            return false;
        }
        return true;
    }

    /**
     * 任务分派
     * a 没有分派到 流程挂起
     * b 分派到 处理流程
     *
     * @param roleType
     * @param assignee
     * @param execution
     * @return
     */
    public boolean taskAssignee(String roleType, String assignee, DelegateExecution execution, String businessType) {
        try {
            String priority = (String) execution.getVariable(VariableUtil.PRIORITY);

            if (StringUtils.isBlank(assignee)) {
                execution.setVariable(roleType, "");
                runtimeService.suspendProcessInstanceById(execution.getProcessInstanceId());
                //保存等待分派
                saveWaitApportion(roleType, execution.getBusinessKey(), execution.getProcessInstanceId(), businessType, priority);
                logger.info(VariableUtil.CURRENTTIME, "WorkFlowServiceImpl:Suspend Process because of no people to assign", execution.getProcessInstanceId());
            } else {
                resolveTaskAssignee(roleType, assignee, execution, businessType, priority);
            }
            return true;
        } catch (Exception e) {
            logger.error(new Date().toString() + "WorkFlowServiceImpl:taskAssignee has Exception", e);
            execution.setVariable(roleType, "");
            String priority = (String) execution.getVariable(VariableUtil.PRIORITY);
            saveWaitApportion(roleType, execution.getBusinessKey(), execution.getProcessInstanceId(), businessType, priority);
            runtimeService.suspendProcessInstanceById(execution.getProcessInstanceId());
            return false;
        }

    }

    /**
     * 分派到 处理流程
     */
    private boolean resolveTaskAssignee(String roleType, String assignee, DelegateExecution execution, String businessType, String priority) {
        try {
            execution.setVariable(roleType, assignee);
            // 保存依赖关系成功 任务分派 发送消息
            if (saveApplyAnalystAuditor(roleType, assignee, execution.getBusinessKey(), businessType, priority)) {
                Task singleTask = this.taskService.createTaskQuery().processInstanceId(execution.getProcessInstanceId()).singleResult();
                if (singleTask != null) {
                    this.taskService.setAssignee(singleTask.getId(), assignee);
                }
                String event = RoleType.compareRoleType(roleType, RoleType.ANALYST) ? analystEvent : auditorEvent;
                String content = resolveMessageContent(roleType, businessType);
                MessageType messageType = RoleType.compareRoleType(roleType, RoleType.ANALYST) ? MessageType.PERSON_APPLY_ANALYST : MessageType.GOVERNMENT_APPLY_AUDITOR_START;
                sendMessage(assignee, roleType, event, content, messageType);
            } else {
                //保存依赖关系 失败  流程挂起保存到等待分派列表
                execution.setVariable(roleType, "");
                runtimeService.suspendProcessInstanceById(execution.getProcessInstanceId());
                //调用工作流保存等待分派失败保存到本地数据库
                saveWaitApportion(roleType, execution.getBusinessKey(), execution.getProcessInstanceId(), businessType, priority);
                logger.info(VariableUtil.CURRENTTIME, "WorkFlowServiceImpl:Suspend Process because of save relation between assignee and task fail", execution.getProcessInstanceId());
            }
            return true;
        } catch (Exception e) {
            logger.error(new Date().toString() + "WorkFlowServiceImpl:resolveTaskAssignee has Exception", e);
            return false;
        }
    }

    /**
     * 根据业务类型处理消息体
     */
    private String resolveMessageContent(String roleType, String businessType) {
        if (StringUtils.isEmpty(roleType) || StringUtils.isEmpty(businessType)) {
            logger.info("roleType or businessType is empty when roleType=? businessType=?", roleType, businessType);
            return null;
        } else {
            return getResolveMessageContent(roleType, businessType);
        }
    }

    /**
     * 消息类型处理
     *
     * @param roleType
     * @param businessType
     * @return
     */
    private String getResolveMessageContent(String roleType, String businessType) {
        if (VariableUtil.BNUSSINEESSPERSON.equals(businessType)) {
            return RoleType.compareRoleType(roleType, RoleType.ANALYST) ? analystPersonContent : auditorPersonContent;
        } else if (VariableUtil.BNUSSINEESSPGOVER.equals(businessType)) {
            return RoleType.compareRoleType(roleType, RoleType.ANALYST) ? analystGovernContent : auditorGovernContent;
        } else if (VariableUtil.BNUSSINEESSNOTICE.equals(businessType)) {
            return RoleType.compareRoleType(roleType, RoleType.ANALYST) ? analystNoticeContent : auditorNoticeContent;
        }
        return null;
    }

    /**
     * 根据角色获取指定空闲用户(分析员获取空闲的 审核员获取在线的审核员)
     * 分析员 获取空闲第一个分析员
     * 审核员 获取任务最少的审核员
     *
     * @param roleType
     * @param userList
     * @return
     */
    public String getUserByRoleType(String roleType, List<User> userList, String processDefinedId) {
        List<String> userOnline = new ArrayList<>();
        userList.forEach(user -> userOnline.add(user.getUserId()));
        if (RoleType.ANALYST.equals(roleTypeExchange(roleType))) {
            List<String> userFreeList = crimeServiceImpl.getAnalystFreeList(userOnline);
            //拿到没有任务的分析员
            return CollectionUtils.isEmpty(userFreeList) ? null : userFreeList.get(0);
        } else if (RoleType.AUDITOR.equals(roleTypeExchange(roleType))) {
            return bubberSortLessTaskAssignee.getLessTaskAssigneeId(userOnline, processDefinedId);
        }
        return null;
    }

    /**
     * 分派后保存依赖关系
     *
     * @param roleType
     * @param analystName
     * @param applyId
     * @return
     */
    public boolean saveApplyAnalystAuditor(String roleType, String analystName, String applyId, String businessType, String priority) {
        if (StringUtils.isNotEmpty(analystName)) {
            ApplyAnalystAuditor applyAnalystAuditor = new ApplyAnalystAuditor();
            applyAnalystAuditor.setApplyInfoId(applyId);
            applyAnalystAuditor.setAnalystId(RoleType.compareRoleType(roleType, RoleType.ANALYST) ? analystName : "");
            applyAnalystAuditor.setAuditorId(RoleType.compareRoleType(roleType, RoleType.AUDITOR) ? analystName : "");
            applyAnalystAuditor.setCreateTime(new Date());
            applyAnalystAuditor.setAnalysisResultFail(0);
            applyAnalystAuditor.setBusinessType(businessType);
            applyAnalystAuditor.setPriority(priority);
            if (!crimeServiceImpl.saveApplyAnalystAuditor(applyAnalystAuditor)) {
                logger.info("Access Crime Server Exception");
                return false;
            }
        }
        return true;
    }

    /**
     * 保存等待分派数据
     *
     * @param roleType
     * @param applyId
     * @param processId
     * @return
     */
    public boolean saveWaitApportion(String roleType, String applyId, String processId, String businessType, String priority) {
        try {
            WaitApportion waitApportion = new WaitApportion();
            waitApportion.setApplyInfoId(applyId);
            waitApportion.setProcessId(processId);
            waitApportion.setBusinessType(businessType);
            waitApportion.setApplyPriority(priority);
            waitApportion.setWaitReasonType(roleType.equals(VariableUtil.ANALYST) ? "1" : "2");//因为没有分析员在线而挂起
            boolean isSuccess = crimeServiceImpl.saveWaitApportion(waitApportion);
            if (!isSuccess) {
                waitApportionService.saveWaitApportion(waitApportion);
                logger.info("Access Crime server exception");
            }
            return isSuccess;
        } catch (Exception e) {
            logger.error("saveWaitApportion has Exception", e);
            return false;
        }
    }

    /**
     * 发送消息
     *
     * @param receiverId
     * @param event
     * @param roleType
     * @param content
     * @return
     */
    public boolean sendMessage(String receiverId, String roleType, String event, String content, MessageType messageType) {
        NoticeMessage messageInfo = new NoticeMessage();
        messageInfo.setReceiverRoleType(roleTypeExchange(roleType));
        messageInfo.setReceiverId(receiverId);
        messageInfo.setEvent(event);
        messageInfo.setContent(content);
        messageInfo.setMessageType(messageType);
        return socketServiceImpl.sendMessage(messageInfo);
    }

    /**
     * 当存在发送人的时候
     *
     * @param receiverId
     * @param roleType
     * @param event
     * @param content
     * @param messageType
     * @param senderId
     * @return
     */
    public boolean sendMessageHasSenderId(String receiverId, RoleType roleType, String event, String content, MessageType messageType, String senderId) {
        NoticeMessage messageInfo = new NoticeMessage();
        messageInfo.setReceiverRoleType(roleType);
        messageInfo.setReceiverId(receiverId);
        messageInfo.setEvent(event);
        messageInfo.setSenderId(senderId);
        messageInfo.setContent(content);
        messageInfo.setMessageType(messageType);
        return socketServiceImpl.sendMessage(messageInfo);
    }

    /**
     * 角色转化
     */
    public RoleType roleTypeExchange(String roleType) {
        if (StringUtils.isBlank(roleType)) {
            return null;
        } else if (RoleType.compareRoleType(roleType, RoleType.ANALYST)) {
            return RoleType.ANALYST;
        } else if (RoleType.compareRoleType(roleType, RoleType.AUDITOR)) {
            return RoleType.AUDITOR;
        } else if (RoleType.compareRoleType(roleType, RoleType.OPERATOR)) {
            return RoleType.OPERATOR;
        }
        return null;

    }

    /**
     * 激活流程实例并设置分析员
     * 流程激活成功  保存依赖关系成功 加入分派成功列表
     * 流程激活失败  保存依赖关系失败
     *
     * @param analystAndProcesses
     * @return
     */
    public List<String> activateProcessAndSetAnalyst(List<AnalystAndProcess> analystAndProcesses) {
        List<String> apportionList = new ArrayList<>();
        analystAndProcesses.forEach(analystAndProcess -> {
                    try {
                        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(analystAndProcess.getProcessId()).singleResult();
                        Task singleTask = this.taskService.createTaskQuery().processInstanceId(analystAndProcess.getProcessId()).singleResult();
                        if (processInstance != null && singleTask != null) {
                            runtimeService.setVariable(analystAndProcess.getProcessId(), VariableUtil.ANALYST, analystAndProcess.getAnalystId());
                            runtimeService.activateProcessInstanceById(analystAndProcess.getProcessId());
                            this.taskService.setAssignee(singleTask.getId(), analystAndProcess.getAnalystId());
                            //保存关联关系 分析席
                            if (saveDataWhenAnalyst(analystAndProcess.getAnalystId(), processInstance.getBusinessKey(), analystAndProcess.getProcessId(), analystAndProcess.getPriority(), analystAndProcess.getBusinessType())) {
                                //保存关联关系 分析席
                                apportionList.add(processInstance.getBusinessKey());
                            }
                        }
                    } catch (Exception e) {
                        logger.error(new Date().toString() + "WorkFlowServiceImpl:analyst processInstanceId" + analystAndProcess.getProcessId() + "active is failure", e);
                    }
                }
        );
        return apportionList;
    }

    /**
     * 分析流程处理成功 保存关系数据
     */
    private boolean saveDataWhenAnalyst(String analystId, String businessKey, String processId, String priority, String businessType) {
        try {
            //保存关联关系 分析席
            if (StringUtils.isNotEmpty(businessKey) && saveApplyAnalystAuditor(VariableUtil.ANALYST, analystId, businessKey, "1", priority)) {
                String content = resolveMessageContent(VariableUtil.ANALYST, businessType);
                sendMessage(analystId, VariableUtil.ANALYST, analystEvent, content, MessageType.PERSON_APPLY_ANALYST);
                return true;
            } else {
                runtimeService.suspendProcessInstanceById(processId);
                logger.info(VariableUtil.CURRENTTIME, "WorkFlowServiceImpl:Suspend Process because of save relation between assignee and task fail", processId);
                return false;
            }
        } catch (Exception e) {
            logger.error(new Date().toString() + "WorkFlowServiceImpl:saveApplyAnalystAuditor has Exception ", e);
            return false;
        }
    }


    /**
     * 审核席 批量激活
     * 1.先进行流程激活
     * 2、激活成功 进行保存
     * 修改的内容为:添加了业务类型
     */
    public List<String> activateProcessByProcessIdList(List<WaitApportion> waitApportionList, List<String> users) {
        List<String> apportionList = new ArrayList<>();
        waitApportionList.forEach(waitApportion -> {
            try {
                ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(waitApportion.getProcessId()).singleResult();
                Task singleTask = this.taskService.createTaskQuery().processInstanceId(waitApportion.getProcessId()).singleResult();
                if (processInstance != null && singleTask != null) {
                    runtimeService.activateProcessInstanceById(waitApportion.getProcessId());
                    String auditor = bubberSortLessTaskAssignee.getLessTaskAssigneeId(users, processInstance.getProcessDefinitionId());
                    this.taskService.setAssignee(singleTask.getId(), auditor);
                    //保存关联关系 审核席
                    if (saveDataWhenAudit(auditor, processInstance.getBusinessKey(), waitApportion.getBusinessType(), waitApportion.getApplyPriority(), processInstance.getProcessInstanceId(), waitApportion.getProcessId())) {
                        apportionList.add(processInstance.getBusinessKey());
                    }
                }

            } catch (Exception e) {
                logger.info(new Date().toString() + "WorkFlowServiceImpl:auditor processInstanceId" + waitApportion.getProcessId() + "active is failure", e);
            }
        });
        return apportionList;
    }

    /**
     * 审核流程处理成功 保存关系数据
     */
    private boolean saveDataWhenAudit(String auditor, String businessKey, String businessType, String priority, String processInstanceId, String processId) {
        try {
            if (StringUtils.isNotEmpty(businessKey) && saveApplyAnalystAuditor(VariableUtil.AUDITOR, auditor, businessKey, businessType, priority)) {
                runtimeService.setVariable(processInstanceId, VariableUtil.AUDITOR, auditor);
                this.sendMessageByBusinessType(auditor, businessType);
                return true;
            } else {
                runtimeService.suspendProcessInstanceById(processId);
                runtimeService.setVariable(processInstanceId, VariableUtil.AUDITOR, "");
                logger.info(VariableUtil.CURRENTTIME, "WorkFlowServiceImpl:Suspend Process because of save  auditor relation between assignee and task fail", processId);
                return false;
            }
        } catch (Exception e) {
            logger.error("saveApplyAnalystAuditor has Exception ", e);
            return false;
        }
    }

    /**
     * 根据业务类型发送不同的消息
     *
     * @param auditor
     * @param businessType
     */
    private void sendMessageByBusinessType(String auditor, String businessType) {
        String content = resolveMessageContent(VariableUtil.AUDITOR, businessType);
        if ("1".equals(businessType)) {
            sendMessage(auditor, VariableUtil.AUDITOR, auditorEvent, content, MessageType.PERSON_APPLY_AUDITOR_START);
        } else if ("2".equals(businessType)) {
            sendMessage(auditor, VariableUtil.AUDITOR, auditorEvent, content, MessageType.GOVERNMENT_APPLY_AUDITOR_START);
        } else {
            sendMessage(auditor, VariableUtil.AUDITOR, auditorEvent, content, MessageType.NOTICE_APPLY_AUDITOR_START);
        }
    }
}
