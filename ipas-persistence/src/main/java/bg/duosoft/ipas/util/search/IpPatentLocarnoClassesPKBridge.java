package bg.duosoft.ipas.util.search;

import bg.duosoft.ipas.persistence.model.entity.FileSeqTypSerNbrPK;
import bg.duosoft.ipas.persistence.model.entity.file.IpFilePK;
import bg.duosoft.ipas.persistence.model.entity.patent.IpPatentLocarnoClassesPK;
import org.apache.lucene.document.Document;
import org.hibernate.search.bridge.LuceneOptions;
import org.hibernate.search.bridge.TwoWayFieldBridge;

public class IpPatentLocarnoClassesPKBridge implements TwoWayFieldBridge {

    private final static String FILE_SEQ = ".fileSeq";
    private final static String FILE_TYP = ".fileTyp";
    private final static String FILE_SER = ".fileSer";
    private final static String FILE_NBR = ".fileNbr";
    private final static String DELIM = "/";

    @Override
    public Object get(String s, Document document) {
        String[] split = document.get(s).split(DELIM);
        if (split.length != 5) {
            throw new RuntimeException("index doesn't contain correct pk");
        }

        IpPatentLocarnoClassesPK ipPatentLocarnoClassesPK = new IpPatentLocarnoClassesPK(
                split[0],
                split[1],
                Integer.parseInt(split[2]),
                Integer.parseInt(split[3]),
                split[4]);

        return ipPatentLocarnoClassesPK;
    }

    @Override
    public String objectToString(Object o) {
        IpPatentLocarnoClassesPK ipPatentLocarnoClassesPK = (IpPatentLocarnoClassesPK) o;

        StringBuilder sb = new StringBuilder();

        sb.append(getFilePkAsString(ipPatentLocarnoClassesPK));
        sb.append(DELIM);

        sb.append(ipPatentLocarnoClassesPK.getLocarnoClassCode());

        return sb.toString();
    }

    private String getFilePkAsString(Object o) {
        IpPatentLocarnoClassesPK ipPatentLocarnoClassesPK = (IpPatentLocarnoClassesPK) o;

        StringBuilder sb = new StringBuilder();

        sb.append(ipPatentLocarnoClassesPK.getFileSeq());
        sb.append(DELIM);

        sb.append(ipPatentLocarnoClassesPK.getFileTyp());
        sb.append(DELIM);

        sb.append(ipPatentLocarnoClassesPK.getFileSer());
        sb.append(DELIM);

        sb.append(ipPatentLocarnoClassesPK.getFileNbr());

        return sb.toString();
    }

    @Override
    public void set(String s, Object o, Document document, LuceneOptions luceneOptions) {
        luceneOptions.addFieldToDocument(s, objectToString(o), document);
        luceneOptions.addSortedDocValuesFieldToDocument("file." + s, getFilePkAsString(o), document);
    }
}
