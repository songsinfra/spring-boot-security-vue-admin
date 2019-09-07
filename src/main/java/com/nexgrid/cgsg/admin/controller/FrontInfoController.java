package com.nexgrid.cgsg.admin.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/front")
public class FrontInfoController {

    @RequestMapping("/constant")
    public String getInfo() {
        return "info";
    }
}
