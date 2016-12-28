package com.device.inspect.common.model.device;

import com.device.inspect.common.model.firm.Company;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Administrator on 2016/7/8.
 */
@Entity
@Table(name = "device_type")
public class DeviceType {

    private Integer id;
    private String name;
    private String logo;
    private Company company;
    private Integer enable;
    private List<DeviceTypeInspect> deviceTypeInspectList;

    @Id
    @GeneratedValue()
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

    @Column(name = "logo_url")
    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    @OneToMany(mappedBy = "deviceType")
    public List<DeviceTypeInspect> getDeviceTypeInspectList() {
        return deviceTypeInspectList;
    }

    public void setDeviceTypeInspectList(List<DeviceTypeInspect> deviceTypeInspectList) {
        this.deviceTypeInspectList = deviceTypeInspectList;
    }

    @ManyToOne()
    @JoinColumn(name = "company_id")
    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Integer getEnable() {
        return enable;
    }

    public void setEnable(Integer enable) {
        this.enable = enable;
    }
}
