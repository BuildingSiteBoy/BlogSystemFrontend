package com.zzeng.wj.service;

import com.zzeng.wj.dao.UserDao;
import com.zzeng.wj.dto.UserDTO;
import com.zzeng.wj.entity.AdminRole;
import com.zzeng.wj.entity.User;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    UserDao userDao;
    @Autowired
    AdminRoleService adminRoleService;
    @Autowired
    AdminUserRoleService adminUserRoleService;

    /**
     * list: 列出所有角色
     * */
    public List<UserDTO> list() {
        List<User> users = userDao.findAll();

        // Find all roles in DB to enable JPA persistence context.
        List<UserDTO> userDTOS = users
                .stream().map(user -> (UserDTO) new UserDTO().convertFrom(user)).collect(Collectors.toList());

        userDTOS.forEach(userDTO -> {
            List<AdminRole> roles = adminRoleService.listRolesByUser(userDTO.getUsername());
            userDTO.setRoles(roles);
        });

        return userDTOS;
    }

    /**
     * 判断该用户是否存在
     * */
    public boolean isExist(String username) {
        User user = findByUsername(username);
        return null != user;
    }

    /**
     * 通过名字获取用户
     * */
    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    /**
     * 通过用户名和密码获取用户
     * */
    public User get(String username, String password) {
        return userDao.getByUsernameAndPassword(username, password);
    }

    /**
     * 添加用户
     * */
    public void add(User user) {
        userDao.save(user);
    }

    /**
     * 注册用户
     * */
    public int register(User user) {
        String username = user.getUsername();
        String name = user.getName();
        String password = user.getPassword();
        String phone = user.getPhone();
        String email = user.getEmail();

        username = HtmlUtils.htmlEscape(username);
        user.setUsername(username);
        name = HtmlUtils.htmlEscape(name);
        user.setName(name);
        phone = HtmlUtils.htmlEscape(phone);
        user.setPhone(phone);
        email = HtmlUtils.htmlEscape(email);
        user.setEmail(email);
        user.setEnabled(true);


        if (username.equals("") || password.equals("")) {
            return 0;
        }

        boolean exist = isExist(username);
        if (exist) {
            return 2;
        }

        //用户名不为空、且存在，默认生产16位盐
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        int times = 2;      //hash算法的迭代次数
        String encodedPassword = new SimpleHash("md5", password, salt, times).toString();

        user.setSalt(salt);
        user.setPassword(encodedPassword);

        userDao.save(user);

        return 1;
    }

    /**
     * 更新用户状态
     * */
    public void updateUserStatus(User user) {
        User userInDB = userDao.findByUsername(user.getUsername());
        userInDB.setEnabled(user.isEnabled());
        userDao.save(userInDB);
    }

    /**
     * 重置密码
     * */
    public User resetPassword(User user) {
        User userInDB = userDao.findByUsername(user.getUsername());
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        int times = 2;
        String encodedPassword = new SimpleHash("md5", "123", salt, times).toString();

        userInDB.setSalt(salt);
        userInDB.setPassword(encodedPassword);

        return userDao.save(userInDB);
    }

    /**
     * 编辑用户
     * */
    public void editUser(User user) {
        User userInDB = userDao.findByUsername(user.getUsername());
        userInDB.setName(user.getName());
        userInDB.setEmail(user.getEmail());
        userDao.save(userInDB);
        adminUserRoleService.saveRoleChanges(userInDB.getId(), user.getRoles());
    }

    /**
     * 根据id删除用户
     * */
    public void deleteById(int id) {
        userDao.deleteById(id);
    }

}
