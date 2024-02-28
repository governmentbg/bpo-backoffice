package bg.duosoft.ipas.util.search;

import bg.duosoft.ipas.persistence.model.entity.FileSeqTypSerNbrPK;
import bg.duosoft.ipas.persistence.model.entity.file.IpFilePK;
import bg.duosoft.ipas.persistence.model.entity.file.IpFileRelationshipPK;
import bg.duosoft.ipas.persistence.model.entity.patent.IpPatentLocarnoClassesPK;
import org.apache.lucene.document.Document;
import org.hibernate.search.bridge.LuceneOptions;
import org.hibernate.search.bridge.TwoWayFieldBridge;

public class IpFileRelationshipPkBridge implements TwoWayFieldBridge {

    private final static String FILE_SEQ = ".fileSeq";
    private final static String FILE_TYP = ".fileTyp";
    private final static String FILE_SER = ".fileSer";
    private final static String FILE_NBR = ".fileNbr";
    private final static String DELIM = "/";

    @Override
    public Object get(String s, Document document) {
        String[] split = document.get(s).split(DELIM);
        if (split.length != 9) {
            throw new RuntimeException("index doesn't contain correct pk");
        }

        IpFileRelationshipPK ipFileRelationshipPK = new IpFileRelationshipPK(
                split[0],
                split[1],
                Integer.parseInt(split[2]),
                Integer.parseInt(split[3]),
                split[4],
                split[5],
                Integer.parseInt(split[6]),
                Integer.parseInt(split[7]),
                split[8]);

        return ipFileRelationshipPK;
    }

    @Override
    public String objectToString(Object o) {
        StringBuilder sb = new StringBuilder();

        sb.append(getFile1PkAsString(o));
        sb.append(DELIM);

        sb.append(getFile2PkAsString(o));
        sb.append(DELIM);

        sb.append(getRelationshipTypePkAsString(o));

        return sb.toString();
    }

    @Override
    public void set(String s, Object o, Document document, LuceneOptions luceneOptions) {
        luceneOptions.addFieldToDocument(s, objectToString(o), document);
        luceneOptions.addFieldToDocument("file1." + s, getFile1PkAsString(o), document);
        luceneOptions.addSortedDocValuesFieldToDocument("file1." + s, getFile1PkAsString(o), document);
        luceneOptions.addFieldToDocument("file2." + s, getFile2PkAsString(o), document);
        luceneOptions.addSortedDocValuesFieldToDocument("file2." + s, getFile2PkAsString(o), document);
        luceneOptions.addFieldToDocument("relationship_type", getRelationshipTypePkAsString(o), document);
    }

    private String getFile1PkAsString(Object o) {
        IpFileRelationshipPK ipFileRelationshipPK = (IpFileRelationshipPK) o;

        StringBuilder sb = new StringBuilder();

        sb.append(ipFileRelationshipPK.getFileSeq1());
        sb.append(DELIM);

        sb.append(ipFileRelationshipPK.getFileTyp1());
        sb.append(DELIM);

        sb.append(ipFileRelationshipPK.getFileSer1());
        sb.append(DELIM);

        sb.append(ipFileRelationshipPK.getFileNbr1());

        return sb.toString();
    }

    private String getFile2PkAsString(Object o) {
        IpFileRelationshipPK ipFileRelationshipPK = (IpFileRelationshipPK) o;

        StringBuilder sb = new StringBuilder();

        sb.append(ipFileRelationshipPK.getFileSeq2());
        sb.append(DELIM);

        sb.append(ipFileRelationshipPK.getFileTyp2());
        sb.append(DELIM);

        sb.append(ipFileRelationshipPK.getFileSer2());
        sb.append(DELIM);

        sb.append(ipFileRelationshipPK.getFileNbr2());

        return sb.toString();
    }

    private String getRelationshipTypePkAsString(Object o) {
        IpFileRelationshipPK ipFileRelationshipPK = (IpFileRelationshipPK) o;

        return ipFileRelationshipPK.getRelationshipTyp();
    }
}
