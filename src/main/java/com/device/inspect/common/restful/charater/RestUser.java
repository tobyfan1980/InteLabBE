package com.device.inspect.common.restful.charater;

import com.device.inspect.common.model.charater.User;
import com.device.inspect.common.model.firm.Company;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by Administrator on 2016/7/12.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestUser {

    private Integer id;
    private String name;
    private String password;
    private String userName;
    private String mobile;
    private String telephone;
    private String headIcon;
    private Date createDate;
    private String gender;
    private String email;
    private String department;
    private String job;
    private String jobNum;
    private RestRole role;
    private String companyName;

    public RestUser(@NotNull User user){
        this.id = user.getId();
        this.name = user.getName();
        this.password = user.getPassword();
        this.userName = user.getUserName();
        this.mobile = user.getMobile();
        this.telephone = user.getTelephone();
        this.headIcon = user.getHeadIcon();
        this.createDate = user.getCreateDate();
        this.gender = user.getGender();
        this.email = user.getEmail();
        this.department = user.getDepartment();
        this.job = user.getJob();
        this.jobNum = user.getJobNum();
        if (null!=user.getRoles()&&user.getRoles().size()>0)
            this.role = new RestRole(user.getRoles().get(0));
        this.companyName = user.getCompany().getName();
    }



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getHeadIcon() {
        return headIcon;
    }

    public void setHeadIcon(String headIcon) {
        this.headIcon = headIcon;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getJobNum() {
        return jobNum;
    }

    public void setJobNum(String jobNum) {
        this.jobNum = jobNum;
    }

    public RestRole getRole() {
        return role;
    }

    public void setRole(RestRole role) {
        this.role = role;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
