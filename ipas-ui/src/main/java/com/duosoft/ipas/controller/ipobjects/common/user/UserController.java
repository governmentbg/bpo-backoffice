package com.duosoft.ipas.controller.ipobjects.common.user;

import bg.duosoft.ipas.core.model.person.CUser;
import bg.duosoft.ipas.core.service.person.SimpleUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private SimpleUserService userService;

    @GetMapping(value = "/users-autocomplete", produces = "application/json")
    @ResponseBody
    public List<CUser> userAutocomplete(@RequestParam String name,@RequestParam(required = false) Boolean onlyActiveUsers) {
        List<CUser> users = userService.findByUsernameLike(name, Objects.isNull(onlyActiveUsers)?true:onlyActiveUsers);
        if (!CollectionUtils.isEmpty(users)) {
            users.sort(Comparator.comparing(CUser::getUserName));
        }
        return users;
    }

}
