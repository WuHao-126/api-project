package com.wuhao.project.controller;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.wuhao.project.constant.UserConstant;
import com.wuhao.project.mapper.CommonMapper;
import com.wuhao.project.mapper.WebInfoMapper;
import com.wuhao.project.model.entity.InterfaceInfo;
import com.wuhao.project.model.entity.Tag;
import com.wuhao.project.model.entity.User;
import com.wuhao.project.annotation.AuthCheck;
import com.wuhao.project.common.*;
import com.wuhao.project.constant.CommonConstant;
import com.wuhao.project.exception.BusinessException;
import com.wuhao.project.exception.ThrowUtils;
import com.wuhao.project.model.enmus.InterfaceInfoStatusEnum;
import com.wuhao.project.model.request.interfaces.*;
import com.wuhao.project.model.response.TimeoutInterfaceResponse;
import com.wuhao.project.service.InterfaceInfoService;
import com.wuhao.project.service.UserService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/interface")
public class InterfaceInfoController {

    @Autowired
    private UserService userService;
    @Autowired
    private InterfaceInfoService interfaceInfoService;

    @Autowired
    private CommonMapper commonMapper;

    @Autowired
    private WebInfoMapper webInfoMapper;

    @PostMapping("/add")
    @AuthCheck(anyRole = {UserConstant.SUPER_ADMIN_ROLE,UserConstant.ADMIN_ROLE})
    public Result addInterfaceInfo(@RequestBody InterfaceInfoAddRequest interfaceInfoAddRequest, HttpServletRequest request){
        if(interfaceInfoAddRequest ==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String name = interfaceInfoAddRequest.getName();
        String url = interfaceInfoAddRequest.getUrl();
        String description = interfaceInfoAddRequest.getDescription();
        String method = interfaceInfoAddRequest.getMethod();
        Integer type = interfaceInfoAddRequest.getType();
        String requestHeaderParams = interfaceInfoAddRequest.getRequestHeaderParams();
        String responseFieldParams = interfaceInfoAddRequest.getResponseFieldParams();
//        if(StringUtils.isAnyBlank(name,requestHeaderParams,responseFieldParams,url,description,method)){
//            return Result.error(ErrorCode.PARAMS_ERROR);
//        }
        InterfaceInfo interfaceInfo=new InterfaceInfo();
        //请求参数可以为空，但响应参数不可
        BeanUtils.copyProperties(interfaceInfoAddRequest,interfaceInfo);
//        User loginUser = userService.getLoginUser(request);
        interfaceInfo.setCreateBy(1l);
        boolean result = interfaceInfoService.save(interfaceInfo);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return Result.success(true);
    }

    @PostMapping("/delete")
//    @AuthCheck(mustRole = UserConstant.SUPER_ADMIN_ROLE)
    public Result deleteInterfaceInfo(@RequestBody DeleteRequest deleteRequest,HttpServletRequest request){
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
//        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
//        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
//        ThrowUtils.throwIf(oldInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);
//        // 仅本人或管理员可删除
//        if (!oldInterfaceInfo.getCreateBy().equals(user.getId()) && !userService.isAdmin(request)) {
//            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
//        }
        boolean b = interfaceInfoService.removeById(id);
        return Result.success(b);
    }

    @PostMapping("/update")
    @AuthCheck(anyRole = {UserConstant.SUPER_ADMIN_ROLE,UserConstant.ADMIN_ROLE})
    public Result updateInterfaceInfo(@RequestParam InterfaceInfoUpdateRequest interfaceInfoUpdateRequest,
                                                     HttpServletRequest request){
        if (interfaceInfoUpdateRequest == null || interfaceInfoUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoUpdateRequest, interfaceInfo);
        // 参数校验
        interfaceInfoService.validInterfaceInfo(interfaceInfo, false);
        User user = userService.getLoginUser();
        long id = interfaceInfoUpdateRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可修改
        if (!oldInterfaceInfo.getCreateBy().equals(user.getId()) && !userService.isAdmin(user)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = interfaceInfoService.updateById(interfaceInfo);
        return Result.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get/{id}")
    public Result getInterfaceInfoById(@PathVariable long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);
        Tag tag=webInfoMapper.getTagById(interfaceInfo.getType());
        interfaceInfo.setTypeName(tag.getName());
        return Result.success(interfaceInfo);
    }

    /**
     * 分页获取列表
     * @return
     */
    @PostMapping("/page")
    public Result listInterfaceInfoByPage(@RequestBody InterfaceInfoQueryRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long current = request.getCurrent();
        long size = request.getPageSize();
        String keywords = request.getKeywords();
        Date beginDate = request.getBeginDate();
        Date endDate = request.getEndDate();
        String name = request.getName();
        Integer type = request.getType();
        Integer state = request.getState();
        String url = request.getUrl();
        // 限制爬虫
        if (size > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(request.getMethod()),"method",request.getMethod())
                    .eq(state!=null,"state",request.getState())
                    .eq(!StringUtils.isBlank(url),"url",url)
                    .eq(type!=null,"type",type)
                    .between(beginDate!=null && endDate!=null,"createTime",beginDate,endDate)
                    .like(StringUtils.isNotBlank(keywords),"name",keywords)
                    .or()
                    .like(StringUtils.isNotBlank(keywords),"description",keywords)
                    .or()
                    .like(StringUtils.isNotBlank(keywords),"url",keywords)
                    .like(StringUtils.isNotBlank(name),"name",name);
        //查询出用户信息
        Page<InterfaceInfo> interfaceInfoPage = interfaceInfoService.page(new Page<>(current, size), queryWrapper);
        Map<Long, String> collect = userService.list().stream().collect(Collectors.toMap(User::getId, User::getUserName));
        Map<Integer, String> tagList = webInfoMapper.getAllTags(new Page(1, 100)).getRecords().stream().collect(Collectors.toMap(Tag::getId, Tag::getName));
        interfaceInfoPage.getRecords().stream().forEach(data ->{
            Long createBy = data.getCreateBy();
            data.setCreateByName(collect.get(createBy));
            data.setTypeName(tagList.get(data.getType()));
        });
        return Result.success(interfaceInfoPage);
    }

    @PostMapping("/invoke")
    public Result invokeInterface(@RequestBody InvokeInterfaceRequest invokeInterfaceRequest){
        Long id = invokeInterfaceRequest.getId();
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);
        Integer state = interfaceInfo.getState();
        if(state==0){
            return Result.error(404,"此接口已经关闭，请联系管理员");
        }
        String url = invokeInterfaceRequest.getUrl();
        String method = "GET";
        Map<String,Object> requestParamsMap=new HashMap<>();
        Map<String,String> headerParamsMap=new HashMap<>();
        List<RequestHeaderParam> requestHeaderParams = invokeInterfaceRequest.getRequestHeaderParams();
        List<RequestFieldParam> requestFieldParams = invokeInterfaceRequest.getRequestFieldParams();
        for (RequestFieldParam requestFieldParam : requestFieldParams) {
            //TODO 先写死
            String required = requestFieldParam.getRequired();
            String fieldName = requestFieldParam.getFieldName();
            String value = requestFieldParam.getValue();
            if("accessKey".equals(fieldName) || "secretKey".equals(fieldName)){
                continue;
            }
            if("是".equals(required) && StringUtils.isEmpty(value)){
                return Result.error(ErrorCode.PARAMS_ERROR);
            }
            requestParamsMap.put(fieldName,value);
        }
        Map<String, String> map=new HashMap<>();
        map.put("api-gateway-test","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");
        if("GET".equals(method)){
            HttpRequest form = HttpRequest.get(url).addHeaders(map).form(requestParamsMap);
            HttpResponse execute = form.execute();
            String body = execute.body();
            return Result.success(body);
        }else if ("POST".equals(method)){
            HttpRequest request = HttpRequest.post(url).addHeaders(map).body(JSONUtil.toJsonStr(requestParamsMap));
            String body = request.execute().body();
            return Result.success(body);
        }else{
            Result.error(ErrorCode.PARAMS_ERROR);
        }
        return Result.error(ErrorCode.OPERATION_ERROR);
    }

    // endregion

    /**
     * 发布
     * 修改接口信息为发布状态
     * @param idRequest
     * @return
     */
    @PostMapping("/online")
//    @AuthCheck(anyRole = {UserConstant.SUPER_ADMIN_ROLE,UserConstant.ADMIN_ROLE})
    public Result onlineInterfaceInfo(@RequestBody IdRequest idRequest) {
        if (idRequest == null || idRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long id = idRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // TODO 判断该接口是否可以调用+
//        String url = oldInterfaceInfo.getUrl();
//        String method = oldInterfaceInfo.getMethod();
//        String userRequestParams = oldInterfaceInfo.getRequestParams();
//        Gson gson=new Gson();
//        Map map=new HashMap();
//        Map paramsMap = gson.fromJson(userRequestParams, map.getClass());
//        String response = apiClient.getResponse(url,method,paramsMap);
//        if (StringUtils.isBlank(response)) {
//            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "接口验证失败");
//        }
        // 仅本人或管理员可修改

        oldInterfaceInfo.setState(InterfaceInfoStatusEnum.ONLINE.getValue());
        boolean result = interfaceInfoService.updateById(oldInterfaceInfo);
        return Result.success(result);
    }

    /**
     * 下线
     * @param idRequest
     * @return
     */
    @PostMapping("/offline")
    @AuthCheck(anyRole = {UserConstant.SUPER_ADMIN_ROLE,UserConstant.ADMIN_ROLE})
    public Result offlineInterfaceInfo(@RequestBody IdRequest idRequest) {
        if (idRequest == null || idRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long id = idRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可修改
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        interfaceInfo.setId(id);
        interfaceInfo.setState(InterfaceInfoStatusEnum.OFFLINE.getValue());
        boolean result = interfaceInfoService.updateById(interfaceInfo);
        return Result.success(result);
    }


    @GetMapping("/tag")
    public Result getInterfaceTags(){
        return Result.success(commonMapper.getBlogTag("INTERFACE"));
    }


    @GetMapping("/timeout")
    @AuthCheck(anyRole = {"admin","superadmin"})
    public Result getTimeoutInterface(Page page){
        Page<TimeoutInterfaceResponse> page1 = interfaceInfoService.getTimeoutList(page);
        return Result.success(page1);
    }

    @GetMapping("/timeout/delete")
    @AuthCheck(anyRole = {"admin","superadmin"})
    public Result deleteTimeoutData(Long id){
        interfaceInfoService.deleteTimeoutData(id);
        return Result.success();
    }
}
