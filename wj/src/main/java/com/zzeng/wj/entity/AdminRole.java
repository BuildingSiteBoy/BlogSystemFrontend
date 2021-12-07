package com.zzeng.wj.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.util.List;

/**
 * 角色类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "admin_role")
@ToString
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class AdminRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    private String name;

    @Column(name = "name_zh")
    private String nameZh;

    private boolean enabled;

    /**
     * Transient property for storing permissions owned by current role.
     * 用于存储当前角色拥有的权限的临时属性。
     * */
    @Transient
    private List<AdminPermission> perms;

    /**
     * Transient property for storing menus owned by current role.
     * 用于存储当前角色拥有的菜单的临时属性。
     * */
    @Transient
    private List<AdminMenu> menus;
}
