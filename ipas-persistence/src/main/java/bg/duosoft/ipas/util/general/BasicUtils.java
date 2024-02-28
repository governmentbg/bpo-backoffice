package bg.duosoft.ipas.util.general;

import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.document.CDocumentSeqId;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.enums.FileType;
import bg.duosoft.ipas.util.DefaultValue;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.Optional;

public class BasicUtils {
    public static final int FIRST_CHAR_INDEX = 0;
    private static final String ZERO_CHAR_APPENDER = "0";

    public static CFileId createCFileId(String filingNumber) {
        if (StringUtils.isEmpty(filingNumber))
            return null;

        String[] parts = filingNumber.split(DefaultValue.IPAS_OBJECT_ID_SEPARATOR);

        CFileId cFileId = new CFileId();
        cFileId.setFileSeq(parts[0]);
        cFileId.setFileType(parts[1]);
        cFileId.setFileSeries(Integer.valueOf(parts[2]));
        cFileId.setFileNbr(Integer.valueOf(parts[3]));
        return cFileId;
    }


    public static boolean isMarkLikeFileType(String fileType){
        if (Objects.isNull(fileType)){
            return false;
        }
        return fileType.equals(FileType.GEOGRAPHICAL_INDICATIONS.code()) || fileType.equals(FileType.GEOGRAPHICAL_INDICATIONS_V.code())
                || fileType.equals(FileType.DIVISIONAL_MARK.code()) || fileType.equals(FileType.MARK.code()) || fileType.equals(FileType.DIVISIONAL_MARK.code())
                || fileType.equals(FileType.INTERNATIONAL_MARK_I.code()) || fileType.equals(FileType.INTERNATIONAL_MARK_R.code()) || fileType.equals(FileType.INTERNATIONAL_MARK_B.code()) || fileType.equals(FileType.INTERNATIONAL_MARK.code());
    }

    public static boolean isPatentLikeFileType(String fileType){
        if (Objects.isNull(fileType)){
            return false;
        }

        return fileType.equals(FileType.PATENT.code()) || fileType.equals(FileType.EU_PATENT.code())
                || fileType.equals(FileType.UTILITY_MODEL.code()) || fileType.equals(FileType.DESIGN.code()) || fileType.equals(FileType.SPC.code())
                || fileType.equals(FileType.PLANTS_AND_BREEDS.code());
    }

    public static String getPatentRelatedFileTypesAsSequence(){
        StringBuilder builder = new StringBuilder();
        builder.append("'"+FileType.PATENT.code()+"'").append(",")
                .append("'"+FileType.EU_PATENT.code()+"'").append(",")
                .append("'"+FileType.UTILITY_MODEL.code()+"'").append(",")
                .append("'"+FileType.DESIGN.code()+"'").append(",")
                .append("'"+FileType.SPC.code()+"'").append(",")
                .append("'"+FileType.PLANTS_AND_BREEDS.code()+"'");
        return builder.toString();
    }
    public static String getMarkRelatedFileTypesAsSequence(){
        StringBuilder builder = new StringBuilder();
        builder.append("'"+FileType.GEOGRAPHICAL_INDICATIONS.code()+"'").append(",")
                .append("'"+FileType.GEOGRAPHICAL_INDICATIONS_V.code()+"'").append(",")
                .append("'"+FileType.DIVISIONAL_MARK.code()+"'").append(",")
                .append("'"+FileType.MARK.code()+"'").append(",")
                .append("'"+FileType.INTERNATIONAL_MARK_I.code()+"'").append(",")
                .append("'"+FileType.INTERNATIONAL_MARK_R.code()+"'").append(",")
                .append("'"+FileType.INTERNATIONAL_MARK_B.code()+"'").append(",")
                .append("'"+FileType.INTERNATIONAL_MARK.code()+"'");
        return builder.toString();
    }

    public static String createFilingNumber(String fileSeq, String fileType, Integer fileSeries, Integer fileNbr) {
        return fileSeq + DefaultValue.IPAS_OBJECT_ID_SEPARATOR + fileType + DefaultValue.IPAS_OBJECT_ID_SEPARATOR + fileSeries + DefaultValue.IPAS_OBJECT_ID_SEPARATOR + fileNbr;
    }

