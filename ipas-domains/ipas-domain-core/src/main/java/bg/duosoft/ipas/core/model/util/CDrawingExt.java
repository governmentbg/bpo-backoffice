package bg.duosoft.ipas.core.model.util;

import bg.duosoft.ipas.core.model.patent.CDrawing;
import de.danielbechler.diff.inclusion.Inclusion;
import de.danielbechler.diff.introspection.ObjectDiffProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Objects;

@Getter
@EqualsAndHashCode
public class CDrawingExt extends CDrawing {

    private Long drawingNbrDb;

    public static CDrawingExt createCDrawingExtObject(CDrawing cDrawing){
        CDrawingExt cDrawingExt= new CDrawingExt();
        cDrawingExt.setDrawingNbr(cDrawing.getDrawingNbr());
        cDrawingExt.setDrawingType(cDrawing.getDrawingType());
        cDrawingExt.drawingNbrDb = cDrawing.getDrawingNbr();
        cDrawingExt.setSingleDesignExtended(cDrawing.getSingleDesignExtended());
        return cDrawingExt;
    }
    @ObjectDiffProperty(inclusion = Inclusion.EXCLUDED)
    public Long getDrawingNbrDb() {
        return drawingNbrDb;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) {
            return false;
        }
        //DO NOT change this!!!The idea is if the o is of type CDrawing, it's equals method should be used to compare CDrawingExt(this) and CDrawing(o) !!!
        if (o.getClass().equals(CDrawing.class)) {
            return o.equals(this);
        }
        if (getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CDrawingExt that = (CDrawingExt) o;
        return Objects.equals(drawingNbrDb, that.drawingNbrDb);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), drawingNbrDb);
    }
}
