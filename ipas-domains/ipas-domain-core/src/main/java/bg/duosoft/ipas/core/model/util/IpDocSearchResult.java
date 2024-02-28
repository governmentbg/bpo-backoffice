package bg.duosoft.ipas.core.model.util;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
public class IpDocSearchResult implements Serializable {
    private String pk;
    private String docSeqNbr;


    public IpDocSearchResult pk(String pk) {
        this.pk = pk;
        return this;
    }

    public IpDocSearchResult docSeqNbr(String docSeqNbr) {
        this.docSeqNbr = docSeqNbr;
        return this;
    }
}
