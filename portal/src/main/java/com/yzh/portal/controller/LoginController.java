package com.yzh.portal.controller;

import com.yzh.bean.UserInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 * @author yuanzhihao
 * @since 2022/5/8
 */
@Controller
public class LoginController {
    @GetMapping(value = "/login")
    public String login(HttpServletRequest request, HashMap<String, Object> map) {
        UserInfo userInfo = (UserInfo) request.getSession().getAttribute("userInfo");
        if (userInfo != null) {
            map.put("userInfo", userInfo);
            return "index";
        }
        return "login";
    }

    @PostMapping("/login")
    public String login(UserInfo userInfo, HashMap<String, Object> map, HttpServletRequest request) {
        // 修改sessionId
        request.changeSessionId();
        // 设置session
        request.getSession().setAttribute("userInfo", userInfo);
        map.put("userInfo", userInfo);
        return "index";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return "logout";
    }
}
