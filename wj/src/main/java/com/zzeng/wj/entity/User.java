package com.zzeng.wj.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 注解：
 * @Entity  表示实体类
 * @Table(name = "user") 表示对应的表名时user
 * @JsonIgnoreProperties 简化对数据库的操作
 */
@Entity
@Table(name = "user")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotEmpty(message = "用户名不能为空")
    private String username;

    private String password;

    private String name;

    private String salt;

    private String phone;

    /**
     * A email address can bu null, but should be correct if exists.
     * */
    @Email(message = "请输入正确的邮箱")
    private String email;

    //user status
    private boolean enabled;

    /**
     * Transient property for storing role owned by current user.
     * */
    @Transient
    private List<AdminRole> roles;
}
