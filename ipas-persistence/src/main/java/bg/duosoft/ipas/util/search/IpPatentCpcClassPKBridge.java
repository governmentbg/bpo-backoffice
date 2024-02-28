package bg.duosoft.ipas.util.search;

import bg.duosoft.ipas.persistence.model.entity.patent.IpPatentCpcClassesPK;
import org.apache.lucene.document.Document;
import org.hibernate.search.bridge.LuceneOptions;
import org.hibernate.search.bridge.TwoWayFieldBridge;

public class IpPatentCpcClassPKBridge implements TwoWayFieldBridge {

    private final static String FILE_SEQ = ".fileSeq";
    private final static String FILE_TYP = ".fileTyp";
    private final static String FILE_SER = ".fileSer";
    private final static String FILE_NBR = ".fileNbr";
    private final static String CPC_EDITION_CODE = ".cpcEditionCode";
    private final static String CPC_SECTION_CODE = ".cpcSectionCode";
    private final static String CPC_CLASS_CODE = ".cpcClassCode";
    private final static String CPC_SUBCLASS_CODE = ".cpcSubclassCode";
    private final static String CPC_GROUP_CODE = ".cpcGroupCode";
    private final static String CPC_SUBGROUP_CODE = ".cpcSubgroupCode";
    private final static String CPC_QUALIFICATION_CODE = ".cpcQualificationCode";
    private final static String DELIM = "/";

    @Override
    public Object get(String s, Document document) {
        String[] split = document.get(s).split(DELIM);

        if ((split.length < 4 || split.length > 11)
                || document.get(s).chars().filter(ch -> ch == '/').count() != 10 ) {
            throw new RuntimeException("index doesn't contain correct pk");
        }

        IpPatentCpcClassesPK ipPatentCpcClassesPK = new IpPatentCpcClassesPK(
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

        return ipPatentCpcClassesPK;
    }

    @Override
    public String objectToString(Object o) {
        IpPatentCpcClassesPK ipPatentCpcClassesPK  = (IpPatentCpcClassesPK) o;

        StringBuilder sb = new StringBuilder();

        sb.append(ipPatentCpcClassesPK.getFileSeq());
        sb.append(DELIM);

        sb.append(ipPatentCpcClassesPK.getFileTyp());
        sb.append(DELIM);

        sb.append(ipPatentCpcClassesPK.getFileSer());
        sb.append(DELIM);

        sb.append(ipPatentCpcClassesPK.getFileNbr());
        sb.append(DELIM);

        sb.append(ipPatentCpcClassesPK.getCpcEditionCode());
        sb.append(DELIM);

        sb.append(ipPatentCpcClassesPK.getCpcSectionCode());
        sb.append(DELIM);

        sb.append(ipPatentCpcClassesPK.getCpcClassCode());
        sb.append(DELIM);

        sb.append(ipPatentCpcClassesPK.getCpcSubclassCode());
        sb.append(DELIM);

        sb.append(ipPatentCpcClassesPK.getCpcGroupCode());
        sb.append(DELIM);

        sb.append(ipPatentCpcClassesPK.getCpcSubgroupCode());
        sb.append(DELIM);

        sb.append(ipPatentCpcClassesPK.getCpcQualificationCode());

        return sb.toString();
    }

    @Override
    public void set(String s, Object o, Document document, LuceneOptions luceneOptions) {
        luceneOptions.addFieldToDocument(s, objectToString(o), document);
    }
}
