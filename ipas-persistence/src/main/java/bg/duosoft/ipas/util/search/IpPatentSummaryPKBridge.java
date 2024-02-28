package bg.duosoft.ipas.util.search;

import bg.duosoft.ipas.persistence.model.entity.patent.IpPatentSummaryPK;
import org.apache.lucene.document.Document;
import org.hibernate.search.bridge.LuceneOptions;
import org.hibernate.search.bridge.TwoWayFieldBridge;

public class IpPatentSummaryPKBridge implements TwoWayFieldBridge {

    private final static String DELIM = "/";

    @Override
    public Object get(String s, Document document) {
        String[] split = document.get(s).split(DELIM);
        if (split.length != 5) {
            throw new RuntimeException("index doesn't contain correct pk");
        }

        IpPatentSummaryPK ipPatentSummaryPK = new IpPatentSummaryPK(
                split[0],
                split[1],
                Integer.parseInt(split[2]),
                Integer.parseInt(split[3]),
                split[4]);

        return ipPatentSummaryPK;
    }

    @Override
    public String objectToString(Object o) {
        IpPatentSummaryPK ipPatentSummaryPK = (IpPatentSummaryPK) o;

        StringBuilder sb = new StringBuilder();

        String filePkAsString = getFilePkAsString(o);
        sb.append(filePkAsString);
        sb.append(DELIM);

        sb.append(ipPatentSummaryPK.getLanguageCode());

        return sb.toString();
    }


    public String getFilePkAsString(Object o) {
        IpPatentSummaryPK ipPatentSummaryPK = (IpPatentSummaryPK) o;

        StringBuilder sb = new StringBuilder();

        sb.append(ipPatentSummaryPK.getFileSeq());
        sb.append(DELIM);

        sb.append(ipPatentSummaryPK.getFileTyp());
        sb.append(DELIM);

        sb.append(ipPatentSummaryPK.getFileSer());
        sb.append(DELIM);

        sb.append(ipPatentSummaryPK.getFileNbr());

        return sb.toString();
    }

    @Override
    public void set(String s, Object o, Document document, LuceneOptions luceneOptions) {
        luceneOptions.addFieldToDocument(s, objectToString(o), document);
        luceneOptions.addSortedDocValuesFieldToDocument("file." + s, getFilePkAsString(o), document);
    }
}
