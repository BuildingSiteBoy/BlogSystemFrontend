package com.zzeng.wj.service;

import com.zzeng.wj.dao.AdminRolePermissionDao;
import com.zzeng.wj.entity.AdminPermission;
import com.zzeng.wj.entity.AdminRolePermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminRolePermissionService {
    @Autowired
    AdminRolePermissionDao adminRolePermissionDao;

    /**
     * 通过角色id查找角色的所有权限
     * */
    List<AdminRolePermission> findAllByRid(int rid) {
        return adminRolePermissionDao.findAllByRid(rid);
    }

    /**
     * 更改角色的权限
     * */
    @Transactional      //标示类中所有方法都进行事务处理
    public void savePermChanges(int rid, List<AdminPermission> perms) {
        adminRolePermissionDao.deleteAllByRid(rid);
        List<AdminRolePermission> rps = new ArrayList<>();  //用于存储角色权限
        perms.forEach(p -> {
            AdminRolePermission rp = new AdminRolePermission();
            rp.setRid(rid);
            rp.setPid(p.getId());
            rps.add(rp);
        });
        adminRolePermissionDao.saveAll(rps);
    }
}
