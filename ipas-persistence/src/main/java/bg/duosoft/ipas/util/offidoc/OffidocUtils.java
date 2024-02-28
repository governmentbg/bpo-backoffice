package bg.duosoft.ipas.util.offidoc;

import bg.duosoft.abdocs.model.DocFile;
import bg.duosoft.abdocs.model.Document;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.file.CProcessId;
import bg.duosoft.ipas.core.model.offidoc.COffidoc;
import bg.duosoft.ipas.core.model.offidoc.COffidocId;
import bg.duosoft.ipas.core.model.offidoc.COffidocTypeTemplate;
import bg.duosoft.ipas.core.model.process.CProcess;
import bg.duosoft.ipas.core.model.process.CProcessEvent;
import bg.duosoft.ipas.core.model.process.CProcessParentData;
import bg.duosoft.ipas.core.service.nomenclature.OffidocTypeTemplateService;
import bg.duosoft.ipas.core.service.offidoc.OffidocService;
import bg.duosoft.ipas.core.service.process.ProcessService;
import bg.duosoft.ipas.enums.FileType;
import bg.duosoft.ipas.enums.OffidocParentObjectType;
import bg.duosoft.ipas.enums.OffidocTypeTemplateConfig;
import bg.duosoft.ipas.enums.ProcessEventType;
import bg.duosoft.ipas.services.core.IpasServiceException;
import bg.duosoft.ipas.util.DefaultValue;
import org.springframework.data.util.Pair;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class OffidocUtils {

    public static String selectOffidocFileName(OffidocTypeTemplateService offidocTypeTemplateService, COffidoc offidoc, String template) {
        String offidocTypeTemplateFileNameConfig = offidocTypeTemplateService.findNameFileConfigById(offidoc.getOffidocType().getOffidocType(), template);

        if (OffidocTypeTemplateConfig.TEMPLATE.code().equals(offidocTypeTemplateFileNameConfig)) {
            return template;
        }
        if (OffidocTypeTemplateConfig.REGISTRATION_NBR.code().equals(offidocTypeTemplateFileNameConfig)) {
            Integer regNumber = offidoc.getOffidocParentData().getTopProcessFileData().getRegNumber();
            if (Objects.nonNull(regNumber)) {
                return regNumber.toString();
            }
        }
        return null;
    }

    public static String selectOffidocFileName(COffidoc offidoc, COffidocTypeTemplate template) {

        if (OffidocTypeTemplateConfig.TEMPLATE == template.getNameFileConfig()) {
            return template.getNameWFile();
        }
        if (OffidocTypeTemplateConfig.REGISTRATION_NBR == template.getNameFileConfig()) {
            Integer regNumber = offidoc.getOffidocParentData().getTopProcessFileData().getRegNumber();
            if (Objects.nonNull(regNumber)) {
                return regNumber.toString();
            }
        }
        return null;
    }

    public static void printAllNotPrintedOffidocs(CProcessId cProcessId, ProcessService processService, OffidocService offidocService) throws IpasServiceException {
        CProcess cProcess = processService.selectProcess(cProcessId, true);
        List<CProcessEvent> processEventList = cProcess.getProcessEventList();
        if (!CollectionUtils.isEmpty(processEventList)) {
            List<CProcessEvent> collect = processEventList.stream()
                    .filter(cProcessEvent -> cProcessEvent.getEventType().equals(ProcessEventType.ACTION.name()))
                    .filter(cProcessEvent -> cProcessEvent.getEventAction().getGeneratedOffidoc() != null)
                    .filter(cProcessEvent -> cProcessEvent.getEventAction().getGeneratedOffidoc().getPrintDate() == null)
                    .collect(Collectors.toList());

            if (!CollectionUtils.isEmpty(collect)) {
                for (CProcessEvent cProcessEvent : collect) {
                    COffidocId offidocId = cProcessEvent.getEventAction().getGeneratedOffidoc().getOffidocId();
                    offidocService.printOffidoc(offidocId);
                }
            }
        }
    }

    public static COffidocId convertStringToOffidocId(String concatenatedId) {
        if (StringUtils.isEmpty(concatenatedId))
            return null;

        String[] split = concatenatedId.split(DefaultValue.IPAS_OBJECT_ID_SEPARATOR);
        if (split.length != 3)
            throw new RuntimeException("Wrong office document id !");

        COffidocId cOffidocId = new COffidocId();
        cOffidocId.setOffidocOrigin(split[0]);
        cOffidocId.setOffidocSeries(Integer.valueOf(split[1]));
        cOffidocId.setOffidocNbr(Integer.valueOf(split[2]));
        return cOffidocId;
    }

    public static String covertOffidocIdToString(COffidocId id) {
        return id.getOffidocOrigin() + "/" + id.getOffidocSeries() + "/" + id.getOffidocNbr();
    }

    public static boolean hasGeneratedFileForFileName(String fileName, Document document) {
        DocFile docFile = selectGeneratedFileForFileName(fileName, document);
        return !Objects.isNull(docFile);
    }

    public static DocFile selectGeneratedFileForFileName(String fileName, Document document) {
        if (Objects.isNull(document))
            return null;

        List<DocFile> docFiles = document.getDocFiles();
        if (CollectionUtils.isEmpty(docFiles))
            return null;

        return docFiles.stream()
                .filter(file -> {
                    String name = file.getName();
                    int lastIndex = name.lastIndexOf('.');
                    if (lastIndex == -1) {
                        return name.equalsIgnoreCase(getFileNameWithoutExtension(fileName));
                    } else {
                        String substring = name.substring(0, lastIndex);
                        return substring.equalsIgnoreCase(getFileNameWithoutExtension(fileName));
                    }
                })
                .findFirst()
                .orElse(null);
    }

    private static String getFileNameWithoutExtension(String fileName) {
        String fileNameWithoutExtension;
        int i = fileName.lastIndexOf(".");
        if (i != -1) {
            fileNameWithoutExtension = fileName.substring(0, i);
        } else {
            fileNameWithoutExtension = fileName;
        }
        return fileNameWithoutExtension;
    }

    public static boolean isOffidoc(String filingNumber) {
        return DefaultValue.OFFIDOC_FILING_NUMBER_PARTS == filingNumber.split(DefaultValue.IPAS_OBJECT_ID_SEPARATOR).length;
    }

    public static CProcessParentData selectClosestMainParentObject(CProcessParentData offidocParentData) {
        if (Objects.isNull(offidocParentData))
            return null;

        if (offidocParentData.isFileProcess()) {
            return offidocParentData;
        } else if (offidocParentData.isUserdocProcess()) {
            return offidocParentData;
        } else {
            return selectClosestMainParentObject(offidocParentData.getParent());
        }
    }

    public static Pair<String, String> selectClosestMainParentObjectData(CProcessParentData offidocParentData) {
        CProcessParentData closestObject = OffidocUtils.selectClosestMainParentObject(offidocParentData);
        if (Objects.nonNull(closestObject)) {
            if (closestObject.isFileProcess()) {
                return Pair.of(closestObject.getFileId().createFilingNumber(), OffidocParentObjectType.IPOBJECT.name());
            } else if (closestObject.isUserdocProcess()) {
                return Pair.of(closestObject.getUserdocId().createFilingNumber(), OffidocParentObjectType.USERDOC.name());
            }
        }

        return null;
    }
}
