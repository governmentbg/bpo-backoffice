package bg.duosoft.ipas.core.service.impl.userdoc;

import bg.duosoft.ipas.core.mapper.userdoc.UserdocPanelMapper;
import bg.duosoft.ipas.core.mapper.userdoc.userdoc_type.UserdocToUiPanelMapper;
import bg.duosoft.ipas.core.model.userdoc.CUserdocPanel;
import bg.duosoft.ipas.core.model.userdoc.CUserdocType;
import bg.duosoft.ipas.core.service.userdoc.UserdocPanelService;
import bg.duosoft.ipas.persistence.model.entity.ext.core.CfUserdocTypeToUiPanel;
import bg.duosoft.ipas.persistence.model.entity.ext.core.CfUserdocTypeToUiPanelPK;
import bg.duosoft.ipas.persistence.model.entity.ext.core.CfUserdocUiPanel;
import bg.duosoft.ipas.persistence.repository.entity.ext.CfUserdocTypeToUiPanelRepository;
import bg.duosoft.ipas.persistence.repository.entity.nomenclature.CfUserdocUiPanelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserdocPanelServiceImpl implements UserdocPanelService {

    @Autowired
    private CfUserdocUiPanelRepository userdocUiPanelRepository;

    @Autowired
    private CfUserdocTypeToUiPanelRepository userdocTypeToUiPanelRepository;

    @Autowired
    private UserdocToUiPanelMapper userdocToUiPanelMapper;

    @Autowired
    private UserdocPanelMapper userdocPanelMapper;


    @Override
    public Map<String, String> getUserdocPanelsSelectOptions(CUserdocType cUserdocType) {
        List<CfUserdocUiPanel> panels = userdocUiPanelRepository.findAll();
        if (CollectionUtils.isEmpty(panels)) {
            return null;
        }

        return createAndSortPanelsMap(panels);
    }

    @Override
    public CUserdocPanel findUserdocPanelByPanelAndUserdocType(String id, String userdocType) {
        if (StringUtils.isEmpty(id) || StringUtils.isEmpty(userdocType)) {
            return null;
        }

        CfUserdocTypeToUiPanel cfUserdocToUiPanel = userdocTypeToUiPanelRepository.findById(new CfUserdocTypeToUiPanelPK(userdocType,id)).orElse(null);
        if (Objects.isNull(cfUserdocToUiPanel))
            return null;

        return userdocToUiPanelMapper.toCore(cfUserdocToUiPanel);
    }

    @Override
    public CUserdocPanel findPanelById(String id) {
        if (StringUtils.isEmpty(id)) {
            return null;
        }

        CfUserdocUiPanel cfUserdocUiPanel = userdocUiPanelRepository.findById(id).orElse(null);
        if (Objects.isNull(cfUserdocUiPanel)) {
            return null;
        }

        return userdocPanelMapper.toCore(cfUserdocUiPanel);
    }

    private Map<String, String> createAndSortPanelsMap(List<CfUserdocUiPanel> panels) {
        panels.sort(Comparator.comparing(CfUserdocUiPanel::getName));
        Map<String, String> panelsMap = panels.stream().collect(Collectors.toMap(CfUserdocUiPanel::getPanel, CfUserdocUiPanel::getName));

        return panelsMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }
}
