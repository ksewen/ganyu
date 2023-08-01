package com.github.ksewen.ganyu.mapper;

import com.github.ksewen.ganyu.domain.Token;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author ksewen
 * @date 11.05.2023 08:29
 */
@Repository
public interface TokenMapper extends JpaRepository<Token, Long>, JpaSpecificationExecutor<Token> {

    Optional<Token> findByToken(String token);

}