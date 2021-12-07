package com.zzeng.wj.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

/**
 * 菜单类
 */
@Data
@Entity
@Table(name = "admin_menu")
@ToString
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class AdminMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    private String path;
    private String name;
    private String nameZh;
    private String iconCls;
    private String component;
    private int parentId;

    /**
     * Transient property for storing children menus.
     * 用于存储子菜单的瞬态(临时)属性。
     * */
    @Transient
    private List<AdminMenu> children;
}
