package com.zzeng.wj.service;

import com.zzeng.wj.dao.AdminRoleDao;
import com.zzeng.wj.entity.AdminMenu;
import com.zzeng.wj.entity.AdminPermission;
import com.zzeng.wj.entity.AdminRole;
import com.zzeng.wj.entity.AdminUserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminRoleService {
    @Autowired
    AdminRoleDao adminRoleDao;
    @Autowired
    UserService userService;
    @Autowired
    AdminUserRoleService adminUserRoleService;
    @Autowired
    AdminPermissionService adminPermissionService;
    @Autowired
    AdminRolePermissionService adminRolePermissionService;
    @Autowired
    AdminMenuService adminMenuService;

    /**
     * 查找角色的所有权限和菜单
     * @return
     */
    public List<AdminRole> listWithPermsAndMenus() {
        List<AdminRole> roles = adminRoleDao.findAll(); //列出所有角色
        List<AdminPermission> perms;                    //权限
        List<AdminMenu> menus;                          //菜单
        for (AdminRole role : roles) {
            perms = adminPermissionService.listPermsByRoleId(role.getId());
            menus = adminMenuService.getMenusByRoleId(role.getId());
            role.setPerms(perms);
            role.setMenus(menus);
        }
        return roles;
    }

    /**
     * 返回所有角色
     * @return
     */
    public List<AdminRole> findAll() {
        return adminRoleDao.findAll();
    }

    /**
     * 添加或更新角色
     * @param adminRole
     */
    public void addOrUpdate(AdminRole adminRole) {
        adminRoleDao.save(adminRole);
    }

    /**
     * 通过用户名查找所有的角色
     * @param username
     * @return
     */
    public List<AdminRole> listRolesByUser(String username) {
        int uid = userService.findByUsername(username).getId();
        List<Integer> rids = adminUserRoleService.listAllByUid(uid)
                .stream().map(AdminUserRole::getRid).collect(Collectors.toList());
        return adminRoleDao.findAllById(rids);
    }

    /**
     * 改变角色状态
     * @param role
     * @return
     */
    public AdminRole updateRoleStatus(AdminRole role) {
        AdminRole roleInDB = adminRoleDao.findById(role.getId());
        roleInDB.setEnabled(role.isEnabled());
        return adminRoleDao.save(roleInDB);
    }

    /**
     * 编辑角色
     * @param role
     */
    public void editRole(@RequestBody AdminRole role) {
        adminRoleDao.save(role);
        adminRolePermissionService.savePermChanges(role.getId(), role.getPerms());
    }
}
