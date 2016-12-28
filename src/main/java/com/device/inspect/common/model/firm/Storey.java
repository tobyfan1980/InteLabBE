package com.device.inspect.common.model.firm;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/7/7.
 */
@Entity
@Table(name = "floors")
public class Storey {

    private Integer id;
    private Integer num;
    private String name;
    private Building build;
    private Integer deviceNum;
    private Date createDate;
    private String background;
    private Float xpoint;
    private Float ypoint;
    private List<Room> roomList;
//    private Integer alterNum;
    private Integer lowAlert;
    private Integer highAlert;
    private Integer online;
    private Integer offline;
    private Integer total;
    private Float score;
    private Integer enable;

    @Id
    @GeneratedValue()
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "floor_num")
    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToOne()
    @JoinColumn(name = "build_id")
    public Building getBuild() {
        return build;
    }

    public void setBuild(Building build) {
        this.build = build;
    }

    @Column(name = "device_num")
    public Integer getDeviceNum() {
        return deviceNum;
    }

    public void setDeviceNum(Integer deviceNum) {
        this.deviceNum = deviceNum;
    }

    @Column(name = "create_date")
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Column(name = "background_url")
    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    @Column(name = "x_location")
    public Float getXpoint() {
        return xpoint;
    }

    public void setXpoint(Float xpoint) {
        this.xpoint = xpoint;
    }

    @Column(name = "y_location")
    public Float getYpoint() {
        return ypoint;
    }

    public void setYpoint(Float ypoint) {
        this.ypoint = ypoint;
    }

//    @OneToMany(mappedBy = "floor")
    @Transient
    public List<Room> getRoomList() {
        return roomList;
    }

    public void setRoomList(List<Room> roomList) {
        this.roomList = roomList;
    }

    @Column(name = "low_alert_num")
    public Integer getLowAlert() {
        return lowAlert;
    }

    public void setLowAlert(Integer lowAlert) {
        this.lowAlert = lowAlert;
    }
    @Column(name = "high_alert_num")
    public Integer getHighAlert() {
        return highAlert;
    }

    public void setHighAlert(Integer highAlert) {
        this.highAlert = highAlert;
    }
    @Column(name = "online_num")
    public Integer getOnline() {
        return online;
    }

    public void setOnline(Integer online) {
        this.online = online;
    }
    @Column(name = "offline_num")
    public Integer getOffline() {
        return offline;
    }

    public void setOffline(Integer offline) {
        this.offline = offline;
    }
    @Column(name = "total_num")
    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
    @Column(name = "assets_health")
    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    public Integer getEnable() {
        return enable;
    }

    public void setEnable(Integer enable) {
        this.enable = enable;
    }
}
