package bg.duosoft.ipas.core.model.userdoc.grounds;

import bg.duosoft.ipas.core.model.CApplicationSubType;
import bg.duosoft.ipas.core.model.miscellaneous.CGeoCountry;
import bg.duosoft.ipas.enums.MarkSignType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CMarkGroundData implements Serializable {
    private String registrationNbr;
    private Date registrationDate;
    private Boolean niceClassesInd;
    private Boolean markImportedInd;
    private String nameText;
    private byte[] nameData;
    private String filingNumber;
    private Date filingDate;
    private CMarkGroundType markGroundType;
    private CLegalGroundCategories legalGroundCategory;
    private CGeoCountry registrationCountry;
    private CApplicationSubType geographicalIndTyp;
    private MarkSignType markSignTyp;
    private List<CGroundNiceClasses> userdocGroundsNiceClasses;

    public void initMarkRelatedData(String registrationNbr,Date registrationDate,String nameText,String filingNumber,
                                   Date filingDate,String markSignTypCode){
        this.setFilingNumber(filingNumber);
        this.setFilingDate(filingDate);
        this.setRegistrationNbr(registrationNbr);
        this.setRegistrationDate(registrationDate);
        this.setNameText(nameText);
        if (!Objects.isNull(markSignTypCode)){
            this.setMarkSignTyp(MarkSignType.selectByCode(markSignTypCode));
        }
    }

}
