package com.gs.workflow.systemmanager.service.delegate.notice;

import com.gs.workflow.systemmanager.service.serviceimpl.crime.NoticeServiceImpl;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by zhengyali on 2017/10/31.
 * 检查在线审核员
 */
@Component("NoticeToAuditorDelegate")
public class NoticeToAuditorDelegate   implements JavaDelegate {
    @Autowired
    private NoticeServiceImpl noticeService;
    /**
     * 查询审核员列表
     * 是否存在在线审核员
     * 存在 任务分派 保存关系
     * 不存在 流程挂起
     * @param execution
     * @throws Exception
     */
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        noticeService.noticeApportionResolve(execution);
    }
}
