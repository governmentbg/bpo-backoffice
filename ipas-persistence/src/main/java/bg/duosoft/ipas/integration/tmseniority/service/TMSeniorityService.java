package bg.duosoft.ipas.integration.tmseniority.service;

import bg.duosoft.ipas.core.model.mark.CMarkSeniority;

import java.util.List;

/**
 * Created by Raya
 * 07.03.2019
 */
public interface TMSeniorityService {

    List<CMarkSeniority> getMarkSeniorities(String fileType, Integer fileSeries, Integer fileNumber, boolean isInternational);
    List<CMarkSeniority> getMarkSeniorities(String fileType, Integer fileSeries, Integer fileNumber);
    List<CMarkSeniority> getMarkSeniorities(String idappli, boolean isInternational);
    List<CMarkSeniority> getMarkSeniorities(String idappli);

}
