package com.zzeng.wj.dao;

import com.zzeng.wj.entity.AdminMenu;
import com.zzeng.wj.entity.AdminPermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdminPermissionDao extends JpaRepository<AdminPermission, Integer> {
    AdminPermission findById(int id);
}
