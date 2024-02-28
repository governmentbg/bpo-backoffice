package bg.duosoft.ipas.util.search;

import bg.duosoft.ipas.persistence.model.entity.FileSeqTypSerNbrPK;
import bg.duosoft.ipas.persistence.model.entity.doc.IpDoc;
import bg.duosoft.ipas.persistence.model.entity.file.IpFilePK;
import bg.duosoft.ipas.persistence.model.entity.process.IpProcSimple;
import org.apache.lucene.document.Document;
import org.hibernate.search.bridge.LuceneOptions;
import org.hibernate.search.bridge.TwoWayFieldBridge;

import java.util.Objects;

public class IpDocFileFieldBridge implements TwoWayFieldBridge {

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

        IpProcSimple subProc = new IpProcSimple();
        subProc.setFileSeq(split[0]);
        subProc.setFileTyp(split[1]);
        subProc.setFileSer(Integer.parseInt(split[2]));
        subProc.setFileNbr(Integer.parseInt(split[3]));


        IpProcSimple proc = new IpProcSimple();
        proc.setSubProc(subProc);

        IpDoc ipDoc = new IpDoc();
        ipDoc.setProc(proc);


        return ipDoc;
    }

    @Override
    public String objectToString(Object o) {
        IpDoc ipDoc = (IpDoc) o;
        IpProcSimple proc = ipDoc.getProc();
        if (Objects.isNull(proc)) {
            return "";
        }

        IpProcSimple subProc = proc.getSubProc();
        if (Objects.isNull(subProc)) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        if (Objects.nonNull(subProc.getFileSeq())) {
            sb.append(subProc.getFileSeq());
            sb.append(DELIM);
        }

        if (Objects.nonNull(subProc.getFileTyp())) {
            sb.append(subProc.getFileTyp());
            sb.append(DELIM);
        }

        if (Objects.nonNull(subProc.getFileSer())) {
            sb.append(subProc.getFileSer());
            sb.append(DELIM);
        }

        if (Objects.nonNull(subProc.getFileNbr())) {
            sb.append(subProc.getFileNbr());
        }

        return sb.toString();
    }

    @Override
    public void set(String s, Object o, Document document, LuceneOptions luceneOptions) {
        luceneOptions.addSortedDocValuesFieldToDocument(s, objectToString(o), document);
        luceneOptions.addFieldToDocument(s, objectToString(o), document);
    }
}
