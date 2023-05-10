package com.github.ksewen.ganyu.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.github.ksewen.ganyu.domain.UserDomain;
import com.github.ksewen.ganyu.mapper.UserExtMapper;

@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserExtMapper userExtMapper;

    @Override
    public UserDetails loadUserByUsername(String identity) throws UsernameNotFoundException {
        UserDomain user = userExtMapper.loginByIdentity(identity);

        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", identity));
        } else {
            return JwtUserFactory.create(user);
        }
    }
}
