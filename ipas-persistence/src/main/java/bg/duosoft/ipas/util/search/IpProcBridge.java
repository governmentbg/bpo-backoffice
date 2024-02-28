package bg.duosoft.ipas.util.search;

import bg.duosoft.ipas.persistence.model.entity.action.IpActionPK;
import bg.duosoft.ipas.persistence.model.entity.process.IpProcPK;
import org.apache.lucene.document.Document;
import org.hibernate.search.bridge.LuceneOptions;
import org.hibernate.search.bridge.TwoWayFieldBridge;

public class IpProcBridge implements TwoWayFieldBridge {

    private final static String DELIM = "/";

    @Override
    public Object get(String s, Document document) {
        String[] split = document.get(s).split(DELIM);
        if (split.length != 2) {
            throw new RuntimeException("index doesn't contain correct pk");
        }

        IpProcPK ipProcPK = new IpProcPK(
                split[0],
                Integer.parseInt(split[1]));

        return ipProcPK;
    }

    @Override
    public String objectToString(Object o) {

        StringBuilder sb = new StringBuilder();
        if (o instanceof IpProcPK) {
            IpProcPK ipProcPK = (IpProcPK) o;

            sb.append(ipProcPK.getProcTyp());
            sb.append(DELIM);
            sb.append(ipProcPK.getProcNbr());
        }

        if (o instanceof IpActionPK) {
            IpActionPK ipActionPK = (IpActionPK) o;

            sb.append(ipActionPK.getProcTyp());
            sb.append(DELIM);
            sb.append(ipActionPK.getProcNbr());
        }

        return sb.toString();
    }

    @Override
    public void set(String s, Object o, Document document, LuceneOptions luceneOptions) {
        luceneOptions.addFieldToDocument(s, objectToString(o), document);
    }
}
