package bg.duosoft.ipas.core.model.patent;

import bg.duosoft.ipas.core.model.design.CSingleDesignExtended;
import bg.duosoft.ipas.core.model.util.CDrawingExt;
import de.danielbechler.diff.inclusion.Inclusion;
import de.danielbechler.diff.introspection.ObjectDiffProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CDrawing implements Serializable {

    private static final long serialVersionUID = 245591712498572858L;
    private Long drawingNbr;
    private String drawingType;
    private byte[] drawingData;
    private CSingleDesignExtended singleDesignExtended;

    @ObjectDiffProperty(inclusion = Inclusion.EXCLUDED)
    public String getBase64EncodedDrawingData() {
        if (Objects.isNull(this.drawingData))
            return null;
        return Base64.getEncoder().encodeToString(this.drawingData);
    }

    public boolean isLoaded() {
        return null != this.drawingData && 0 != this.drawingData.length;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        //DO not change this: The original version is if (o == null || getClass() != o.getClass()) return false;
        //The idea is if the o is of CDrawingExt, only the CDrawing's fields are getting compared!!!!
        if (o == null) {
            return false;
        }
        if (getClass() != o.getClass() && !o.getClass().equals(CDrawingExt.class)) {
            return false;
        }
        CDrawing cDrawing = (CDrawing) o;
        return Objects.equals(drawingNbr, cDrawing.drawingNbr) && Objects.equals(drawingType, cDrawing.drawingType) && Arrays.equals(drawingData, cDrawing.drawingData) && Objects.equals(singleDesignExtended, cDrawing.singleDesignExtended);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(drawingNbr, drawingType, singleDesignExtended);
        result = 31 * result + Arrays.hashCode(drawingData);
        return result;
    }
}
