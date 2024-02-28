package bg.duosoft.ipas.core.service.impl.nomenclature;

import bg.duosoft.ipas.core.mapper.action.ActionTypeMapper;
import bg.duosoft.ipas.core.model.action.CActionType;
import bg.duosoft.ipas.core.service.nomenclature.ActionTypeService;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfActionType;
import bg.duosoft.ipas.persistence.repository.entity.nomenclature.CfActionTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;


@Service
@Transactional
public class ActionTypeServiceImpl implements ActionTypeService {

    @Autowired
    private CfActionTypeRepository cfActionTypeRepository;

    @Autowired
    private ActionTypeMapper actionTypeMapper;

    @Override
    public CActionType findById(String actionTyp) {
        if (StringUtils.isEmpty(actionTyp))
            return null;

        CfActionType cfActionType = cfActionTypeRepository.findById(actionTyp).orElse(null);
        if(Objects.isNull(cfActionType))
            return null;

        return actionTypeMapper.toCore(cfActionType);
    }

    @Override
    public List<CActionType> findActionTypesByProcTypesAndActionName(List<String> procTypes, String actionName) {

        if (!actionName.isEmpty())
            actionName = "%" + actionName + "%";

        List<CfActionType> cfActionTypes = cfActionTypeRepository.findAllByProcTypesAndActionTypeNameContainsOrderByActionTypeName(procTypes, actionName);
        if(Objects.isNull(cfActionTypes) || cfActionTypes.size() == 0)
            return null;

        return actionTypeMapper.toCoreList(cfActionTypes);
    }

    @Override
    public List<CActionType> findActionTypesByFileTypesAndActionName(List<String> fileTypes, String actionName) {

        if (!actionName.isEmpty())
            actionName = "%" + actionName + "%";

        List<CfActionType> cfActionTypes = cfActionTypeRepository
                .findAllByActionTypeNameContainsAndFileTypesOrderByActionTypeName(fileTypes, actionName);
        if(Objects.isNull(cfActionTypes) || cfActionTypes.size() == 0)
            return null;

        return actionTypeMapper.toCoreList(cfActionTypes);
    }

    @Override
    public List<CActionType> findAllByAutomaticActionWcode(Integer automaticWCode) {
        List<CfActionType> allByAutomaticActionWcode = cfActionTypeRepository.findAllByAutomaticActionWcode(automaticWCode);
        return actionTypeMapper.toCoreList(allByAutomaticActionWcode);
    }

    @Override
    public List<CActionType> findAllByActionTypIn(List<String> actionTyp) {
        List<CfActionType> actionTypeList = cfActionTypeRepository.findAllByActionTypInOrderByActionTypeName(actionTyp);
        return actionTypeMapper.toCoreList(actionTypeList);
    }
}