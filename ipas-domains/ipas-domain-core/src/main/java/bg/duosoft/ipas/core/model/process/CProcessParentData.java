package bg.duosoft.ipas.core.model.process;

import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.file.CProcessId;
import bg.duosoft.ipas.core.model.miscellaneous.CStatus;
import bg.duosoft.ipas.core.model.offidoc.COffidocId;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Data
public class CProcessParentData implements Serializable {
    private CFileId fileId;
    private CDocumentId userdocId;
    private String userdocType;
    private COffidocId offidocId;
    private String userdocRegistrationNumber;
    private String offidocRegistrationNumber;
    private CProcessId processId;
    private CProcessParentData parent;
    private CProcessId topProcessId;
    private CTopProcessFileData topProcessFileData;
    private Boolean isManualSubProcess;

    public boolean isTopProcessFile() {
        if (Objects.isNull(this.fileId))
            return false;

        return this.fileId.createFilingNumber().equals(this.topProcessFileData.getFileId().createFilingNumber());
    }

    public String selectConcatenatedIdentifier() {
        if (Objects.nonNull(this.fileId)) {
            return this.fileId.getFileSeq() + "/" + this.fileId.getFileType() + "/" + this.fileId.getFileSeries() + "/" + this.fileId.getFileNbr();
        }
        if (Objects.nonNull(this.userdocId)) {
            return this.userdocId.getDocOrigin() + "/" + this.userdocId.getDocLog() + "/" + this.userdocId.getDocSeries() + "/" + this.userdocId.getDocNbr();
        }
        if (Objects.nonNull(this.offidocId)) {
            return this.offidocId.getOffidocOrigin() + "/" + this.offidocId.getOffidocSeries() + "/" + this.offidocId.getOffidocNbr();
        }
        return null;
    }

    public boolean isFileProcess() {
        return Objects.nonNull(this.fileId);
    }

    public boolean isUserdocProcess() {
        return Objects.nonNull(this.userdocId);
    }

    public boolean isOffidocProcess() {
        return Objects.nonNull(this.offidocId);
    }

}
