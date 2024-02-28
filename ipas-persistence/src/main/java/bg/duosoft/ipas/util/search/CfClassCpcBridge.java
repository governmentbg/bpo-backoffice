package bg.duosoft.ipas.util.search;

import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfClassCpc;
import org.apache.lucene.document.Document;
import org.hibernate.search.bridge.LuceneOptions;
import org.hibernate.search.bridge.TwoWayFieldBridge;

public class CfClassCpcBridge implements TwoWayFieldBridge {

    private final static String CPC_CODE = "cpcCode";

    @Override
    public Object get(String s, Document document) {
        String cpcCode = document.get(s);

        return cpcCode;
    }

    @Override
    public String objectToString(Object o) {
        if (o instanceof CfClassCpc) {
            CfClassCpc cfClassCpc  = (CfClassCpc) o;

            StringBuilder sb = new StringBuilder();

            sb.append(cfClassCpc.getPk().getCpcSectionCode());

            sb.append(cfClassCpc.getPk().getCpcClassCode());

            sb.append(cfClassCpc.getPk().getCpcSubclassCode());

            sb.append(cfClassCpc.getPk().getCpcGroupCode());

            sb.append(cfClassCpc.getPk().getCpcSubgroupCode());

            sb.append(" - ").append(cfClassCpc.getPk().getCpcEditionCode());

            return sb.toString();
        }

        if(o instanceof String) {
            return (String) o;
        }

        throw new RuntimeException("Object isn't instace of CfClassCpc or String!");
    }

    @Override
    public void set(String s, Object o, Document document, LuceneOptions luceneOptions) {
        luceneOptions.addFieldToDocument(CPC_CODE, objectToString(o), document);
    }
}
