package com.duosoft.ipas.session.reception.model;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReceptionEmailSession {

    private String sessionObjectName;
    private Integer userId;
    private String username;
    private String userFullName;
    private int emailId;
    private String emailSubject;
    private Date date;

}
