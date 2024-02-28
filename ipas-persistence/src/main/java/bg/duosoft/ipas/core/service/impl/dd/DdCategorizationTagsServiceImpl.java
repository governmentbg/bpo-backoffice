package bg.duosoft.ipas.core.service.impl.dd;

import bg.duosoft.ipas.core.mapper.dd.DdCategorizationTagsMapper;
import bg.duosoft.ipas.core.model.decisiondesktop.CDdCategorizationTags;
import bg.duosoft.ipas.core.service.dd.DdCategorizationTagsService;
import bg.duosoft.ipas.persistence.model.entity.ext.core.CfDdCategorizationTags;
import bg.duosoft.ipas.persistence.repository.entity.ext.CfDdCategorizationTagsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created by IntelliJ IDEA.
 * User: Raya
 * Date: 01.06.2021
 * Time: 16:31
 */
@Service
public class DdCategorizationTagsServiceImpl implements DdCategorizationTagsService {

    @Autowired
    private CfDdCategorizationTagsRepository categorizationTagsRepository;

    @Autowired
    private DdCategorizationTagsMapper mapper;


    @Override
    public CDdCategorizationTags getCategorizationTags(String fileType, String userdocType) {
        Optional<CfDdCategorizationTags> ddConfig;
        if(userdocType == null){
            ddConfig = categorizationTagsRepository.findByFileTypeAndEmptyUserdocType(fileType);
        } else {
            ddConfig = categorizationTagsRepository.findByFileTypeAndUserdocType(fileType, userdocType);
        }
        if(ddConfig.isPresent()){
            return mapper.toCore(ddConfig.get());
        } else {
            return null;
        }
    }
}
