package bg.duosoft.ipas.util.search;

import org.apache.lucene.document.Document;
import org.hibernate.search.bridge.LuceneOptions;
import org.hibernate.search.bridge.TwoWayFieldBridge;

public class BooleanToStringBridge implements TwoWayFieldBridge {

    @Override
    public Object get(String s, Document document) {
        String value = document.get(s);
        if(value.equalsIgnoreCase("true")) {
            return "M";
        } else  if(value.equalsIgnoreCase( "false")) {
            return "F";
        }
        return null;
    }

    @Override
    public String objectToString(Object o) {
        Boolean value = (Boolean) o;
        if(value == true) {
            return "M";
        } else  if(value == false) {
            return "F";
        }
        return null;
    }

    @Override
    public void set(String s, Object o, Document document, LuceneOptions luceneOptions) {
        String str = (String) o;
        Boolean value = ("M".equals(str));
        luceneOptions.addFieldToDocument(s, value.toString(), document);
    }
}
