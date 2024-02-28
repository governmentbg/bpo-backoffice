package bg.duosoft.ipas.util.search;

import bg.duosoft.ipas.persistence.model.entity.patent.IpPatentIpcClassesPK;
import org.apache.lucene.document.Document;
import org.hibernate.search.bridge.LuceneOptions;
import org.hibernate.search.bridge.TwoWayFieldBridge;

public class IpPatentIpcClassPKBridge implements TwoWayFieldBridge {

    private final static String FILE_SEQ = ".fileSeq";
    private final static String FILE_TYP = ".fileTyp";
    private final static String FILE_SER = ".fileSer";
    private final static String FILE_NBR = ".fileNbr";
    private final static String IPC_EDITION_CODE = ".ipcEditionCode";
    private final static String IPC_SECTION_CODE = ".ipcSectionCode";
    private final static String IPC_CLASS_CODE = ".ipcClassCode";
    private final static String IPC_SUBCLASS_CODE = ".ipcSubclassCode";
    private final static String IPC_GROUP_CODE = ".ipcGroupCode";
    private final static String IPC_SUBGROUP_CODE = ".ipcSubgroupCode";
    private final static String IPC_QUALIFICATION_CODE = ".ipcQualificationCode";
    private final static String DELIM = "/";

    @Override
    public Object get(String s, Document document) {
        String[] split = document.get(s).split(DELIM);

        if ((split.length < 4 || split.length > 11)
                || document.get(s).chars().filter(ch -> ch == '/').count() != 10 ) {
            throw new RuntimeException("index doesn't contain correct pk");
        }

        IpPatentIpcClassesPK ipPatentIpcClassesPK = new IpPatentIpcClassesPK(
                split[0],
                split[1],
                Integer.parseInt(split[2]),
                Integer.parseInt(split[3]),
                split[4],
                split[5],
                split[6],
                split[7],
                split[8],
                split[9],
                split[10]);

        return ipPatentIpcClassesPK;
    }

    @Override
    public String objectToString(Object o) {
        IpPatentIpcClassesPK ipPatentIpcClassesPK  = (IpPatentIpcClassesPK) o;

        StringBuilder sb = new StringBuilder();

        sb.append(ipPatentIpcClassesPK.getFileSeq());
        sb.append(DELIM);

        sb.append(ipPatentIpcClassesPK.getFileTyp());
        sb.append(DELIM);

        sb.append(ipPatentIpcClassesPK.getFileSer());
        sb.append(DELIM);

        sb.append(ipPatentIpcClassesPK.getFileNbr());
        sb.append(DELIM);

        sb.append(ipPatentIpcClassesPK.getIpcEditionCode());
        sb.append(DELIM);

        sb.append(ipPatentIpcClassesPK.getIpcSectionCode());
        sb.append(DELIM);

        sb.append(ipPatentIpcClassesPK.getIpcClassCode());
        sb.append(DELIM);

        sb.append(ipPatentIpcClassesPK.getIpcSubclassCode());
        sb.append(DELIM);

        sb.append(ipPatentIpcClassesPK.getIpcGroupCode());
        sb.append(DELIM);

        sb.append(ipPatentIpcClassesPK.getIpcSubgroupCode());
        sb.append(DELIM);

        sb.append(ipPatentIpcClassesPK.getIpcQualificationCode());

        return sb.toString();
    }

    @Override
    public void set(String s, Object o, Document document, LuceneOptions luceneOptions) {
        luceneOptions.addFieldToDocument(s, objectToString(o), document);
    }
}
