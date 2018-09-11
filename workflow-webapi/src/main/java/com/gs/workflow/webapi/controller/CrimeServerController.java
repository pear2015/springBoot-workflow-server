package com.gs.workflow.webapi.controller;

import com.gs.workflow.common.annotation.LimitIPRequestAnnotation;
import com.gs.workflow.common.configs.HttpError;
import com.gs.workflow.systemmanager.contract.model.crms.AnalystAndProcess;
import com.gs.workflow.systemmanager.contract.model.crms.AuditorApportion;
import com.gs.workflow.systemmanager.contract.model.crms.WaitApportion;

import com.gs.workflow.systemmanager.service.serviceimpl.crime.WaitApportionServiceImpl;
import com.gs.workflow.systemmanager.service.serviceimpl.crime.WorkFlowServiceImpl;
import io.swagger.annotations.*;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhengyali on 2017/10/10.
 */
@RestController
@RequestMapping(value = "/api/v1", produces = "application/json")
@Api(value = "/api/v1", description = "Workflow Api")
public class CrimeServerController {
    @Autowired
    private WorkFlowServiceImpl workFlowService;
    @Autowired
    private WaitApportionServiceImpl waitApportionService;

    /**
     * 审核席：批量激活流程
     *
     * @param auditorApportion
     * @return
     */
    @RequestMapping(value = "/process/active", method = RequestMethod.POST)
    @ApiOperation(value = "批量激活流程", notes = "workflow controller")
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Internal Server Error", response = HttpError.class),
            @ApiResponse(code = 406, message = "Not Acceptable", response = HttpError.class)})
    @LimitIPRequestAnnotation(limitCounts = 500, timeSecond = 1000)
    public ResponseEntity<List<String>> activeProcesses(@ApiParam(value = "流程实例ID集合", required = true) @RequestBody AuditorApportion auditorApportion) {
        List<String> apportionList = new ArrayList<>();
        if (CollectionUtils.isEmpty(auditorApportion.getWaitApportionList()) || CollectionUtils.isEmpty(auditorApportion.getUserIds())) {
            return new ResponseEntity<>(apportionList, HttpStatus.BAD_REQUEST);
        }
        apportionList = workFlowService.activateProcessByProcessIdList(auditorApportion.getWaitApportionList(), auditorApportion.getUserIds());
        return new ResponseEntity<>(apportionList, HttpStatus.OK);
    }

    /**
     * 犯罪系统:批量激活流程并且设置分析员
     *
     * @param analystAndProcesses
     * @return
     */
    @RequestMapping(value = "/process/active/analyst", method = RequestMethod.POST)
    @ApiOperation(value = "犯罪系统:批量激活流程并且设置分析员", notes = "workflow controller")
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Internal Server Error", response = HttpError.class),
            @ApiResponse(code = 406, message = "Not Acceptable", response = HttpError.class)})
    @LimitIPRequestAnnotation(limitCounts = 500, timeSecond = 1000)
    public ResponseEntity<List<String>> activeProcessesSetAnalyst(@ApiParam(value = "流程实例ID和分析员ID集合", required = true) @RequestBody List<AnalystAndProcess> analystAndProcesses) {
        List<String> apportionList = new ArrayList<>();
        if (analystAndProcesses.isEmpty()) {
            return new ResponseEntity<>(apportionList, HttpStatus.BAD_REQUEST);
        }
        apportionList = workFlowService.activateProcessAndSetAnalyst(analystAndProcesses);
        return new ResponseEntity<>(apportionList, HttpStatus.OK);
    }

    /**
     * 获取等待分派的异常列表
     *
     * @return
     */
    @RequestMapping(value = "/waitApportion/findAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取等待分派的异常列表", notes = "workflow controller")
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Internal Server Error", response = HttpError.class),
            @ApiResponse(code = 406, message = "Not Acceptable", response = HttpError.class)})
    @LimitIPRequestAnnotation(limitCounts = 500, timeSecond = 1000)
    public ResponseEntity<List<WaitApportion>> activeProcessesSetAnalyst() {
        return new ResponseEntity<>(waitApportionService.findAll(), HttpStatus.OK);
    }

}
