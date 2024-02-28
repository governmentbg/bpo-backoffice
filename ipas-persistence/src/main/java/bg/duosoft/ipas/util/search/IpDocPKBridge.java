package bg.duosoft.ipas.util.search;

import bg.duosoft.ipas.persistence.model.entity.FileSeqTypSerNbrPK;
import bg.duosoft.ipas.persistence.model.entity.doc.IpDocPK;
import bg.duosoft.ipas.persistence.model.entity.file.IpFilePK;
import org.apache.lucene.document.Document;
import org.hibernate.search.bridge.LuceneOptions;
import org.hibernate.search.bridge.TwoWayFieldBridge;

public class IpDocPKBridge implements TwoWayFieldBridge {

    private final static String DOC_ORI = ".docOri";
    private final static String DOC_LOG = ".docLog";
    private final static String DOC_SER = ".docSer";
    private final static String DOC_NBR = ".docNbr";
    private final static String DELIM = "/";

    @Override
    public Object get(String s, Document document) {
        String[] split = document.get(s).split(DELIM);
        if (split.length != 4) {
            throw new RuntimeException("index doesn't contain correct pk");
        }

        IpDocPK ipDocPK = new IpDocPK(
                split[0],
                split[1],
                Integer.parseInt(split[2]),
                Integer.parseInt(split[3]));

        return ipDocPK;
    }

    @Override
    public String objectToString(Object o) {
        return convertToString(o);
    }

    @Override
    public void set(String s, Object o, Document document, LuceneOptions luceneOptions) {
        luceneOptions.addSortedDocValuesFieldToDocument(s, objectToString(o), document);
        luceneOptions.addFieldToDocument(s, objectToString(o), document);
    }

    public static String convertToString(Object o) {
        IpDocPK ipDocPK = (IpDocPK) o;

        StringBuilder sb = new StringBuilder();

        sb.append(ipDocPK.getDocOri());
        sb.append(DELIM);

        sb.append(ipDocPK.getDocLog());
        sb.append(DELIM);

        sb.append(ipDocPK.getDocSer());
        sb.append(DELIM);

        sb.append(ipDocPK.getDocNbr());

        return sb.toString();
    }
}
