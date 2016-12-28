package com.device.inspect.common.model.charater;

import javax.persistence.*;

/**
 * Created by Administrator on 2016/7/7.
 */
@Entity
@Table(name = "role_authority")
public class RoleAuthority {
    private Integer id;
    private String name;
    private Integer child;
    private String roleName;
    private Integer parent;

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

//    @Column(name = "child_id")
//    public Integer getChild() {
//        return child;
//    }
//
//    public void setChild(Integer child) {
//        this.child = child;
//    }

    @Column(name = "role_name")
    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Column(name = "parent_id")
    public Integer getParent() {
        return parent;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
    }
}
