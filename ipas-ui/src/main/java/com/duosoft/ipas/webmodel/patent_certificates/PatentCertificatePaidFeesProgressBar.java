package com.duosoft.ipas.webmodel.patent_certificates;

import com.duosoft.ipas.webmodel.ProgressBar;
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
public class PatentCertificatePaidFeesProgressBar extends ProgressBar {
    private String patentCertificateMainObject;
    private String lastPatentCertificateMainObject;
    private List<Integer> paidFeesForGeneration;
    private List<PatentCertificatePaidFeesProgressResult> results;

    public List<PatentCertificatePaidFeesProgressResult> selectSuccessful() {
        if (CollectionUtils.isEmpty(this.results)) {
            return null;
        }

        return this.results.stream()
                .filter(PatentCertificatePaidFeesProgressResult::isGeneratedSuccessfully)
                .collect(Collectors.toList());
    }

    public List<PatentCertificatePaidFeesProgressResult> selectUnsuccessful() {
        if (CollectionUtils.isEmpty(this.results)) {
            return null;
        }

        return this.results.stream()
                .filter(d -> !d.isGeneratedSuccessfully())
                .collect(Collectors.toList());
    }

    public List<PatentCertificatePaidFeesProgressResult> getResults() {
        if (Objects.isNull(this.results)) {
            this.results = new ArrayList<>();
        }
        return this.results;
    }

    @Override
    public void stop(String reason) {
        super.stop(reason);
        this.paidFeesForGeneration = null;
        this.patentCertificateMainObject = null;
    }

    public void start(List<Integer> paidFees, String patentCertificateMainObject) {
        super.start();
        this.paidFeesForGeneration = paidFees;
        this.patentCertificateMainObject = patentCertificateMainObject;
        this.lastPatentCertificateMainObject = patentCertificateMainObject;
        this.results = null;
    }
}
