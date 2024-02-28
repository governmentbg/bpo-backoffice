package bg.duosoft.ipas.util.userdoc;

import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.userdoc.*;
import bg.duosoft.ipas.enums.UserdocExtraDataTypeCode;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

public class UserdocExtraDataUtils {


    public static List<CUserdocExtraDataType> selectAllUserdocExtraDataTypes(CUserdoc userdoc) {
        if (Objects.nonNull(userdoc)) {
            List<CUserdocPanel> panels = userdoc.getUserdocType().getPanels();
            if (!CollectionUtils.isEmpty(panels)) {
                Set<CUserdocExtraDataType> result = panels.stream()
                        .map(CUserdocPanel::getExtraDataTypes)
                        .filter(types -> !CollectionUtils.isEmpty(types))
                        .flatMap(Collection::stream)
                        .collect(Collectors.toSet());
                if (!result.isEmpty()) {
                    return new ArrayList<>(result);
                }
            }
        }
        return null;
    }

    public static CUserdocExtraData selectExtraDataProperty(List<CUserdocExtraData> list, UserdocExtraDataTypeCode type) {
        if (CollectionUtils.isEmpty(list) || Objects.isNull(type)) {
            return null;
        }
        return list.stream()
                .filter(d -> d.getType().getCode().equals(type.name()))
                .findFirst()
                .orElse(null);
    }


    public static List<CUserdocExtraDataType> selectTypes(CUserdoc userdoc, String panel) {
        if (Objects.nonNull(userdoc) && StringUtils.hasText(panel)) {
            List<CUserdocPanel> panels = userdoc.getUserdocType().getPanels();
            if (!CollectionUtils.isEmpty(panels)) {
                CUserdocPanel userdocPanel = panels.stream().filter(cUserdocPanel -> cUserdocPanel.getPanel().equalsIgnoreCase(panel)).findFirst().orElse(null);
                if (Objects.nonNull(userdocPanel)) {
                    return userdocPanel.getExtraDataTypes();
                }
            }
        }
        return null;
    }

    public static CUserdocExtraDataType selectType(List<CUserdocExtraDataType> types, String type) {
        if (CollectionUtils.isEmpty(types) || StringUtils.isEmpty(type)) {
            return null;
        }
        return types.stream()
                .filter(m -> m.getCode().equalsIgnoreCase(type))
                .findFirst()
                .orElse(null);
    }

    public static Date selectDate(String type, List<CUserdocExtraData> userdocExtraData) {
        if (StringUtils.isEmpty(type) || CollectionUtils.isEmpty(userdocExtraData)) {
            return null;
        }

        CUserdocExtraData cUserdocExtraData = userdocExtraData.stream().filter(u -> u.getType().getCode().equalsIgnoreCase(type)).findFirst().orElse(null);
        if (Objects.isNull(cUserdocExtraData)) {
            return null;
        }

        return cUserdocExtraData.getDateValue();
    }


    public static String selectText(String type, List<CUserdocExtraData> userdocExtraData) {
        if (StringUtils.isEmpty(type) || CollectionUtils.isEmpty(userdocExtraData)) {
            return null;
        }

        CUserdocExtraData cUserdocExtraData = userdocExtraData.stream().filter(u -> u.getType().getCode().equalsIgnoreCase(type)).findFirst().orElse(null);
        if (Objects.isNull(cUserdocExtraData)) {
            return null;
        }

        return cUserdocExtraData.getTextValue();
    }


    public static Boolean selectBoolean(String type, List<CUserdocExtraData> userdocExtraData) {
        if (StringUtils.isEmpty(type) || CollectionUtils.isEmpty(userdocExtraData)) {
            return null;
        }

        CUserdocExtraData cUserdocExtraData = userdocExtraData.stream().filter(u -> u.getType().getCode().equalsIgnoreCase(type)).findFirst().orElse(null);
        if (Objects.isNull(cUserdocExtraData)) {
            return null;
        }

        return cUserdocExtraData.getBooleanValue();
    }


    public static Integer selectNumber(String type, List<CUserdocExtraData> userdocExtraData) {
        if (StringUtils.isEmpty(type) || CollectionUtils.isEmpty(userdocExtraData)) {
            return null;
        }

        CUserdocExtraData cUserdocExtraData = userdocExtraData.stream().filter(u -> u.getType().getCode().equalsIgnoreCase(type)).findFirst().orElse(null);
        if (Objects.isNull(cUserdocExtraData)) {
            return null;
        }

        return cUserdocExtraData.getNumberValue();
    }

    public static CUserdocExtraData buildExtraDataObject(CDocumentId documentId, String type, CUserdocExtraDataValue value, CUserdocExtraDataType typeObject) {
        CUserdocExtraData data = new CUserdocExtraData();
        data.setDocumentId(documentId);
        data.setType(typeObject);
        if (typeObject.getIsText()) {
            data.setTextValue(value.getTextValue());
        } else if (typeObject.getIsNumber()) {
            data.setNumberValue(value.getNumberValue());
        } else if (typeObject.getIsBoolean()) {
            data.setBooleanValue(value.getBooleanValue());
        } else if (typeObject.getIsDate()) {
            data.setDateValue(value.getDateValue());
        } else {
            throw new RuntimeException("Invalid data value type! " + type);
        }
        return data;
    }

    public static void setUserdocExtraDataProperty(CUserdoc userdoc, String type, CUserdocExtraDataValue value) {
        if (Objects.isNull(userdoc.getUserdocExtraData())) {
            userdoc.setUserdocExtraData(new ArrayList<>());
        }
        List<CUserdocExtraData> userdocExtraData = userdoc.getUserdocExtraData();
        CUserdocExtraDataType typeObject = selectType(selectAllUserdocExtraDataTypes(userdoc), type);
        if (Objects.nonNull(typeObject)) {
            CUserdocExtraData newData = buildExtraDataObject(userdoc.getDocumentId(), type, value, typeObject);
            userdocExtraData.removeIf(u -> u.getType().getCode().equalsIgnoreCase(newData.getType().getCode()));
            userdocExtraData.add(newData);
        }
    }

}
