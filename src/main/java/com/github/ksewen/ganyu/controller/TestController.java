package com.github.ksewen.ganyu.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ksewen
 * @date 09.05.2023 18:28
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/test")
    public String security(){
        return "Hello World!";
    }

}
