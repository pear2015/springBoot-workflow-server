package com.gs.workflow.webapi.controller;

import com.gs.workflow.common.annotation.LimitIPRequestAnnotation;
import com.gs.workflow.common.configs.HttpError;
import com.gs.workflow.systemmanager.contract.service.CamundaBasicService;

import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
/**
 * Created by zhengyali on 2017/10/10.
 */
@RestController
@RequestMapping(value = "/api/v1", produces = "application/json")
@Api(value = "/api/v1", description = "工作流公共API")
public class CamundaBasicController {

    @Autowired
    private CamundaBasicService camundaBasicService;

    /**
     * 部署流程定义
     *
     * @param file bpmn文件
     * @param
     * @return
     */
    @RequestMapping(value = "/deploy", method = RequestMethod.POST)
    @ApiOperation(value = "部署流程定义", notes = "部署BPMN文件")
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Internal Server Error", response = HttpError.class),
            @ApiResponse(code = 406, message = "Not Acceptable", response = HttpError.class)})
    @LimitIPRequestAnnotation(limitCounts = 500, timeSecond = 1000)
    public ResponseEntity<String> deployProcess(@ApiParam(value = "BPMN文件", required = true) @RequestParam MultipartFile file
    ) {
        String deployId = camundaBasicService.deployProcess(file);

        return new ResponseEntity<>(deployId, HttpStatus.OK);
    }


    /**
     * 启动流程实例
     *
     * @param processKey        流程定义key
     * @param bussinessKey      设置业务key（申请ID）
     * @param workFlowVariables 流程变量
     * @return
     */
    @RequestMapping(value = "/process/start", method = RequestMethod.POST)
    @ApiOperation(value = "启动流程实例", notes = "workflow controller")
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Internal Server Error", response = HttpError.class),
            @ApiResponse(code = 406, message = "Not Acceptable", response = HttpError.class)})
    @LimitIPRequestAnnotation(limitCounts = 500, timeSecond = 1000)
    public ResponseEntity<String> runProcess(@ApiParam(value = "流程定义Key", required = true) @RequestParam String processKey, @ApiParam(value = "业务Key", required = true) @RequestParam String bussinessKey,
                                             @ApiParam(value = "流程变量", required = false) @RequestBody Map<String, Object> workFlowVariables) {
        String processInstanceId = camundaBasicService.startProcessByProcessKey(processKey, bussinessKey, workFlowVariables);
        return new ResponseEntity<>(processInstanceId, HttpStatus.OK);
    }


    /**
     * 完成任务，同时设置流程参数
     *
     * @param taskId
     * @param workFlowVariables
     * @return
     */

    @RequestMapping(value = "/task/complete/{taskId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "通过任务ID完成任务", notes = "workflow controller")
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Internal Server Error", response = HttpError.class),
            @ApiResponse(code = 406, message = "Not Acceptable", response = HttpError.class)})
    @LimitIPRequestAnnotation(limitCounts = 500, timeSecond = 1000)
    public ResponseEntity<Boolean> completeTaskSetVariable(@ApiParam(value = "任务ID", required = true) @PathVariable String taskId, @ApiParam(value = "流程变量map", required = false) @RequestBody Map<String, Object> workFlowVariables) {

        boolean result = camundaBasicService.completeTask(taskId, workFlowVariables);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 完成任务，同时设置流程参数
     *
     * @param assignee
     * @param processId
     * @param variables
     * @return
     */

    @RequestMapping(value = "/task/complete/{businessKey}/{assignee}/{processId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
@ApiOperation(value = "通过任务ID和流程ID完成任务", notes = "workflow controller")
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Internal Server Error", response = HttpError.class),
            @ApiResponse(code = 406, message = "Not Acceptable", response = HttpError.class)})
    @LimitIPRequestAnnotation(limitCounts = 500, timeSecond = 1000)
    public ResponseEntity<Boolean> completeTask(@ApiParam(value = "业务Id", required = true) @PathVariable String businessKey,@ApiParam(value = "任务参与者", required = true) @PathVariable String assignee, @ApiParam(value = "流程实例ID", required = true) @PathVariable String processId,
                                                @ApiParam(value = "流程变量", required = false) @RequestBody Map<String, Object> variables) {

        boolean result = camundaBasicService.completeTaskAndSetVariables(businessKey,assignee, processId, variables);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 获取用户任务
     *
     * @param assignee
     * @return
     */
    @RequestMapping(value = "/task/get/{assignee}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "获取用户任务", notes = "workflow controller")
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Internal Server Error", response = HttpError.class),
            @ApiResponse(code = 406, message = "Not Acceptable", response = HttpError.class)})
    @LimitIPRequestAnnotation(limitCounts =500, timeSecond = 1000)
    public ResponseEntity<List<String>> getTasksByAssignee(@ApiParam(value = "任务参与者", required = true) @PathVariable String assignee) {
        List<String> tasks = camundaBasicService.getTaskByAssignee(assignee);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    /**
     * 获取任务列表
     *
     * @param assignee  参与者ID
     * @param processId 流程实例ID
     * @return
     */
    @RequestMapping(value = "/task/get/{assignee}/{processId}", method = RequestMethod.GET)
    @ApiOperation(value = "通过参与者和流程实例获取单个任务", notes = "workflow controller")
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Internal Server Error", response = HttpError.class),
            @ApiResponse(code = 406, message = "Not Acceptable", response = HttpError.class)})
    @LimitIPRequestAnnotation(limitCounts = 500, timeSecond = 1000)
    public ResponseEntity<String> getSingleTaskByAssignee(@ApiParam(value = "任务参与者", required = true) @RequestParam String assignee, @ApiParam(value = "流程实例ID", required = true) @RequestParam String processId) {

        String taskId = camundaBasicService.getTaskByAssigneeAndProcessId(assignee, processId);

        return new ResponseEntity<>(taskId, HttpStatus.OK);

    }

    /**
     * 激活流程
     *
     * @param id
     * @param workFlowVariables
     * @return
     */
    @RequestMapping(value = "/process/active/{id}", method = RequestMethod.POST)
    @ApiOperation(value = "激活流程", notes = "workflow controller")
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Internal Server Error", response = HttpError.class),
            @ApiResponse(code = 406, message = "Not Acceptable", response = HttpError.class)})
    @LimitIPRequestAnnotation(limitCounts = 500, timeSecond = 1000)
    public ResponseEntity<Boolean> activeProcessByInstanceId(@ApiParam(value = "流程实例ID", required = true) @PathVariable String id, @ApiParam(value = "流程变量map", required = false) @RequestBody Map<String,Object> workFlowVariables) {
        boolean result = camundaBasicService.activeProcessByProcessId(id, workFlowVariables);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    /**
     * 删除正在运行的流程实例
     *
     * @param id                  流程实例
     * @param deleteReason        删除原因
     * @param skipCustomListeners 是否跳过用户监听
     * @return
     */
    @RequestMapping(value = "/process/delete/{id}", method = RequestMethod.POST)
    @ApiOperation(value = "删除正在运行的流程实例", notes = "workflow controller")
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Internal Server Error", response = HttpError.class),
            @ApiResponse(code = 406, message = "Not Acceptable", response = HttpError.class)})
    @LimitIPRequestAnnotation(limitCounts = 500, timeSecond = 1000)
    public ResponseEntity<Boolean> deleteRuntimeProcessInstanceId(@ApiParam(value = "流程实例ID", required = true) @PathVariable String id, @ApiParam(value = "删除原因", required = true) @RequestParam String deleteReason, @ApiParam(value = "是否跳过用户监听", required = true) @RequestParam boolean skipCustomListeners) {

        boolean result = camundaBasicService.deleteRunProcessInstanceById(id, deleteReason, skipCustomListeners);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
