package com.github.ksewen.ganyu.configuration.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

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
