package com.github.ksewen.ganyu.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ksewen
 * @date 10.05.2023 12:24
 */
@RestController
@RequestMapping("/admin")
@SecurityRequirement(name = "jwt-auth")
public class AdminController {
}
