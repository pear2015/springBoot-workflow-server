package com.gs.workflow.systemmanager.service.delegate.crime;

import com.gs.workflow.common.enums.MessageType;
import com.gs.workflow.common.util.VariableUtil;
import com.gs.workflow.systemmanager.service.serviceimpl.crime.WorkFlowServiceImpl;

import org.apache.commons.lang.StringUtils;

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
 * 个人申请：将分析完成的申请分派给审核员代理类
 */
@Component("PApportionToAuditorDelegate")
public class PApportionToAuditorDelegate implements JavaDelegate {

    @Autowired
    private WorkFlowServiceImpl workFlowServiceImpl;
    @Autowired
    private TaskService taskService;
    @Value("${auditor.event}")
    private String auditorEvent;
    @Value("${message.audit.reply.person.content}")
    private String auditorContent;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        /**
         * 前提条件
         * 1 没有设置过审核员
         * 将审核任务分派给在线审核员的步骤
         * 1.1从消息通知获取所有在线的审核员
         * 2.1将任务分给任务量最少的审核员
         * 3.1将这条数据加入到任务和用户关联表中
         * 2调用消息通知 给当前业务的审核员发送消息
         */
        String auditor = (String) execution.getVariable(VariableUtil.AUDITOR);

        if (StringUtils.isBlank(auditor)) {
            if (workFlowServiceImpl.apportionResolve(execution, VariableUtil.AUDITOR,"1")) {
                logger.info("Check analyst is online over!");
            }
        } else {
            execution.setVariable(VariableUtil.AUDITOR, auditor);

            Task singleTask = this.taskService.createTaskQuery().processInstanceId(execution.getProcessInstanceId()).singleResult();
            if (singleTask != null) {
                this.taskService.setAssignee(singleTask.getId(), auditor);
            }

            workFlowServiceImpl.sendMessage(auditor, VariableUtil.AUDITOR, auditorEvent, auditorContent, MessageType.PERSON_APPLY_AUDITOR_START);
        }

        logger.info("Apportion Personal Application To Auditor Task Over!");
    }

}