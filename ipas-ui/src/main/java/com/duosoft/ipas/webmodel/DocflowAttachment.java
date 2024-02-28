package com.duosoft.ipas.webmodel;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

/**
 * User: Georgi
 * Date: 23.10.2020 г.
 * Time: 18:40
 */
@Data
@AllArgsConstructor
public class DocflowAttachment {
    private UUID uuid;
    private String fileName;
    private String description;
    private Integer databaseId;
}
