package com.device.inspect.controller;

import com.device.inspect.common.model.charater.Role;
import com.device.inspect.common.model.charater.RoleAuthority;
import com.device.inspect.common.model.charater.User;
import com.device.inspect.common.model.device.*;
import com.device.inspect.common.model.firm.Building;
import com.device.inspect.common.model.firm.Company;
import com.device.inspect.common.model.firm.Room;
import com.device.inspect.common.model.firm.Storey;
import com.device.inspect.common.repository.charater.RoleAuthorityRepository;
import com.device.inspect.common.repository.charater.RoleRepository;
import com.device.inspect.common.repository.charater.UserRepository;
import com.device.inspect.common.repository.device.*;
import com.device.inspect.common.repository.firm.BuildingRepository;
import com.device.inspect.common.repository.firm.CompanyRepository;
import com.device.inspect.common.repository.firm.RoomRepository;
import com.device.inspect.common.repository.firm.StoreyRepository;
import com.device.inspect.common.restful.RestResponse;
import com.device.inspect.common.restful.charater.RestUser;
import com.device.inspect.common.restful.device.RestDevice;
import com.device.inspect.common.restful.device.RestDeviceType;
import com.device.inspect.common.util.transefer.UserRoleDifferent;
import com.device.inspect.controller.request.DeviceTypeRequest;
import com.device.inspect.controller.request.InspectTypeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import java.io.File;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/1.
 */
