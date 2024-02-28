package com.duosoft.ipas.webmodel;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * User: Georgi
 * Date: 2.11.2020 г.
 * Time: 12:39
 */
@Getter
@Setter
public class ReceptionDocflowAttachment extends DocflowAttachment {
    private Integer attSeqId;
    private boolean transferAttachment;

    public ReceptionDocflowAttachment(UUID uuid, String fileName, String description, Integer databaseId, boolean transferAttachment, int attSeqId) {
        super(uuid, fileName, description, databaseId);
        this.transferAttachment = transferAttachment;
        this.attSeqId = attSeqId;
    }

}
