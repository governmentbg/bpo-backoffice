package bg.duosoft.ipas.util.search;

import bg.duosoft.ipas.persistence.model.entity.person.IpPersonAddressesPK;
import org.apache.lucene.document.Document;
import org.hibernate.search.bridge.LuceneOptions;
import org.hibernate.search.bridge.TwoWayFieldBridge;

public class IpPersonAddressPkBridge implements TwoWayFieldBridge {
    private final static String DELIM = "/";

    @Override
    public Object get(String s, Document document) {
        String[] split = document.get(s).split(DELIM);
        if (split.length != 2) {
            throw new RuntimeException("index doesn't contain correct pk");
        }

        IpPersonAddressesPK ipPersonAddressesPK = new IpPersonAddressesPK(
                Integer.parseInt(split[0]),
                Integer.parseInt(split[1]));

        return ipPersonAddressesPK;
    }

    @Override
    public String objectToString(Object o) {
        IpPersonAddressesPK ipPersonAddressesPK =  (IpPersonAddressesPK) o;

        StringBuilder sb = new StringBuilder();
        sb.append(ipPersonAddressesPK.getPersonNbr());
        sb.append(DELIM);
        sb.append(ipPersonAddressesPK.getAddrNbr());

        return sb.toString();
    }

    @Override
    public void set(String s, Object o, Document document, LuceneOptions luceneOptions) {
        luceneOptions.addFieldToDocument(s, objectToString(o), document);
    }
}
