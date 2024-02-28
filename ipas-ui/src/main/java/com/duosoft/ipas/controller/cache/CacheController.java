package com.duosoft.ipas.controller.cache;

import bg.duosoft.ipas.core.service.cache.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * User: ggeorgiev
 * Date: 6.6.2019 г.
 * Time: 14:53
 */
@Controller
public class CacheController {
    @Autowired
    private CacheService cacheService;


    @RequestMapping("/clearCache")
    @ResponseBody
    public String clearCache() {
        cacheService.clearCache();
        return "cache cleared...";
    }
}
