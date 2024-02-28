package bg.duosoft.ipas.util.search;

import org.apache.lucene.document.Document;
import org.hibernate.search.bridge.LuceneOptions;
import org.hibernate.search.bridge.TwoWayFieldBridge;

public class BooleanToIndInactiveBridge implements TwoWayFieldBridge {

    @Override
    public Object get(String s, Document document) {
        String value = document.get(s);
        if (value == null) {
            return null;
        }
        if(value.equalsIgnoreCase("true")) {
            return "N";
        } else  if(value.equalsIgnoreCase( "false")) {
            return "S";
        }
        return null;
    }

    @Override
    public String objectToString(Object o) {
        Boolean value = (Boolean) o;
        if (value == null) {
            return null;
        }
        if(value == true) {
            return "N";
        } else  if(value == false) {
            return "S";
        }
        return null;
    }

    @Override
    public void set(String s, Object o, Document document, LuceneOptions luceneOptions) {
        String str = (String) o;
        Boolean value = ("N".equals(str));
        luceneOptions.addFieldToDocument(s, value.toString(), document);
    }
}
