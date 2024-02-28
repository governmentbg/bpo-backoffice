package bg.duosoft.ipas.util.search;

import bg.duosoft.ipas.persistence.model.entity.mark.IpMarkAttachmentViennaClassesPK;
import org.apache.lucene.document.Document;
import org.hibernate.search.bridge.LuceneOptions;
import org.hibernate.search.bridge.TwoWayFieldBridge;

public class IpMarkAttachmentViennaClassesPkBridge implements TwoWayFieldBridge {
    private final static String DELIM = "/";

    @Override
    public Object get(String s, Document document) {
        String[] split = document.get(s).split(DELIM);
        if (split.length != 8) {
            throw new RuntimeException("index doesn't contain correct pk");
        }

        IpMarkAttachmentViennaClassesPK pk = new IpMarkAttachmentViennaClassesPK(
                Integer.parseInt(split[7]),
                split[0],
                split[1],
                Integer.parseInt(split[2]),
                Integer.parseInt(split[3]),
                Long.parseLong(split[4]),
                Long.parseLong(split[5]),
                Long.parseLong(split[6]));

        return pk;
    }

    @Override
    public String objectToString(Object o) {
        IpMarkAttachmentViennaClassesPK pk = (IpMarkAttachmentViennaClassesPK) o;

        StringBuilder sb = new StringBuilder();

        sb.append(pk.getFileSeq());
        sb.append(DELIM);

        sb.append(pk.getFileTyp());
        sb.append(DELIM);

        sb.append(pk.getFileSer());
        sb.append(DELIM);

        sb.append(pk.getFileNbr());
        sb.append(DELIM);

        sb.append(pk.getViennaClassCode());
        sb.append(DELIM);

        sb.append(pk.getViennaGroupCode());
        sb.append(DELIM);

        sb.append(pk.getViennaElemCode());
        sb.append(DELIM);

        sb.append(pk.getAttachmentId());

        return sb.toString();
    }

    @Override
    public void set(String s, Object o, Document document, LuceneOptions luceneOptions) {
        luceneOptions.addFieldToDocument(s, objectToString(o), document);
    }
}
