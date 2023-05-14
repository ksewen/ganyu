package com.github.ksewen.ganyu.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author ksewen
 * @date 09.05.2023 21:01
 */

@Setter
@Getter
@ToString
@ConfigurationProperties(prefix = "application.security.jwt")
public class JwtProperties {

    private String secretKey;

    private long expiration;

    private long refreshExpiration;

    private String readHeader;

    private boolean debug = false;

}
