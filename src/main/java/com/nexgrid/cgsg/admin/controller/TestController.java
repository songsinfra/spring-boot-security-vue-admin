package com.nexgrid.cgsg.admin.controller;

import com.nexgrid.cgsg.admin.vo.LoginInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RequestMapping("/test")
@Controller
public class TestController {

    @RequestMapping(value = "/loginProcessForAuth", method = RequestMethod.POST)
    @ResponseBody
    public String loginProcessForAuth(HttpServletRequest request, @RequestBody LoginInfo loginInfo, Model model,
                                      BindingResult result) {
        HttpSession session = request.getSession(false);

        System.out.println("aaa");

        return "teste";
    }
}
