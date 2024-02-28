package bg.duosoft.ipas.util.search;

import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfClassIpc;
import org.apache.lucene.document.Document;
import org.hibernate.search.bridge.LuceneOptions;
import org.hibernate.search.bridge.TwoWayFieldBridge;

public class CfClassIpcBridge implements TwoWayFieldBridge {

    private final static String IPC_CODE = "ipcCode";

    @Override
    public Object get(String s, Document document) {
        String ipcCode = document.get(s);

        return ipcCode;
    }

    @Override
    public String objectToString(Object o) {
        if (o instanceof CfClassIpc) {
            CfClassIpc cfClassIpc  = (CfClassIpc) o;

            StringBuilder sb = new StringBuilder();

            sb.append(cfClassIpc.getPk().getIpcSectionCode());

            sb.append(cfClassIpc.getPk().getIpcClassCode());

            sb.append(cfClassIpc.getPk().getIpcSubclassCode());

            sb.append(cfClassIpc.getPk().getIpcGroupCode());

            sb.append(cfClassIpc.getPk().getIpcSubgroupCode());

            sb.append(" - ").append(cfClassIpc.getPk().getIpcEditionCode());


            return sb.toString();
        }

        if(o instanceof String) {
            return (String) o;
        }

        throw new RuntimeException("Object isn't instace of CfClassIpc or String!");
    }

    @Override
    public void set(String s, Object o, Document document, LuceneOptions luceneOptions) {
        luceneOptions.addFieldToDocument(IPC_CODE, objectToString(o), document);
    }
}
