package com.duosoft.ipas.webmodel;

import bg.duosoft.ipas.util.security.SecurityUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ProgressBar {

    private boolean inProgress;
    private boolean interrupt;
    private boolean endSuccessful;
    private Date startAt;
    private int progress;
    private String message;
    private String username;

    public void start() {
        this.inProgress = true;
        this.startAt = new Date();
        this.progress = 1;
        this.message = null;
        this.username = SecurityUtils.getLoggedUsername();
        this.interrupt = false;
        this.endSuccessful = true;
    }

    public void stop(String reason) {
        this.inProgress = false;
        this.interrupt = true;
        this.endSuccessful = false;
        this.message = reason;
    }

    public void successful() {
        this.inProgress = false;
        this.progress = 100;
        this.endSuccessful = true;
        this.interrupt = false;
    }

}
