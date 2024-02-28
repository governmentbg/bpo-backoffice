package com.duosoft.ipas.webmodel;

import lombok.*;

import java.util.Objects;

@Builder
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ExpiringMarkNotificationDocument {
    private String filingNumber;
    private Integer documentId;
    private String errorMessage;
    private Status status;

    public enum Status {
        SUCCESS, ERROR, ALREADY_GENERATED
    }

    public boolean doesDocumentExist(){
        return Objects.nonNull(this.documentId);
    }
}
