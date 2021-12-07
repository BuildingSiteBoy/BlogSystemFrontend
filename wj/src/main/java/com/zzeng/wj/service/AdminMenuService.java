package com.zzeng.wj.service;

import com.zzeng.wj.dao.AdminMenuDao;
import com.zzeng.wj.entity.AdminMenu;
import com.zzeng.wj.entity.AdminRoleMenu;
import com.zzeng.wj.entity.AdminUserRole;
import com.zzeng.wj.entity.User;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminMenuService {
    @Autowired
    AdminMenuDao adminMenuDao;
    @Autowired
    UserService userService;
    @Autowired
    AdminUserRoleService adminUserRoleService;
    @Autowired
    AdminRoleMenuService adminRoleMenuService;

    /**
     * 通过父结点获取菜单
     * */
    public List<AdminMenu> getAllByParentId(int parentId) {
        return adminMenuDao.findAllByParentId(parentId);
    }

    /**
     * 获取当前用户的菜单
     * */
    public List<AdminMenu> getMenusByCurrentUser() {
        //获取当前用户
        String username = SecurityUtils.getSubject().getPrincipal().toString();
        User user = userService.findByUsername(username);

        //获取当前用户的角色id列表
        List<Integer> rids = adminUserRoleService.listAllByUid(user.getId())
                .stream().map(AdminUserRole::getRid).collect(Collectors.toList());

        //获取上面得到的角色的菜单
        List<Integer> menuIds = adminRoleMenuService.findAllByRid(rids)
                .stream().map(AdminRoleMenu::getMid).collect(Collectors.toList());
        List<AdminMenu> menus = adminMenuDao.findAllById(menuIds).stream().distinct().collect(Collectors.toList());

        //调整菜单结构
        handleMenus(menus);
        return menus;
    }

    /**
     * 通过角色id获取菜单
     * */
    public List<AdminMenu> getMenusByRoleId(int rid) {
        List<Integer> menuIds = adminRoleMenuService.findAllByRid(rid)
                .stream().map(AdminRoleMenu::getMid).collect(Collectors.toList());
        List<AdminMenu> menus = adminMenuDao.findAllById(menuIds);

        handleMenus(menus);
        return menus;
    }

    /**
     * 修改菜单结构
     * 遍历菜单项，根据每一项的id查询该项出的所有的子项，并放进children属性
     * 剔除掉所有子项，只保留第一层的父项。
     * */
    public void handleMenus(List<AdminMenu> menus) {
        /*menus.forEach(m -> {
            List<AdminMenu> children = getAllByParentId(m.getId());
            m.setChildren(children);
        });
        menus.removeIf(m -> m.getParentId() != 0);*/
        for (AdminMenu menu : menus) {
            List<AdminMenu> children = getAllByParentId(menu.getId());
            menu.setChildren(children);
        }

        Iterator<AdminMenu> iterator = menus.iterator();
        while (iterator.hasNext()) {
            AdminMenu menu = iterator.next();
            if (menu.getParentId() != 0) {
                iterator.remove();
            }
        }
    }
}