@RestController
@RequestMapping(value = "/api/rest/operate")
public class OperateController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private BuildingRepository buildingRepository;

    @Autowired
    private StoreyRepository storeyRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private DeviceTypeRepository deviceTypeRepository;

    @Autowired
    private InspectTypeRepository inspectTypeRepository;

    @Autowired
    private DeviceTypeInspectRepository deviceTypeInspectRepository;

    @Autowired
    private RoleAuthorityRepository roleAuthorityRepository;

    @Autowired
    private DeviceFloorRepository deviceFloorRepository;

    @Autowired
    private DeviceInspectRepository deviceInspectRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private DeviceFileRepository deviceFileRepository;

    private User judgeByPrincipal(Principal principal){
        if (null == principal||null==principal.getName())
            throw new UsernameNotFoundException("You are not login!");
        User user = userRepository.findByName(principal.getName());
        if (null==user)
            throw new UsernameNotFoundException("user not found!");
        return user;
    }

    /**
     * type 0 是添加  1是修改
     * @param deviceId
     * @param map
     * @return
     */
    @RequestMapping(value = "/device/floor/{deviceId}")
    public RestResponse operateDeviceFloor(@PathVariable Integer deviceId,@RequestParam Map<String,String> map){
        Device device = deviceRepository.findOne(deviceId);
        if (null == device||null==map.get("type"))
            return new RestResponse("设备信息出错！",1005,null);
        DeviceFloor deviceFloor = new DeviceFloor();

        if (map.get("type").equals("1")){
            if (map.get("floorId")==null)
                return new RestResponse("设备层信息出错！",1005,null);
            deviceFloor = deviceFloorRepository.findOne(Integer.valueOf(map.get("floorId")));
            if (null == deviceFloor)
                return new RestResponse("设备层信息出错！",1005,null);
            if (null!=map.get("floorNum"))
                deviceFloor.setFloorNum(Integer.valueOf(map.get("floorNum")));

        }else {
            deviceFloor.setDevice(device);
            deviceFloor.setFloorNum(null == map.get("floorNum") ? null : Integer.valueOf(map.get("floorNum")));
        }
//        deviceFloor.setScientist(map.get("scientist"));
        deviceFloor.setName(map.get("name"));
//        deviceFloor.setEmail(map.get("email"));
//        deviceFloor.setMobile(map.get("mobile"));
        deviceFloor.setProductNum(map.get("productNum")==null?null:Integer.valueOf(map.get("productNum")));
        deviceFloorRepository.save(deviceFloor);

        return new RestResponse(new RestDevice(device));

    }

    /**
     * 修改设备基本信息
     * @param deviceId
     * @param map
     * @return
     */
    @RequestMapping(value = "/device/{deviceId}")
    public RestResponse operateDevice(@PathVariable Integer deviceId,@RequestParam Map<String,String> map){
        Device device = deviceRepository.findOne(deviceId);
        if (null == device)
            return new RestResponse("设备信息出错！",1005,null);
        if (null!=map.get("name"))
            device.setName(map.get("name"));
        if (null!=map.get("creator"))
            device.setCreator(map.get("creator"));
        if (null!=map.get("maintain"))
            device.setMaintain(map.get("maintain"));
        if (null!=map.get("maintainAlterDays"))
            device.setMaintainAlterDays(Integer.valueOf(map.get("maintainAlterDays")));
        if (null!=map.get("model"))
            device.setModel(map.get("model"));
        if (null!=map.get("purchase"))
            device.setPurchase(new Date());
        if (null!=map.get("maintainDate"))
            device.setMaintainDate(new Date());
        if(null!=map.get("managerId")){
            User user = userRepository.findOne(Integer.valueOf(map.get("managerId")));
            if (null!=user){
                device.setManager(user);
            }
        }
        deviceRepository.save(device);

        return new RestResponse(new RestDevice(device));
    }

    /**
     * 获取设备报警参数
     * @param deviceId
     * @return
     */
    @RequestMapping(value = "/get/device/parameter/{deviceId}")
    public RestResponse getDeviceParameter(@PathVariable Integer deviceId){
        Device device = deviceRepository.findOne(deviceId);
        if (null==device)
            return new RestResponse("设备信息出错！",1005,null);
        DeviceTypeRequest deviceTypeRequest = new DeviceTypeRequest();
        List<InspectTypeRequest> list = new ArrayList<InspectTypeRequest>();
        for (DeviceInspect deviceInspect : device.getDeviceInspectList()){
            InspectTypeRequest request = new InspectTypeRequest();
            if (deviceInspect.getStandard()==null)
                request.setChosed(false);
            else {
                request.setChosed(true);
            }

            request.setHighUp(null == deviceInspect.getHighUp() ? null : deviceInspect.getHighUp().toString());
            request.setHighDown(deviceInspect.getHighDown() == null ? null : deviceInspect.getHighDown().toString());
            request.setLowUp(deviceInspect.getLowUp() == null ? null : deviceInspect.getLowUp().toString());
            request.setLowDown(deviceInspect.getLowDown() == null ? null : deviceInspect.getLowDown().toString());
            request.setStandard(deviceInspect.getStandard()==null?null:deviceInspect.getStandard().toString());
            request.setId(deviceInspect.getInspectType().getId());
            request.setName(deviceInspect.getInspectType().getName());

            list.add(request);
        }
        deviceTypeRequest.setId(deviceId);
        deviceTypeRequest.setName(device.getDeviceType().getName());
        deviceTypeRequest.setList(list);
        return new RestResponse(deviceTypeRequest);
    }

    /**
     * 修改单个设备的参数
     */
    @RequestMapping(value = "/device/parameter/{deviceId}")
    public RestResponse operateDeviceData(@PathVariable Integer deviceId,@RequestBody DeviceTypeRequest request){
        Device device = deviceRepository.findOne(deviceId);
        if (null == device)
            return new RestResponse("设备信息出错！",1005,null);
        if (null!=request.getList()&&request.getList().size()>0){
            for (InspectTypeRequest inspectTypeRequest:request.getList()){
                DeviceInspect deviceInspect = deviceInspectRepository.
                        findByInspectTypeIdAndDeviceId(inspectTypeRequest.getId(), deviceId);
                if (null!=inspectTypeRequest.getStandard())
                    deviceInspect.setStandard(Float.valueOf(inspectTypeRequest.getStandard()));
                if (null!=inspectTypeRequest.getHighDown())
                    deviceInspect.setHighDown(Float.valueOf(inspectTypeRequest.getHighDown()));
                if (null!=inspectTypeRequest.getHighUp())
                    deviceInspect.setHighUp(Float.valueOf(inspectTypeRequest.getHighUp()));
                if (null!=inspectTypeRequest.getLowUp())
                    deviceInspect.setLowUp(Float.valueOf(inspectTypeRequest.getLowUp()));
                if (null!=inspectTypeRequest.getLowDown())
                    deviceInspect.setLowDown(Float.valueOf(inspectTypeRequest.getLowDown()));

                deviceInspectRepository.save(deviceInspect);
            }
        }
        return new RestResponse(new RestDevice(device));
    }


    /**
     * 创建新用户
     * @param
     * @param map
     * @return
     */
    @RequestMapping(value = "/create/user")
    public RestResponse createNewUser(Principal principal,@RequestParam Map<String,String> map){
        User user = judgeByPrincipal(principal);
        if (null == user)
            return new RestResponse("用户信息错误！",1005,null);
        User child = new User();
//        RoleAuthority roleAuthority = roleAuthorityRepository.findOne(user.getRole().getRoleAuthority().getChild());

        List<RoleAuthority> roleAuthorityList = new ArrayList<RoleAuthority>();
        if (null!=user.getRoles())
            for (Role role:user.getRoles()){
                roleAuthorityList.addAll(roleAuthorityRepository.findByParent(role.getRoleAuthority().getId()));
            }
        if (UserRoleDifferent.userFirmManagerConfirm(user))
            if (null==map.get("role")||"".equals(map.get("role")))
                return new RestResponse("必须选定添加员工角色！",1005,null);
        if (null==roleAuthorityList)
            return new RestResponse("权限不足，无法添加！",1005,null);

        if (null==map.get("companyId"))
            return new RestResponse("公司信息错误！",1005,null);

        if (null==map.get("name"))
            return new RestResponse("登录名不能为空！",1005,null);
        User judge = userRepository.findByName(map.get("name"));
        if (judge!=null)
            return new RestResponse("登录名已存在！",1005,null);
        Company company = new Company();
        company = companyRepository.findOne(Integer.valueOf(map.get("companyId")));
        if (UserRoleDifferent.userServiceWorkerConfirm(user))
            if (company.getManager()!=null)
                return new RestResponse("该公司已有管理员账号！",1005,null);

        child.setCompany(company);
        child.setCreateDate(new Date());
        child.setName(map.get("name"));
        child.setPassword(null==map.get("password")?"123":map.get("password"));
        child.setUserName(map.get("userName"));
        child.setDepartment(map.get("department"));
        child.setJobNum(map.get("jobNum"));
        child.setJob(map.get("job"));
        userRepository.save(child);
        if (roleAuthorityList.size()==1){
            Role role = new Role();
            role.setAuthority(roleAuthorityList.get(0).getName());
            role.setRoleAuthority(roleAuthorityList.get(0));
            role.setUser(child);
            roleRepository.save(role);
        }else {
            if (UserRoleDifferent.userFirmManagerConfirm(user)){
                String[] roles = map.get("role").split(",");
                if (null!=roles&&roles.length>0){
                    for (String roleName:roles){
                        RoleAuthority roleAuthority = roleAuthorityRepository.findByName(roleName);
                        if (null!=roleAuthority){
                            Role role = new Role();
                            role.setAuthority(roleAuthorityList.get(0).getName());
                            role.setRoleAuthority(roleAuthorityList.get(0));
                            role.setUser(child);
                            roleRepository.save(role);
                        }
                    }
                }
            }
        }
        return new RestResponse("创建成功！",null);
    }

    /**
     * 修改用户信息
     * @param
     * @param param
     * @return
     */
    @RequestMapping(value = "/update/user",method = RequestMethod.POST)
    public RestResponse updateUserMessage(Principal principal,@RequestParam Map<String,String> param){
        User user = judgeByPrincipal(principal);
        if (null == user)
            return new RestResponse("用户信息错误！",1005,null);
        if (null!=param.get("userName"))
            user.setUserName(param.get("userName"));
        if (null!=param.get("department"))
            user.setDepartment(param.get("department"));
        if (null!=param.get("jobNum"))
            user.setJobNum(param.get("jobNum"));
        if (null!=param.get("job"))
            user.setJob(param.get("job"));
        if (null!=param.get("password"))
            user.setPassword(param.get("password"));
        if (null!=param.get("mobile"))
            user.setMobile(param.get("mobile"));
        if (null!=param.get("telephone"))
            user.setTelephone(param.get("telephone"));
        if (null!=param.get("email"))
            user.setEmail(param.get("email"));
        userRepository.save(user);
        return new RestResponse(new RestUser(user));
    }

    @RequestMapping(value = "/deviceType")
    public RestResponse operateDeviceType(Principal principal,@RequestBody DeviceTypeRequest deviceTypeReq){
        User user = judgeByPrincipal(principal);
        if (null == user)
            return new RestResponse("手机号出错！", null);
        DeviceType deviceType = new DeviceType();
        List<DeviceTypeInspect> deviceTypeInspects = new ArrayList<DeviceTypeInspect>();

        if (UserRoleDifferent.userFirmManagerConfirm(user)) {
            if (null != deviceTypeReq.getId()) {
                deviceType = deviceTypeRepository.findOne(deviceTypeReq.getId());
                if (null==deviceType)
                    return new RestResponse("当前设备不存在！",1005,null);
                deviceType.setName(deviceTypeReq.getName());
                deviceTypeRepository.save(deviceType);
                deviceTypeInspects = deviceType.getDeviceTypeInspectList();
                if (null != deviceTypeReq && deviceTypeReq.getList().size() > 0) {
                    for (InspectTypeRequest inspectTypeRequest : deviceTypeReq.getList()) {
                        if (inspectTypeRequest.isChosed()){
                            InspectType inspectType = inspectTypeRepository.findOne(inspectTypeRequest.getId());
                            DeviceTypeInspect deviceTypeInspect = deviceTypeInspectRepository.
                                    findByDeviceTypeIdAndInspectTypeId(deviceType.getId(), inspectType.getId());
                            if (null != deviceTypeInspect) {
                                deviceTypeInspect.setDeviceType(deviceType);
                                deviceTypeInspect.setInspectType(inspectType);
                                deviceTypeInspect.setHighDown(Float.valueOf(inspectTypeRequest.getHighDown()));
                                deviceTypeInspect.setHighUp(Float.valueOf(inspectTypeRequest.getHighUp()));
                                deviceTypeInspect.setStandard(Float.valueOf(inspectTypeRequest.getStandard()));
                                deviceTypeInspect.setLowDown(Float.valueOf(inspectTypeRequest.getLowDown()));
                                deviceTypeInspect.setLowUp(Float.valueOf(inspectTypeRequest.getLowUp()));
                                deviceTypeInspect.setLowAlter(null == inspectTypeRequest.getLowAlter() ? 10 : inspectTypeRequest.getLowAlter());
                                deviceTypeInspectRepository.save(deviceTypeInspect);
                            }
                        }
                    }
                }
            } else {
                deviceType.setName(deviceTypeReq.getName());
                deviceTypeRepository.save(deviceType);
                if (null != deviceTypeReq && deviceTypeReq.getList().size() > 0) {
                    for (InspectTypeRequest inspectTypeRequest : deviceTypeReq.getList()) {
                        if (inspectTypeRequest.isChosed()){
                            InspectType inspectType = inspectTypeRepository.findOne(inspectTypeRequest.getId());
                            DeviceTypeInspect deviceTypeInspect = new DeviceTypeInspect();
                            deviceTypeInspect.setDeviceType(deviceType);
                            deviceTypeInspect.setInspectType(inspectType);
                            deviceTypeInspect.setHighDown(Float.valueOf(inspectTypeRequest.getHighDown()));
                            deviceTypeInspect.setHighUp(Float.valueOf(inspectTypeRequest.getHighUp()));
                            deviceTypeInspect.setStandard(Float.valueOf(inspectTypeRequest.getStandard()));
                            deviceTypeInspect.setLowDown(Float.valueOf(inspectTypeRequest.getLowDown()));
                            deviceTypeInspect.setLowUp(Float.valueOf(inspectTypeRequest.getLowUp()));
                            deviceTypeInspect.setLowAlter(null == inspectTypeRequest.getLowAlter() ? 10 : inspectTypeRequest.getLowAlter());
                            deviceTypeInspectRepository.save(deviceTypeInspect);
                        }

                    }
                }
            }
        } else {
            return new RestResponse("权限不足！",1005,null);
        }
        return new RestResponse(new RestDeviceType(deviceType));
    }

    @RequestMapping(value = "/delete/file/{fileId}")
    public RestResponse deleteFileFromDevice(@PathVariable Integer fileId,@RequestParam Integer deviceId){
        Files files = fileRepository.findOne(fileId);
        if (null==files)
            return new RestResponse("该文件不存在！",1005,null);
        DeviceFile deviceFile = deviceFileRepository.findByDeviceIdAndFileId(deviceId,fileId);
        if (null == deviceFile)
            return new RestResponse("该设备无此文件！",1005,null);
        deviceFileRepository.delete(deviceFile);
        fileRepository.delete(files);
        return new RestResponse("删除成功！",null);
    }

    @RequestMapping(value = "/manager/device/{deviceId}")
    public RestResponse deleteDeviceById(Principal principal,@PathVariable Integer deviceId,@RequestParam Integer enable){
        User user = judgeByPrincipal(principal);
        Device device = deviceRepository.findOne(deviceId);
        if (null==device)
            return new RestResponse("该设备不存在！",1005,null);
        if (device.getManager().getId().equals(user.getId())||(UserRoleDifferent.userFirmManagerConfirm(user)&&
        device.getManager().getCompany().getId().equals(user.getCompany().getId()))) {
            device.setEnable(enable);
            deviceRepository.save(device);
            return new RestResponse("删除成功！",null);
        }
        return new RestResponse("权限不足！",1005,null);
    }

    @RequestMapping(value = "/manager/room/{roomId}")
    public RestResponse deleteRoomById(Principal principal,@PathVariable Integer roomId,@RequestParam Integer enable){
        User user = judgeByPrincipal(principal);
        Room room = roomRepository.findOne(roomId);
        if (null == room)
            return new RestResponse("该房间不存在！",1005,null);
        if (UserRoleDifferent.userFirmManagerConfirm(user)&&user.getCompany().getId().equals(room.getFloor().getBuild().getCompany().getId())){
            List<Device> list = deviceRepository.findByRoomId(roomId);
            if (null!=list)
                for (Device device:list){
                    device.setEnable(enable);
                }
            deviceRepository.save(list);
            room.setEnable(enable);
            roomRepository.save(room);
            return new RestResponse("删除成功！",null);
        }
        return new RestResponse("权限不足！",1005,null);
    }

    @RequestMapping(value = "/manager/floor/{floorId}")
    public RestResponse deleteFloorById(Principal principal,@PathVariable Integer floorId,@RequestParam Integer enable){
        User user = judgeByPrincipal(principal);
        Storey floor = storeyRepository.findOne(floorId);
        if (null==floor)
            return new RestResponse("该楼层不存在！",1005,null);
        if (UserRoleDifferent.userFirmManagerConfirm(user)&&user.getCompany().getId().equals(floor.getBuild().getCompany().getId())){
            List<Room> roomList = roomRepository.findByFloorId(floorId);
            if (null!=roomList) {
                for (Room room : roomList) {
                    List<Device> deviceListlist = deviceRepository.findByRoomId(room.getId());
                    if (null != deviceListlist)
                        for (Device device : deviceListlist) {
                            device.setEnable(enable);
                        }
                    deviceRepository.save(deviceListlist);
                    room.setEnable(enable);
                }
                roomRepository.save(roomList);
            }
            floor.setEnable(enable);
            storeyRepository.save(floor);
            return new RestResponse("删除成功！",null);
        }
        return new RestResponse("权限不足！",1005,null);
    }

    @RequestMapping(value = "/manager/build/{buildId}")
    public RestResponse deleteBuildById(Principal principal,@PathVariable Integer buildId,@RequestParam Integer enable){
        User user = judgeByPrincipal(principal);
        Building building = buildingRepository.findOne(buildId);
        if (null == building)
            return new RestResponse("该建筑不存在！",1005,null);
        if(UserRoleDifferent.userFirmManagerConfirm(user)&&user.getCompany().getId().equals(building.getCompany().getId())){
            List<Storey> floorList = storeyRepository.findByBuildId(buildId);
            if (null!=floorList){
                for (Storey floor:floorList){
                    List<Room> roomList = roomRepository.findByFloorId(floor.getId());
                    if (null!=roomList) {
                        for (Room room : roomList) {
                            List<Device> deviceListlist = deviceRepository.findByRoomId(room.getId());
                            if (null != deviceListlist)
                                for (Device device : deviceListlist) {
                                    device.setEnable(enable);
                                }
                            deviceRepository.save(deviceListlist);
                            room.setEnable(enable);
                        }
                        roomRepository.save(roomList);
                    }
                    floor.setEnable(enable);
                }
                storeyRepository.save(floorList);
            }
            building.setEnable(enable);
            buildingRepository.save(building);
            return new RestResponse("删除成功！",null);
        }
        return new RestResponse("权限不足！",1005,null);
    }

    @RequestMapping(value = "/manager/device/type/{typeId}")
    public RestResponse managerOperateDeviceTypeById(Principal principal,@PathVariable Integer typeId,@RequestParam Integer enable){
        User user = judgeByPrincipal(principal);
        DeviceType deviceType = deviceTypeRepository.findOne(typeId);
        if (null==deviceType.getCompany()){
            if (UserRoleDifferent.userStartWithService(user)){
                deviceType.setEnable(enable);
                deviceTypeRepository.save(deviceType);
                return new RestResponse("删除成功！",null);
            }else {
                return new RestResponse("权限不足！",1005,null);
            }
        }else {
            if (UserRoleDifferent.userFirmManagerConfirm(user)&&
                    user.getCompany().getId().equals(deviceType.getCompany().getId())){
                deviceType.setEnable(enable);
                deviceTypeRepository.save(deviceType);
                return new RestResponse("删除成功！",null);
            }else {
                return new RestResponse("权限不足！",1005,null);
            }
        }
    }


}
