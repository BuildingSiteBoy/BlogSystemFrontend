package com.zzeng.wj.dao;

import com.zzeng.wj.entity.AdminRolePermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdminRolePermissionDao extends JpaRepository<AdminRolePermission, Integer> {
    List<AdminRolePermission> findAllByRid(int rid);
    List<AdminRolePermission> findAllByRidIn(List<Integer> rids);
    void deleteAllByRid(int rid);
}
