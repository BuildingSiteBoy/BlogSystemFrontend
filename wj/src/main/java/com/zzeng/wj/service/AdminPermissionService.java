package com.zzeng.wj.service;

import com.zzeng.wj.dao.AdminPermissionDao;
import com.zzeng.wj.dao.AdminRolePermissionDao;
import com.zzeng.wj.entity.AdminPermission;
import com.zzeng.wj.entity.AdminRole;
import com.zzeng.wj.entity.AdminRolePermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AdminPermissionService {
    @Autowired
    AdminPermissionDao adminPermissionDao;
    @Autowired
    AdminUserRoleService adminUserRoleService;
    @Autowired
    AdminRoleService adminRoleService;
    @Autowired
    AdminRolePermissionService adminRolePermissionService;
    @Autowired
    AdminRolePermissionDao adminRolePermissionDao;
    @Autowired
    UserService userService;

    /**
     * 列出所有权限
     * */
    public List<AdminPermission> list() {
        return adminPermissionDao.findAll();
    }

    /**
     * 确定客户端请求时是否需要权限
     * @param requestAPI 一个特定的API，由客户端请求
     * @return 在数据库中找到该API时返回true
     */
    public boolean needFilter(String requestAPI) {
        List<AdminPermission> ps = adminPermissionDao.findAll();
        for (AdminPermission p : ps) {
            //匹配前缀
            if (requestAPI.startsWith(p.getUrl())) {
                return true;
            }
        }

        return false;
    }

    /**
     * 通过角色id查找权限
     */
    public List<AdminPermission> listPermsByRoleId(int rid) {
        List<Integer> pids = adminRolePermissionService.findAllByRid(rid)
                .stream().map(AdminRolePermission::getPid).collect(Collectors.toList());
        return adminPermissionDao.findAllById(pids);
    }

    /**
     * 通过用户名查找权限路径
     */
    public Set<String> listPermissionURLsByUser(String username) {
        List<Integer> rids = adminRoleService.listRolesByUser(username)
                .stream().map(AdminRole::getId).collect(Collectors.toList());

        List<Integer> pids = adminRolePermissionDao.findAllByRidIn(rids)
                .stream().map(AdminRolePermission::getPid).collect(Collectors.toList());

        List<AdminPermission> perms = adminPermissionDao.findAllById(pids);

        Set<String> URLs = perms.stream().map(AdminPermission::getUrl).collect(Collectors.toSet());

        return URLs;
    }
}
