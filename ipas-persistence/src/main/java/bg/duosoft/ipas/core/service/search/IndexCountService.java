package bg.duosoft.ipas.core.service.search;

import java.io.Serializable;

public interface IndexCountService {

    public Integer countMarks();

    public Integer countPatents();

    public Integer countPatentLocarnoClasses();

    public Integer countFileRelationships();

    public Integer countActions();

    public Integer countProcesses();

    public Integer countPersonAddresses();

    public Integer countPatentSummary();

    public Integer countIpcClasses();

    public Integer countCpcClasses();

    public Integer countNiceClasses();

    public Integer countViennaClasses();

    public Integer countMarkAttachmentViennaClasses();

    public Integer countDocs();

    public Integer countUserDocs();

    public Integer countUserDocPersons();
}
