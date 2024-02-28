package bg.duosoft.ipas.util.search;

import org.apache.lucene.document.Document;
import org.hibernate.search.bridge.LuceneOptions;
import org.hibernate.search.bridge.TwoWayFieldBridge;

import java.util.Objects;

public class ObjectToFlagBridge implements TwoWayFieldBridge {

    @Override
    public Object get(String s, Document document) {
        if (document.get(s).equals("true")){
            return true;
        }

        return false;
    }

    @Override
    public String objectToString(Object o) {
        if (Objects.isNull(o)) {
            return "false";
        }

        return "true";
    }

    @Override
    public void set(String s, Object o, Document document, LuceneOptions luceneOptions) {
        luceneOptions.addFieldToDocument(s, objectToString(o), document);
    }
}
