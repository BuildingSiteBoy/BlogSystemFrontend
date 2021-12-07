package com.zzeng.wj.dao;

import com.zzeng.wj.entity.AdminRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRoleDao extends JpaRepository<AdminRole, Integer> {
    AdminRole findById(int id);
}
