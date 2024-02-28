package bg.duosoft.ipas.util.search;

import bg.duosoft.ipas.persistence.model.entity.mark.IpMarkNiceClassesPK;
import org.apache.lucene.document.Document;
import org.hibernate.search.bridge.LuceneOptions;
import org.hibernate.search.bridge.TwoWayFieldBridge;

public class IpMarkNiceClassesPkBridge implements TwoWayFieldBridge {
    private final static String DELIM = "/";

    @Override
    public Object get(String s, Document document) {
        return toIpMarkNiceClassesPK(document.get(s));
    }

    public static IpMarkNiceClassesPK toIpMarkNiceClassesPK(String s) {
        String[] split = s.split(DELIM);
        if (split.length != 6) {
            throw new RuntimeException("index doesn't contain correct pk");
        }

        IpMarkNiceClassesPK ipMarkNiceClassesPK = new IpMarkNiceClassesPK(
                split[0],
                split[1],
                Integer.parseInt(split[2]),
                Integer.parseInt(split[3]),
                Long.parseLong(split[4]),
                split[5]);

        return ipMarkNiceClassesPK;
    }

    @Override
    public String objectToString(Object o) {
        IpMarkNiceClassesPK ipMarkNiceClassesPK = (IpMarkNiceClassesPK) o;

        StringBuilder sb = new StringBuilder();

        sb.append(ipMarkNiceClassesPK.getFileSeq());
        sb.append(DELIM);

        sb.append(ipMarkNiceClassesPK.getFileTyp());
        sb.append(DELIM);

        sb.append(ipMarkNiceClassesPK.getFileSer());
        sb.append(DELIM);

        sb.append(ipMarkNiceClassesPK.getFileNbr());
        sb.append(DELIM);

        sb.append(ipMarkNiceClassesPK.getNiceClassCode());
        sb.append(DELIM);

        sb.append(ipMarkNiceClassesPK.getNiceClassStatusWcode());

        return sb.toString();
    }

    @Override
    public void set(String s, Object o, Document document, LuceneOptions luceneOptions) {
        luceneOptions.addFieldToDocument(s, objectToString(o), document);
    }
}
