package com.github.ksewen.ganyu.mapper.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import com.github.ksewen.ganyu.domain.User;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

/**
 * @author ksewen
 * @date 13.05.2023 00:27
 */
public class UserSpecification implements Specification<User> {

    private Long id;
    private String username;
    private String nickname;
    private String email;
    private String mobile;
    private boolean isAnd = true;

    public UserSpecification(Long id, String username, String nickname, String email, String mobile, boolean isAnd) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.email = email;
        this.mobile = mobile;
        this.isAnd = isAnd;
    }

    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> list = new ArrayList<>();
        if (this.id != null) {
            list.add(criteriaBuilder.equal(root.get("id").as(Long.class), this.id));
        }
        if (StringUtils.hasLength(this.username)) {
            list.add(criteriaBuilder.equal(root.get("username").as(String.class), this.username));
        }
        if (StringUtils.hasLength(this.nickname)) {
            list.add(criteriaBuilder.equal(root.get("nickname").as(String.class), this.nickname));
        }
        if (StringUtils.hasLength(this.email)) {
            list.add(criteriaBuilder.equal(root.get("email").as(String.class), this.email));
        }
        if (StringUtils.hasLength(this.mobile)) {
            list.add(criteriaBuilder.equal(root.get("mobile").as(String.class), this.mobile));
        }
        Predicate[] array = new Predicate[list.size()];
        Predicate[] predicates = list.toArray(array);
        return isAnd ? criteriaBuilder.and(predicates) : criteriaBuilder.or(predicates);
    }
}
