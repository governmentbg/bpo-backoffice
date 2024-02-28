package bg.duosoft.ipas.core.service.impl.nomenclature;

import bg.duosoft.ipas.core.mapper.miscellaneous.SettlementMapper;
import bg.duosoft.ipas.core.model.miscellaneous.CSettlement;
import bg.duosoft.ipas.core.service.nomenclature.SettlementService;
import bg.duosoft.ipas.persistence.model.entity.ext.core.CfSettlement;
import bg.duosoft.ipas.persistence.repository.entity.nomenclature.CfSettlementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class SettlementServiceImpl implements SettlementService {

    @Autowired
    private CfSettlementRepository cfSettlementRepository;

    @Autowired
    private SettlementMapper settlementMapper;


    @Override
    public List<CSettlement> selectByNameLikeOrSettlementNameLike(String name) {
        if (StringUtils.isEmpty(name))
            return null;

        String namelike = "%" + name.trim() + "%";
        List<CfSettlement> cfSettlements = cfSettlementRepository.selectByNameLikeOrSettlementNameLike(namelike);
        if (CollectionUtils.isEmpty(cfSettlements))
            return null;

        return settlementMapper.toCoreList(cfSettlements);
    }

    @Override
    public List<CSettlement> selectBySettlementName(String name) {
        if (StringUtils.isEmpty(name))
            return null;

        List<CfSettlement> cfSettlements = cfSettlementRepository.findAllBySettlementname(name);
        if (CollectionUtils.isEmpty(cfSettlements))
            return null;

        return settlementMapper.toCoreList(cfSettlements);
    }

    @Override
    public List<CSettlement> selectByNameOrSettlementName(String name) {
        if (StringUtils.isEmpty(name))
            return null;

        List<CfSettlement> cfSettlements = cfSettlementRepository.selectByNameOrSettlementName(name);
        if (CollectionUtils.isEmpty(cfSettlements))
            return null;

        return settlementMapper.toCoreList(cfSettlements);
    }

    @Override
    public CSettlement selectById(Integer id) {
        if (Objects.isNull(id))
            return null;

        CfSettlement cfSettlement = cfSettlementRepository.findById(id).orElse(null);
        if (Objects.isNull(cfSettlement))
            return null;

        return settlementMapper.toCore(cfSettlement);
    }

    @Override
    public CSettlement selectBySettlementMunicipalityAndDistrict(String settlement, String municipality, String district) {
        CfSettlement cfSettlement = cfSettlementRepository.findByNameAndMunicipalitycode_NameAndDistrictcode_Name(settlement, municipality, district);
        if (Objects.isNull(cfSettlement))
            return null;

        return settlementMapper.toCore(cfSettlement);
    }
}