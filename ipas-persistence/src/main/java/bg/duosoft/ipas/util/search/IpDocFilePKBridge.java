package bg.duosoft.ipas.util.search;

import bg.duosoft.ipas.persistence.model.entity.doc.IpDocFilesPK;
import bg.duosoft.ipas.persistence.model.entity.doc.IpDocPK;
import org.apache.lucene.document.Document;
import org.hibernate.search.bridge.LuceneOptions;
import org.hibernate.search.bridge.TwoWayFieldBridge;

public class IpDocFilePKBridge implements TwoWayFieldBridge {

    private final static String DOC_ORI = ".docOri";
    private final static String DOC_LOG = ".docLog";
    private final static String DOC_SER = ".docSer";
    private final static String DOC_NBR = ".docNbr";
    private final static String DELIM = "/";

    @Override
    public Object get(String s, Document document) {
        String[] split = document.get(s).split(DELIM);
        if (split.length != 8) {
            throw new RuntimeException("index doesn't contain correct pk");
        }

        IpDocFilesPK ipDocPK = new IpDocFilesPK(
                split[0],
                split[1],
                Integer.parseInt(split[2]),
                Integer.parseInt(split[3]),
                split[4],
                split[5],
                Integer.parseInt(split[6]),
                Integer.parseInt(split[7]));

        return ipDocPK;
    }

    @Override
    public String objectToString(Object o) {
        IpDocFilesPK ipDocFilesPK = (IpDocFilesPK) o;

        StringBuilder sb = new StringBuilder();

        sb.append(ipDocFilesPK.getDocOri());
        sb.append(DELIM);

        sb.append(ipDocFilesPK.getDocLog());
        sb.append(DELIM);

        sb.append(ipDocFilesPK.getDocSer());
        sb.append(DELIM);

        sb.append(ipDocFilesPK.getDocNbr());
        sb.append(DELIM);

        sb.append(ipDocFilesPK.getFileSeq());
        sb.append(DELIM);

        sb.append(ipDocFilesPK.getFileTyp());
        sb.append(DELIM);

        sb.append(ipDocFilesPK.getFileSer());
        sb.append(DELIM);

        sb.append(ipDocFilesPK.getFileNbr());

        return sb.toString();
    }

    @Override
    public void set(String s, Object o, Document document, LuceneOptions luceneOptions) {
        luceneOptions.addFieldToDocument(s, objectToString(o), document);

        IpDocFilesPK ipDocFilesPK = (IpDocFilesPK) o;

        StringBuilder sb = new StringBuilder();

        sb.append(ipDocFilesPK.getDocOri());
        sb.append(DELIM);

        sb.append(ipDocFilesPK.getDocLog());
        sb.append(DELIM);

        sb.append(ipDocFilesPK.getDocSer());
        sb.append(DELIM);

        sb.append(ipDocFilesPK.getDocNbr());

        luceneOptions.addFieldToDocument("file.ipDocFilesCollection.ipDocPK", sb.toString(), document);
    }
}
