package com.zzeng.wj.dto;

import com.zzeng.wj.dto.base.OutputConverter;
import com.zzeng.wj.entity.AdminRole;
import com.zzeng.wj.entity.User;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class UserDTO implements OutputConverter<UserDTO, User> {
    private int id;
    private String username;
    private String name;
    private String phone;
    private String email;
    private boolean enabled;
    private List<AdminRole> roles;
}
