package bg.duosoft.ipas.util.search;

import bg.duosoft.ipas.persistence.model.entity.FileSeqTypSerNbrPK;
import bg.duosoft.ipas.persistence.model.entity.file.IpFilePK;
import org.apache.lucene.document.Document;
import org.hibernate.search.bridge.LuceneOptions;
import org.hibernate.search.bridge.TwoWayFieldBridge;

public class IpFileBridge implements TwoWayFieldBridge {

    private final static String FILE_SEQ = ".fileSeq";
    private final static String FILE_TYP = ".fileTyp";
    private final static String FILE_SER = ".fileSer";
    private final static String FILE_NBR = ".fileNbr";
    private final static String DELIM = "/";

    @Override
    public Object get(String s, Document document) {
        String[] split = document.get(s).split(DELIM);
        if (split.length != 4) {
            throw new RuntimeException("index doesn't contain correct pk");
        }

        FileSeqTypSerNbrPK ipFilePK = new IpFilePK(
                split[0],
                split[1],
                Integer.parseInt(split[2]),
                Integer.parseInt(split[3]));

        return ipFilePK;
    }

    @Override
    public String objectToString(Object o) {
        FileSeqTypSerNbrPK ipFilePK = (FileSeqTypSerNbrPK) o;

        StringBuilder sb = new StringBuilder();

        sb.append(ipFilePK.getFileSeq());
        sb.append(DELIM);

        sb.append(ipFilePK.getFileTyp());
        sb.append(DELIM);

        sb.append(ipFilePK.getFileSer());
        sb.append(DELIM);

        sb.append(ipFilePK.getFileNbr());

        return sb.toString();
    }

    @Override
    public void set(String s, Object o, Document document, LuceneOptions luceneOptions) {
        luceneOptions.addFieldToDocument(s, objectToString(o), document);
    }
}
