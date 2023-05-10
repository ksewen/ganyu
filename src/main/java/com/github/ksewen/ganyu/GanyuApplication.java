package com.github.ksewen.ganyu;

import com.github.ksewen.ganyu.configuration.properties.JwtProperties;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
@OpenAPIDefinition(info = @Info(title = "Ganyu API", version = "0.0.1"))
@SecurityScheme(name = "jwt-auth", scheme = "bearer", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
public class GanyuApplication {

	public static void main(String[] args) {
		SpringApplication.run(GanyuApplication.class, args);
	}

}
