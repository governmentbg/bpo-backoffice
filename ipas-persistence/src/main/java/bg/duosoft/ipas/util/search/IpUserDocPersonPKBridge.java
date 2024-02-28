package bg.duosoft.ipas.util.search;

import bg.duosoft.ipas.enums.UserdocPersonRole;
import bg.duosoft.ipas.persistence.model.entity.ext.core.IpUserdocPersonPK;
import org.apache.lucene.document.Document;
import org.hibernate.search.bridge.LuceneOptions;
import org.hibernate.search.bridge.TwoWayFieldBridge;

public class IpUserDocPersonPKBridge implements TwoWayFieldBridge {

    private final static String DELIM = "/";

    @Override
    public Object get(String s, Document document) {
        String[] split = document.get(s).split(DELIM);
        if (split.length != 7) {
            throw new RuntimeException("index doesn't contain correct pk");
        }

        IpUserdocPersonPK ipUserdocPersonPK = new IpUserdocPersonPK(
                split[0],
                split[1],
                Integer.parseInt(split[2]),
                Integer.parseInt(split[3]),
                Integer.parseInt(split[4]),
                Integer.parseInt(split[5]),
                UserdocPersonRole.valueOf(split[6]));

        return ipUserdocPersonPK;
    }

    @Override
    public String objectToString(Object o) {
        IpUserdocPersonPK ipUserdocPersonPK = (IpUserdocPersonPK) o;

        StringBuilder sb = new StringBuilder();

        sb.append(ipUserdocPersonPK.getDocOri());
        sb.append(DELIM);

        sb.append(ipUserdocPersonPK.getDocLog());
        sb.append(DELIM);

        sb.append(ipUserdocPersonPK.getDocSer());
        sb.append(DELIM);

        sb.append(ipUserdocPersonPK.getDocNbr());
        sb.append(DELIM);

        sb.append(ipUserdocPersonPK.getPersonNbr());
        sb.append(DELIM);

        sb.append(ipUserdocPersonPK.getAddrNbr());
        sb.append(DELIM);

        sb.append(ipUserdocPersonPK.getRole());

        return sb.toString();
    }

    @Override
    public void set(String s, Object o, Document document, LuceneOptions luceneOptions) {
        luceneOptions.addSortedDocValuesFieldToDocument(s, objectToString(o), document);
        luceneOptions.addFieldToDocument(s, objectToString(o), document);


        IpUserdocPersonPK ipUserdocPersonPK = (IpUserdocPersonPK) o;

        StringBuilder docPkSB = new StringBuilder();

        docPkSB.append(ipUserdocPersonPK.getDocOri());
        docPkSB.append(DELIM);

        docPkSB.append(ipUserdocPersonPK.getDocLog());
        docPkSB.append(DELIM);

        docPkSB.append(ipUserdocPersonPK.getDocSer());
        docPkSB.append(DELIM);

        docPkSB.append(ipUserdocPersonPK.getDocNbr());

        luceneOptions.addFieldToDocument("doc.pk", docPkSB.toString(), document );
        luceneOptions.addSortedDocValuesFieldToDocument("doc.pk", docPkSB.toString(), document );


        StringBuilder ipUserDocPersonPk = new StringBuilder();

        ipUserDocPersonPk.append(ipUserdocPersonPK.getPersonNbr());
        ipUserDocPersonPk.append(DELIM);

        ipUserDocPersonPk.append(ipUserdocPersonPK.getAddrNbr());
        luceneOptions.addFieldToDocument("userDocPerson.pk", ipUserDocPersonPk.toString(), document );
        luceneOptions.addSortedDocValuesFieldToDocument("userDocPerson.pk", ipUserDocPersonPk.toString(), document );
    }
}
