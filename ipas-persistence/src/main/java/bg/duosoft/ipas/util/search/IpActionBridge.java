package bg.duosoft.ipas.util.search;

import bg.duosoft.ipas.persistence.model.entity.action.IpActionPK;
import org.apache.lucene.document.Document;
import org.hibernate.search.bridge.LuceneOptions;
import org.hibernate.search.bridge.TwoWayFieldBridge;

public class IpActionBridge implements TwoWayFieldBridge {

    private final static String DELIM = "/";

    @Override
    public Object get(String s, Document document) {
        String[] split = document.get(s).split(DELIM);
        if (split.length != 3) {
            throw new RuntimeException("index doesn't contain correct pk");
        }

        IpActionPK ipActionPK = new IpActionPK(
                split[0],
                Integer.parseInt(split[1]),
                Integer.parseInt(split[2]));

        return ipActionPK;
    }

    @Override
    public String objectToString(Object o) {
        IpActionPK ipActionPK = (IpActionPK) o;

        StringBuilder sb = new StringBuilder();

        sb.append(ipActionPK.getProcTyp());
        sb.append(DELIM);

        sb.append(ipActionPK.getProcNbr());
        sb.append(DELIM);

        sb.append(ipActionPK.getActionNbr());

        return sb.toString();
    }

    @Override
    public void set(String s, Object o, Document document, LuceneOptions luceneOptions) {
        luceneOptions.addFieldToDocument(s, objectToString(o), document);
    }
}
