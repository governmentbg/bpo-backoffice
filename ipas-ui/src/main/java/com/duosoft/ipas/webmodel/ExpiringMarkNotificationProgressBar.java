package com.duosoft.ipas.webmodel;

import lombok.*;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpiringMarkNotificationProgressBar extends ProgressBar {

    private List<String> marksForGeneration;
    private List<ExpiringMarkNotificationDocument> expiringMarkNotificationDocuments;


    public List<ExpiringMarkNotificationDocument> selectSuccessful() {
        if (CollectionUtils.isEmpty(this.expiringMarkNotificationDocuments)) {
            return null;
        }

        return this.expiringMarkNotificationDocuments.stream()
                .filter(s -> Objects.nonNull(s.getStatus()))
                .filter(d -> d.getStatus() == ExpiringMarkNotificationDocument.Status.SUCCESS)
                .collect(Collectors.toList());
    }

    public List<ExpiringMarkNotificationDocument> selectUnsuccessful() {
        if (CollectionUtils.isEmpty(this.expiringMarkNotificationDocuments)) {
            return null;
        }

        return this.expiringMarkNotificationDocuments.stream()
                .filter(s -> Objects.nonNull(s.getStatus()))
                .filter(d -> d.getStatus() == ExpiringMarkNotificationDocument.Status.ERROR)
                .collect(Collectors.toList());
    }

    public List<ExpiringMarkNotificationDocument> selectAlreadyGenerated() {
        if (CollectionUtils.isEmpty(this.expiringMarkNotificationDocuments)) {
            return null;
        }

        return this.expiringMarkNotificationDocuments.stream()
                .filter(s -> Objects.nonNull(s.getStatus()))
                .filter(d -> d.getStatus() == ExpiringMarkNotificationDocument.Status.ALREADY_GENERATED)
                .collect(Collectors.toList());
    }

    public List<ExpiringMarkNotificationDocument> getExpiringMarkNotificationDocuments() {
        if (Objects.isNull(this.expiringMarkNotificationDocuments)) {
            this.expiringMarkNotificationDocuments = new ArrayList<>();
        }

        return this.expiringMarkNotificationDocuments;
    }

    @Override
    public void stop(String reason) {
        super.stop(reason);
        this.marksForGeneration = null;
    }

    public void start(List<String> marksForGeneration) {
        super.start();
        this.marksForGeneration = marksForGeneration;
        this.expiringMarkNotificationDocuments = null;
    }
}
