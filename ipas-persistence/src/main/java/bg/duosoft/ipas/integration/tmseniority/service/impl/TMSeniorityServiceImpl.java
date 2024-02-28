package bg.duosoft.ipas.integration.tmseniority.service.impl;


import bg.duosoft.ipas.core.model.mark.CMarkSeniority;
import bg.duosoft.ipas.integration.tmseniority.mapper.MarkSeniorityMapper;
import bg.duosoft.ipas.integration.tmseniority.model.TransactionType;
import bg.duosoft.ipas.integration.tmseniority.service.TMSeniorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

/**
 * Created by Raya
 * 07.03.2019
 */
@Service
public class TMSeniorityServiceImpl implements TMSeniorityService {

    @Autowired
    @Qualifier("noAuthRestTemplate")
    private RestTemplate restTemplate;

    @Value("${tmseniority.rest.service.url}")
    private String seniorityServiceUrl;

    @Autowired
    private MarkSeniorityMapper markSeniorityMapper;


    private String getUrl(Object... args){
        StringBuilder urlBuilder = new StringBuilder(seniorityServiceUrl);
        urlBuilder.append("?markIdappli=").append(args[0]);
        if(args.length>1) {
            urlBuilder.append("&isInternational=").append(args[1]);
        }
        return urlBuilder.toString();
    }

    @Override
    public List<CMarkSeniority> getMarkSeniorities(String fileType, Integer fileSeries, Integer fileNumber, boolean isInternational) {

        StringBuilder idappliBuilder = new StringBuilder();
        Formatter fileNbrFormatter = new Formatter();
        fileNbrFormatter.format("%06d", fileNumber);
        idappliBuilder.append(fileSeries);
        idappliBuilder.append(fileNbrFormatter.toString());
        idappliBuilder.append(fileType);

        return getMarkSeniorities(idappliBuilder.toString(), isInternational);
    }

    @Override
    public List<CMarkSeniority> getMarkSeniorities(String fileType, Integer fileSeries, Integer fileNumber) {
        return getMarkSeniorities(fileType, fileSeries, fileNumber, false);
    }

    @Override
    public List<CMarkSeniority> getMarkSeniorities(String idappli, boolean isInternational) {
        List<CMarkSeniority> markSeniorities = new ArrayList<>();
        TransactionType seniorityTransaction = restTemplate.getForObject(getUrl(idappli, isInternational), TransactionType.class);
        if(seniorityTransaction != null && seniorityTransaction.getTradeMarkTransactionBody() != null && seniorityTransaction.getTradeMarkTransactionBody().size()>0 &&
            seniorityTransaction.getTradeMarkTransactionBody().get(0) != null &&
            seniorityTransaction.getTradeMarkTransactionBody().get(0).getTransactionContentDetails() != null &&
            seniorityTransaction.getTradeMarkTransactionBody().get(0).getTransactionContentDetails().getTransactionData().getSeniorityDetails()!= null &&
            seniorityTransaction.getTradeMarkTransactionBody().get(0).getTransactionContentDetails().getTransactionData().getSeniorityDetails().getSeniority() != null &&
            seniorityTransaction.getTradeMarkTransactionBody().get(0).getTransactionContentDetails().getTransactionData().getSeniorityDetails().getSeniority().size()>0){

            seniorityTransaction.getTradeMarkTransactionBody().get(0).getTransactionContentDetails().getTransactionData().getSeniorityDetails().getSeniority().stream().forEach(
                s-> markSeniorities.add(markSeniorityMapper.seniorityTypeToCMarkSeniority(s))
            );
        }

        return markSeniorities;
    }

    @Override
    public List<CMarkSeniority> getMarkSeniorities(String idappli) {
        return getMarkSeniorities(idappli, false);
    }
}
