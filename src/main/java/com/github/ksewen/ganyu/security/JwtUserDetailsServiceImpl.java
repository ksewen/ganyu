package com.github.ksewen.ganyu.security;

import com.github.ksewen.ganyu.domain.Role;
import com.github.ksewen.ganyu.domain.User;
import com.github.ksewen.ganyu.model.AuthModel;
import com.github.ksewen.ganyu.service.RoleService;
import com.github.ksewen.ganyu.service.UserService;
import com.github.ksewen.ganyu.util.BeanMapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userService.findFirstByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        } else {
            //FIXME: access database twice
            AuthModel authModel = BeanMapperUtil.createAndCopyProperties(user, AuthModel.class);
            List<Role> userRoles = this.roleService.findByUserId(authModel.getId());
            authModel.setRoles(userRoles);
            return JwtUserFactory.create(authModel);
        }
    }
}
