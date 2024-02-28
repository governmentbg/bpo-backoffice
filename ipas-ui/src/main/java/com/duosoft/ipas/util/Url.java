package com.duosoft.ipas.util;

import bg.duosoft.ipas.enums.security.SecurityRole;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class Url {

    private String url;

    private boolean urlPropertyConfiguration;

    private String text;

    private String shortText;

    private String xhref;

    private List<SecurityRole> roles;

    public Url() {
    }

    public Url(String url, String text) {
        this(url, text, "", "", null);
    }

    public Url(String url, String text, String shortText, String xhref, SecurityRole... role) {
        this.url = url;
        this.text = text;
        this.shortText = shortText;
        this.xhref = xhref;
        this.roles = role == null || role.length == 0 ? null : Arrays.asList(role);
    }

    public Url(String url, boolean isUrlPropertyConfiguration, String text, String shortText, String xhref, SecurityRole... role) {
        this.url = url;
        this.urlPropertyConfiguration = isUrlPropertyConfiguration;
        this.text = text;
        this.shortText = shortText;
        this.xhref = xhref;
        this.roles = role == null || role.length == 0 ? null : Arrays.asList(role);
    }

    public static Url createUrl(String url, String text, String shortText, String xhref, SecurityRole... role) {
        return new Url(url, text, shortText, xhref, role);
    }
}
