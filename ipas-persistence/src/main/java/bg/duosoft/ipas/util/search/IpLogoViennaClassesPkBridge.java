package bg.duosoft.ipas.util.search;

import bg.duosoft.ipas.persistence.model.entity.mark.IpLogoViennaClassesPK;
import org.apache.lucene.document.Document;
import org.hibernate.search.bridge.LuceneOptions;
import org.hibernate.search.bridge.TwoWayFieldBridge;

public class IpLogoViennaClassesPkBridge implements TwoWayFieldBridge {
    private final static String DELIM = "/";

    @Override
    public Object get(String s, Document document) {
        String[] split = document.get(s).split(DELIM);
        if (split.length != 7) {
            throw new RuntimeException("index doesn't contain correct pk");
        }

        IpLogoViennaClassesPK ipLogoViennaClassesPK = new IpLogoViennaClassesPK(
                split[0],
                split[1],
                Integer.parseInt(split[2]),
                Integer.parseInt(split[3]),
                Long.parseLong(split[4]),
                Long.parseLong(split[5]),
                Long.parseLong(split[6]));

        return ipLogoViennaClassesPK;
    }

    @Override
    public String objectToString(Object o) {
        IpLogoViennaClassesPK ipLogoViennaClassesPK = (IpLogoViennaClassesPK) o;

        StringBuilder sb = new StringBuilder();

        sb.append(ipLogoViennaClassesPK.getFileSeq());
        sb.append(DELIM);

        sb.append(ipLogoViennaClassesPK.getFileTyp());
        sb.append(DELIM);

        sb.append(ipLogoViennaClassesPK.getFileSer());
        sb.append(DELIM);

        sb.append(ipLogoViennaClassesPK.getFileNbr());
        sb.append(DELIM);

        sb.append(ipLogoViennaClassesPK.getViennaClassCode());
        sb.append(DELIM);

        sb.append(ipLogoViennaClassesPK.getViennaGroupCode());
        sb.append(DELIM);

        sb.append(ipLogoViennaClassesPK.getViennaElemCode());

        return sb.toString();
    }

    @Override
    public void set(String s, Object o, Document document, LuceneOptions luceneOptions) {
        luceneOptions.addFieldToDocument(s, objectToString(o), document);
    }
}
