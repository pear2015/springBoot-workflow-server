package com.gs.workflow.systemmanager.service.delegate.crime;

import com.gs.workflow.common.enums.MessageType;
import com.gs.workflow.common.enums.RoleType;
import com.gs.workflow.systemmanager.service.serviceimpl.crime.WorkFlowServiceImpl;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.task.Task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by zhangqiang on 2017/9/22.
 * 发送消息给分析员，需要申请重新分析
 */
@Component("PSendReAnalysisMesDelegate")
public class PSendReAnalysisMesDelegate implements JavaDelegate {

    @Value("${socket.server.url}")
    private String socketServerUrl;

    @Value("${analyst.event}")
    private String analystEvent;

    @Value("${message.analysis.reply.person.content}")
    private String replyAnalysisContent;
    @Autowired
    private TaskService taskService;
    @Autowired
    private WorkFlowServiceImpl workFlowServiceImpl;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     *
     * @param execution
     * @throws Exception
     * 个人申请 审核员拒绝审核
     * 1、调用消息通知给分析员发送消息
     * 2、将当前的Task 中 Assignee 设为首次分析的分析员
     */
    @Override
    public void execute(DelegateExecution execution) throws Exception {

        String analystId = (String) execution.getVariable("analyst");
        String auditorId = (String) execution.getVariable("auditor");
        boolean flag =  workFlowServiceImpl.sendMessageHasSenderId(analystId, RoleType.ANALYST, analystEvent, replyAnalysisContent,  MessageType.PERSON_APPLY_AUDITOR_REFUSE,auditorId);
        if (!flag) {
            logger.info("Access Socket Server To Send Message Called Exception when person  reply auditor->operator");
        }
        Task singleTask = this.taskService.createTaskQuery().processInstanceId(execution.getProcessInstanceId()).singleResult();
        if (singleTask != null) {
            this.taskService.setAssignee(singleTask.getId(), analystId);
        }
        logger.info("Send Reply Analysis Message From Government Application Task Over!");
    }
}
