package com.gs.workflow.systemmanager.service.delegate.notice;

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
 * Created by zhengyali on 2017/10/31.
 * 公告驳回给归档员
 */
@Component("NoticeReFilerMesDelegate")
public class NoticeReFilerMesDelegate implements JavaDelegate {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Value("${socket.server.url}")
    private String socketServerUrl;

    @Value("${filer.event}")
    private String filerEvent;

    @Value("${message.analysis.reply.notice.content:}")
    private String noticeReplyContent;

    @Autowired
    private TaskService taskService;
    @Autowired
    private WorkFlowServiceImpl workFlowServiceImpl;
    /**
     * 公告申请被驳回 给归档员发送消息
     * @param execution
     * @throws Exception
     */
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String filerId = (String) execution.getVariable("operator");
        String auditorId = (String) execution.getVariable("auditor");
        boolean flag =  workFlowServiceImpl.sendMessageHasSenderId(filerId, RoleType.FILER, filerEvent, noticeReplyContent,  MessageType.NOTICE_AUDITOR_REFUSE,auditorId);

        if (!flag) {
            logger.info("Access Socket Server To Send Message Called Exception");
        }
        Task singleTask = this.taskService.createTaskQuery().processInstanceId(execution.getProcessInstanceId()).singleResult();
        if (singleTask != null) {
            this.taskService.setAssignee(singleTask.getId(), filerId);
        }
        logger.info("Send Reply Analysis Message From Government Application Task Over!");
    }
}
