package com.duosoft.ipas.controller.cron;


import bg.duosoft.cronjob.cron.JobStarter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * User: ggeorgiev
 * Date: 07.04.2021
 * Time: 13:40
 */
@Controller
@Slf4j
@RequestMapping("/cron")
public class CronController {
    @Autowired
    private JobStarter jobStarter;

    @RequestMapping("/start")
    @ResponseBody
    public String startCronByClass(@RequestParam(value = "name")String name){
        try {
            jobStarter.startJob(name);
            return "done...";
        } catch (Exception e) {
            return "error - " + ExceptionUtils.getStackTrace(e);
        }
    }

    @RequestMapping("/reschedule")
    @ResponseBody
    public String reschedule(@RequestParam(value = "name")String name){
        try {
            jobStarter.reinitJob(name);
            return "done...";
        } catch (Exception e) {
            return "error - " + ExceptionUtils.getStackTrace(e);
        }
    }

    @RequestMapping("/unschedule")
    @ResponseBody
    public String unschedule(@RequestParam(value = "name")String name){
        try {
            jobStarter.deleteJob(name);
            return "done...";
        } catch (Exception e) {
            return "error - " + ExceptionUtils.getStackTrace(e);
        }
    }


}
