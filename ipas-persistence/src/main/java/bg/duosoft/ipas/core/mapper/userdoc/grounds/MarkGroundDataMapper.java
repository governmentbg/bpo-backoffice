package bg.duosoft.ipas.core.mapper.userdoc.grounds;


import bg.duosoft.ipas.core.mapper.ApplicationSubTypesMapper;
import bg.duosoft.ipas.core.mapper.miscellaneous.GeoCountryMapper;
import bg.duosoft.ipas.core.model.userdoc.grounds.CMarkGroundData;
import bg.duosoft.ipas.core.service.userdoc.UserdocRootGroundService;
import bg.duosoft.ipas.enums.FileType;
import bg.duosoft.ipas.persistence.model.entity.ext.ExtCfSignType;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfApplicationSubtype;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfApplicationSubtypePK;
import bg.duosoft.ipas.persistence.model.entity.userdoc.grounds.IpMarkGroundData;
import bg.duosoft.ipas.util.DefaultValue;
import bg.duosoft.ipas.util.date.DateUtils;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Mapper(componentModel = "spring", uses = {
        MarkGroundTypeMapper.class,
        GeoCountryMapper.class,
        LegalGroundCategoriesMapper.class,
        ApplicationSubTypesMapper.class,
        GroundNiceClassesMapper.class})
public abstract class MarkGroundDataMapper {

    @Autowired
    private UserdocRootGroundService userdocRootGroundService;

    @Mapping(target = "niceClassesInd", expression = "java(bg.duosoft.ipas.core.mapper.MapperHelper.getTextAsBoolean(markGroundData.getNiceClassesInd()))")
    @Mapping(target = "markImportedInd", expression = "java(bg.duosoft.ipas.core.mapper.MapperHelper.getTextAsBoolean(markGroundData.getMarkImportedInd()))")
    @Mapping(target = "nameData",  expression = "java(loadFileContent ? markGroundData.getNameData() : null)")
    @Mapping(target = "userdocGroundsNiceClasses",  source = "userdocGroundsNiceClasses")
    @Mapping(target = "geographicalIndTyp",  source = "geographicalIndTyp")
    @Mapping(target = "markGroundType",  source = "markGroundType")
    @Mapping(target = "legalGroundCategory",  source = "legalGroundCategory")
    @Mapping(target = "registrationCountry",  source = "registrationCountry")
    @BeanMapping(ignoreByDefault = true)
    public abstract CMarkGroundData toCore(IpMarkGroundData markGroundData, @Context Boolean loadFileContent);


    @InheritInverseConfiguration
    @Mapping(target = "niceClassesInd", expression = "java(bg.duosoft.ipas.core.mapper.MapperHelper.getBooleanAsText(markGroundData.getNiceClassesInd()))")
    @Mapping(target = "markImportedInd", expression = "java(bg.duosoft.ipas.core.mapper.MapperHelper.getBooleanAsText(markGroundData.getMarkImportedInd()))")
    @Mapping(target = "nameData",  source = "nameData")
    @BeanMapping(ignoreByDefault = true)
    public abstract IpMarkGroundData toEntity(CMarkGroundData markGroundData);


    @AfterMapping
    protected void afterToCore(@MappingTarget CMarkGroundData target, IpMarkGroundData source, @Context Boolean loadFileContent) {

        if (Objects.nonNull(source)){
            String signMarkCode=null;
            if (Objects.nonNull(target.getMarkImportedInd()) && target.getMarkImportedInd()){
                String[] filingNumberAsArray = source.getFilingNumber().split("/");
                List<Object[]> groundMarkRelatedData=userdocRootGroundService.findGroundMarkRelatedData(filingNumberAsArray[0],filingNumberAsArray[1], Integer.valueOf(filingNumberAsArray[2]),Integer.valueOf(filingNumberAsArray[3]));
                Object[] importedNationalMarkGroundData =  groundMarkRelatedData.get(0);
                Date filingDate = importedNationalMarkGroundData[1] != null ? DateUtils.parseDateAsString(importedNationalMarkGroundData[1].toString()) : null;
                String registrationNbr = importedNationalMarkGroundData[2] != null ? importedNationalMarkGroundData[2].toString() : null;
                Date registrationDate = importedNationalMarkGroundData[3] != null ? DateUtils.parseDateAsString(importedNationalMarkGroundData[3].toString()) : null;
                String nameText  = importedNationalMarkGroundData[5]!=null?importedNationalMarkGroundData[5].toString():null;
                if (importedNationalMarkGroundData[4]!=null){
                    signMarkCode= importedNationalMarkGroundData[4].toString();
                }
                target.initMarkRelatedData(registrationNbr,registrationDate,nameText,source.getFilingNumber(),filingDate,signMarkCode);
            }else{
                if(Objects.nonNull(source.getMarkSignTyp())){
                    signMarkCode = source.getMarkSignTyp().getSignType();
                }
                target.initMarkRelatedData(source.getRegistrationNbr(),source.getRegistrationDate(),
                        source.getNameText(),source.getFilingNumber(),source.getFilingDate(),signMarkCode);
            }
        }
    }

    @AfterMapping
    protected void afterToEntity(@MappingTarget IpMarkGroundData target, CMarkGroundData source) {
            if (Objects.nonNull(source)){
                if (Objects.nonNull(source.getMarkImportedInd()) && source.getMarkImportedInd()){
                    target.setFilingNumber(source.getFilingNumber());
                }else{
                    target.setFilingNumber(source.getFilingNumber());
                    target.setFilingDate(source.getFilingDate());
                    target.setRegistrationNbr(source.getRegistrationNbr());
                    target.setRegistrationDate(source.getRegistrationDate());
                    target.setNameText(source.getNameText());
                    if(Objects.nonNull(source.getMarkSignTyp())){
                        ExtCfSignType markSignTyp =new ExtCfSignType();
                        markSignTyp.setSignType(source.getMarkSignTyp().code());
                        markSignTyp.setSignTypeName(source.getMarkSignTyp().description());
                        target.setMarkSignTyp(markSignTyp);
                    }
                }
                if (Objects.nonNull(source.getGeographicalIndTyp())){
                    CfApplicationSubtype geographicalIndTyp = new CfApplicationSubtype();
                    CfApplicationSubtypePK cfApplicationSubtypePK=new CfApplicationSubtypePK();
                    cfApplicationSubtypePK.setApplTyp(source.getGeographicalIndTyp().getApplicationType());
                    cfApplicationSubtypePK.setApplSubtyp(source.getGeographicalIndTyp().getApplicationSubType());
                    geographicalIndTyp.setPk(cfApplicationSubtypePK);
                    target.setGeographicalIndTyp(geographicalIndTyp);
                }
            }
    }

}
