package com.zzeng.wj.filter;

import com.zzeng.wj.service.AdminPermissionService;
import com.zzeng.wj.util.SpringContextUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.PathMatchingFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

@Log4j2
public class URLPathMatchingFilter extends PathMatchingFilter {
    @Autowired
    AdminPermissionService adminPermissionService;

    @Override
    protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        // 放行options请求
        if (HttpMethod.OPTIONS.toString().equals((httpServletRequest).getMethod())) {
            httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
            return true;
        }

        if (null == adminPermissionService) {
            adminPermissionService = SpringContextUtils.getContext().getBean(AdminPermissionService.class);
        }

        String requestAPI = getPathWithinApplication(request);
//        System.out.println("访问接口：" + requestAPI);
        Subject subject = SecurityUtils.getSubject();

        if (!subject.isAuthenticated()) {
            log.info("未登录用户尝试访问需要登录的接口");
//            System.out.println("未登录用户尝试访问需要登录的接口");
            return false;
        }

        // 判断访问接口是否需要过滤（数据库中是否有对应信息）
        boolean needFilter = adminPermissionService.needFilter(requestAPI);
        if (!needFilter) {
//            System.out.println("接口：" + requestAPI + "无需权限");
            return true;
        } else {
            // 判断当前用户是否有相应的权限
//            System.out.println("验证访问权限：" + requestAPI);
            boolean hasPermission = false;
            String username = subject.getPrincipal().toString();
            Set<String> permissionAPIs = adminPermissionService.listPermissionURLsByUser(username);
//            System.out.println(permissionAPIs);
            for (String api : permissionAPIs) {
                //匹配前缀
                if (requestAPI.startsWith(api)) {
                    hasPermission = true;
                    break;
                }
            }

            if (hasPermission) {
                log.trace("用户：" + username + "访问了接口：" + requestAPI);
//                System.out.println("用户：" + username + "访问了接口：" + requestAPI);
                return true;
            } else {
                log.warn("用户：" + username + "访问了没有权限的接口：" + requestAPI);
//                System.out.println("用户：" + username + "访问了没有权限的接口：" + requestAPI);
                return false;
            }
        }
    }
}
