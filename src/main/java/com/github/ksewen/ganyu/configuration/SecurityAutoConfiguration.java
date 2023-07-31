package com.github.ksewen.ganyu.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.github.ksewen.ganyu.configuration.filter.JwtAuthenticationTokenFilter;
import com.github.ksewen.ganyu.configuration.properties.JwtProperties;

import lombok.RequiredArgsConstructor;

/**
 * @author ksewen
 * @date 09.05.2023 23:56
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@ComponentScan("com.github.ksewen.ganyu.security")
public class SecurityAutoConfiguration {

    private final JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    private final JwtProperties jwtProperties;

    private final AuthenticationEntryPoint authenticationEntryPoint;

    private final AccessDeniedHandler accessDeniedHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors().and().csrf().disable().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

                .authorizeHttpRequests().requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/", "/*.html", "/favicon.ico", "/*/*.html", "/*/*.css", "/*/*.js",
                        "/v3/api-docs/**", "/swagger-resources/**", "/webjars/**", "swagger-ui/**", "/error", "/actuator", "/actuator/**")
                .permitAll().requestMatchers("/auth/**").permitAll().anyRequest().authenticated().and()
                .exceptionHandling().authenticationEntryPoint(this.authenticationEntryPoint)
                .accessDeniedHandler(this.accessDeniedHandler);

        // 禁用缓存
        httpSecurity.headers().cacheControl();

        httpSecurity.addFilterBefore(this.jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.debug(this.jwtProperties.isDebug()).ignoring().requestMatchers("/css/**", "/js/**",
                "/img/**", "/lib/**", "/favicon.ico");
    }

}