    public static String createExternalSystemId(String seq, String type, Integer ser, String number) {
        return seq + DefaultValue.IPAS_OBJECT_ID_SEPARATOR + type + DefaultValue.IPAS_OBJECT_ID_SEPARATOR + ser + DefaultValue.IPAS_OBJECT_ID_SEPARATOR + number;
    }

    public static String createDocumentFilingNumber(String docOri, String docLog, Integer docSer, Integer docNbr) {
        return docOri + DefaultValue.IPAS_OBJECT_ID_SEPARATOR + docLog + DefaultValue.IPAS_OBJECT_ID_SEPARATOR + docSer + DefaultValue.IPAS_OBJECT_ID_SEPARATOR + docNbr;
    }

    public static CDocumentId createCDocumentId(String filingNumber) {
        if (StringUtils.isEmpty(filingNumber))
            return null;

        String[] parts = filingNumber.split(DefaultValue.IPAS_OBJECT_ID_SEPARATOR);

        CDocumentId cDocumentId = new CDocumentId();
        cDocumentId.setDocOrigin(parts[0]);
        cDocumentId.setDocLog(parts[1]);
        cDocumentId.setDocSeries(Integer.valueOf(parts[2]));
        cDocumentId.setDocNbr(Integer.valueOf(parts[3]));
        return cDocumentId;
    }

    public static String createFilingNumber(CFileId id, boolean containZeroPrefix, int fileNumberLength) {
        if (Objects.isNull(id))
            return null;

        return createFilingNumber(id.getFileSeq(), id.getFileType(), id.getFileSeries(), id.getFileNbr(), containZeroPrefix, fileNumberLength);
    }

    public static String createFilingNumber(String fileSeq, String fileTyp, Integer fileSer, Integer fileNumber, boolean containZeroPrefix, int fileNumberLength) {
        StringBuilder fileNbr = Optional.ofNullable(String.valueOf(fileNumber))
                .map(StringBuilder::new)
                .orElse(null);
        if (Objects.isNull(fileNbr))
            return null;

        if (containZeroPrefix)
            while (fileNbr.length() < fileNumberLength)
                fileNbr.insert(FIRST_CHAR_INDEX, ZERO_CHAR_APPENDER);

        return fileSeq + DefaultValue.IPAS_OBJECT_ID_SEPARATOR + fileTyp + DefaultValue.IPAS_OBJECT_ID_SEPARATOR + fileSer + DefaultValue.IPAS_OBJECT_ID_SEPARATOR + fileNbr;
    }

    public static String selectFileTypeOfFilingNumber(String filingNumber) {
        String[] split = filingNumber.split("/");
        if (split.length != 4)
            throw new RuntimeException("Wrong filing number: " + filingNumber);

        return split[1];
    }

    public static String selectFileTypeOfSessionObjectIdentifier(String sessionObjectIdentifier) {
        String[] split = sessionObjectIdentifier.split("/");
        if (split.length != 4)
            throw new RuntimeException("Wrong sessionIdentifier number: " + sessionObjectIdentifier);
        return split[1];
    }




    public static boolean isNumberOfUserdoc(String filingNumber) {
        String[] parts = filingNumber.split(DefaultValue.IPAS_OBJECT_ID_SEPARATOR);
        return FileType.USERDOC.code().equalsIgnoreCase(parts[1]);
    }

    public static boolean isSpcObject(String fileTyp){
        return FileType.SPC.code().equals(fileTyp);
    }

    public static String generateRegistrationNumberBasedOnDocSeqId(CDocumentId id, CDocumentSeqId seqId) {
        if (Objects.isNull(id) || Objects.isNull(seqId)) {
            return null;
        }

        return id.getDocOrigin() + DefaultValue.IPAS_OBJECT_ID_SEPARATOR + id.getDocLog() + DefaultValue.IPAS_OBJECT_ID_SEPARATOR + seqId.getDocSeqSeries() + DefaultValue.IPAS_OBJECT_ID_SEPARATOR + seqId.getDocSeqNbr();
    }
}
