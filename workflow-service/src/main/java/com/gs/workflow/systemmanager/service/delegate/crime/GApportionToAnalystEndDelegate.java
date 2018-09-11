package com.gs.workflow.systemmanager.service.delegate.crime;

import com.gs.workflow.common.enums.MessageType;
import com.gs.workflow.systemmanager.service.serviceimpl.crime.WorkFlowServiceImpl;

import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.task.Task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by zhengyali on 2017/10/11.
 */
@Component("GApportionToAnalystEndDelegate")
public class GApportionToAnalystEndDelegate implements JavaDelegate {
    @Autowired
    private TaskService taskService;

    @Autowired
    private WorkFlowServiceImpl workFlowServiceImpl;

    @Value("${auditor.event}")
    private String auditorEvent;

    @Value("${message.audit.reply.government.content}")
    private String auditorContent;


    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 重新分析时给审核员发消息
     * @param execution
     * @throws Exception
     */
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String auditor = (String) execution.getVariable("auditor");

        Task singleTask = this.taskService.createTaskQuery().processInstanceId(execution.getProcessInstanceId()).singleResult();
        if (singleTask != null) {
            this.taskService.setAssignee(singleTask.getId(), auditor);
        }

        workFlowServiceImpl.sendMessage(auditor, "auditor", auditorEvent, auditorContent, MessageType.GOVERNMENT_APPLY_AUDITOR_START);

        logger.info("Assignee to analyst task listener over!");
    }

}
