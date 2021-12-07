package com.zzeng.wj.service;

import com.zzeng.wj.dao.AdminRoleMenuDao;
import com.zzeng.wj.entity.AdminRoleMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AdminRoleMenuService {
    @Autowired
    AdminRoleMenuDao adminRoleMenuDao;

    /**
     * 通过角色id查找角色所有菜单
     * */
    public List<AdminRoleMenu> findAllByRid(int rid) {
        return adminRoleMenuDao.findAllByRid(rid);
    }

    /**
     * 通过所有角色id查找角色所有菜单
     * */
    public List<AdminRoleMenu> findAllByRid(List<Integer> rids) {
        return adminRoleMenuDao.findAllByRidIn(rids);
    }

    /**
     * 保存角色的菜单
     * */
    public void save(AdminRoleMenu rm) {
        adminRoleMenuDao.save(rm);
    }

    /**
     * 修改角色菜单
     * */
    @Modifying
    @Transactional
    public void updateRoleMenu(int rid, Map<String, List<Integer>> menusIds) {
        adminRoleMenuDao.deleteAllByRid(rid);
        List<AdminRoleMenu> rms = new ArrayList<>();    //用于存储角色菜单
        for (Integer mid : menusIds.get("menusIds")) {
            AdminRoleMenu rm = new AdminRoleMenu();
            rm.setMid(mid);
            rm.setRid(rid);
            rms.add(rm);
        }
        adminRoleMenuDao.saveAll(rms);
    }
}
